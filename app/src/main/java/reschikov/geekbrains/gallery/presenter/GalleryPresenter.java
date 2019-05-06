package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.Reply;
import reschikov.geekbrains.gallery.data.Request;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.database.AppDataBase;
import reschikov.geekbrains.gallery.data.database.MyImageDao;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Watchable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Watchable> implements Observable, Seen {

	private Observer observer;
    private final List<MyImage> list = new ArrayList<>();
    private final RecyclePresenter recyclePresenter;
    private final MyImageDao myImageDao;
    private final Single<Reply> request;

	public RecyclePresenter getRecyclePresenter() {
        return recyclePresenter;
    }

    public GalleryPresenter() {
	    recyclePresenter = new RecyclePresenter();
	    myImageDao = AppDataBase.getDatabase().getMyImageDao();
		loadFromDatabase();
	    request = new Request().toRequest();
    }

    private void loadFromDatabase(){
		Disposable disposable = myImageDao.getAll()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(myImages -> {
				for (int i = 0; i < myImages.size() ; i++) {
					myImages.get(i).setFavorite(true);
					list.add(myImages.get(i));
					observer.add(myImages.get(i));
				}
				Log.i("loadFromDatabase: ", "загрузка базы");
			}, e -> Log.e("loadFromDatabase: ", e.getMessage()));
    }

	@Override
	protected void onFirstViewAttach() {
		Disposable disposable = request.observeOn(AndroidSchedulers.mainThread())
			.subscribe(reply -> {
				List<MyImage> hits = reply.getHits();
				Log.i("onFirstViewAttach: ", "загрузка с интернета");
				if (list.isEmpty()) list.addAll(hits);
				else checkForMatch(hits);
				Log.i("onFirstViewAttach: ", "совмещение с базой");
				getViewState().updateRecyclerView();
				},
				e -> Log.e("ServerError", e.getMessage()));
	}

	private void checkForMatch(List<MyImage> hits){
		List<MyImage> temp = new ArrayList<>(list);
		label: for (int i = 0; i < hits.size(); i++) {
			for (int j = 0; j < temp.size() ; j++) {
				if (temp.get(j).equals(hits.get(i))){
					temp.remove(j);
					continue label;
				}
			}
			list.add(hits.get(i));
		}
	}

	private void removeListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.delete(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe();
	}

	private void insertListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.insert(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> Log.i("insertMyImage: ", "добавлено "),
				e -> Log.e("insertMyImage: ", e.getMessage()));
	}

	private void syncDataBase(){
		List<MyImage> removable = new ArrayList<>();
		List<MyImage> added = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRowId() != 0 && !list.get(i).isFavorite()){
				removable.add(list.get(i));
				continue;
			}
			if (list.get(i).getRowId() == 0 && list.get(i).isFavorite()) added.add(list.get(i));
		}
		if (!removable.isEmpty()) removeListMyImages(removable);
		if (!added.isEmpty()) insertListMyImages(added);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("onDestroy: ", "onDestroy()");
		syncDataBase();
		unsubscribe();
	}

	@Override
	public void subscribe(Observer observer) {
		this.observer = observer;
	}

	@Override
	public void unsubscribe() {
		observer = null;
	}

	@Override
	public void delete(MyImage myImage) {
		int index = list.indexOf(myImage);
		recyclePresenter.delete(index);
		getViewState().delete(index);
	}

	@Override
	public void setFavorite(MyImage myImage, boolean isChecked) {
		int index = list.indexOf(myImage);
		recyclePresenter.setFavorite(index, isChecked);
		getViewState().check(index);
	}

	private void removeFromDatabase(MyImage myImage){
		Disposable disposable = myImageDao.delete(myImage)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe();
	}

	private class RecyclePresenter implements Bindable{

        @Override
        public void bindView(Settable settable, int position) {
           settable.bind(list.get(position), this);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void move(int fromPos, int toPos) {
            Collections.swap(list, fromPos, toPos);
        }

        @Override
        public void delete(int pos) {
        	MyImage myImage = list.get(pos);
	        if (myImage.isFavorite()) observer.del(myImage);
	        if (myImage.getRowId() != 0) removeFromDatabase(myImage);
	        list.remove(myImage);
        }

        @Override
        public void setFavorite(int pos, boolean isChecked) {
	        MyImage myImage = list.get(pos);
	        myImage.setFavorite(isChecked);
	        if (myImage.isFavorite()) observer.add(myImage);
	        else observer.del(myImage);
        }

		@Override
		public void toSee(MyImage myImage) {
			getViewState().toLook(myImage);
		}
	}
}

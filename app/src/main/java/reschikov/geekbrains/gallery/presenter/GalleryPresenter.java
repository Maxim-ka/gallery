package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import reschikov.geekbrains.gallery.data.Reply;
import reschikov.geekbrains.gallery.data.Request;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Watchable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Watchable> implements Observable, Seen {

	private Observer observer;
    private List<MyImage> list;
    private final RecyclePresenter recyclePresenter;
    private final Single<Reply> request;

	public RecyclePresenter getRecyclePresenter() {
        return recyclePresenter;
    }

    public GalleryPresenter() {
	    recyclePresenter = new RecyclePresenter();
	    request = new Request().toRequest();
    }

	@Override
	protected void onFirstViewAttach() {
		Disposable disposable = request.observeOn(AndroidSchedulers.mainThread())
			.subscribe(reply -> {
				list = reply.getHits();
				getViewState().updateRecyclerView();
				},
				e -> Log.e("ServerError", e.getMessage()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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

	private class RecyclePresenter implements Bindable{

        @Override
        public void bindView(Settable settable, int position) {
           settable.bind(list.get(position), this);
        }

        @Override
        public int getItemCount() {
            if (list == null) return 0;
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

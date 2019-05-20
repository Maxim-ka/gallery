package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Watchable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Watchable> implements Seen {

    private final List<MyImage> list;
    private final RecyclePresenter recyclePresenter;

    @Inject
    Data data;

	public RecyclePresenter getRecyclePresenter() {
        return recyclePresenter;
    }

    public GalleryPresenter() {
		AppDagger.getAppDagger().getAppComponent().inject(this);
	    recyclePresenter = new RecyclePresenter();
	    list = data.getQueue().poll();
    }

	private void syncData(){
		List<MyImage> removable = new ArrayList<>();
		List<MyImage> added = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRowId() != 0 && !list.get(i).isFavorite()){
				removable.add(list.get(i));
				continue;
			}
			if (list.get(i).getRowId() == 0 && list.get(i).isFavorite()) added.add(list.get(i));
		}
		if (!removable.isEmpty()) data.removeListMyImages(removable);
		if (!added.isEmpty()) data.insertListMyImages(added);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		syncData();
	}

	@Override
	public void delete(MyImage myImage) {
		Log.i("Gallery delete: ", String.valueOf(myImage.getId()));
		int index = list.indexOf(myImage);
		Log.i("gallery index: ", String.valueOf(index));
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
            return list.size();
        }

        @Override
        public void move(int fromPos, int toPos) {
            Collections.swap(list, fromPos, toPos);
        }

        @Override
        public void delete(int pos) {
        	MyImage myImage = list.get(pos);
	        if (myImage.getRowId() != 0) data.removeFromDatabase(myImage);
	        list.remove(myImage);
	        data.getImageList().remove(myImage);
        }

        @Override
        public void setFavorite(int pos, boolean isChecked) {
	        MyImage myImage = list.get(pos);
	        myImage.setFavorite(isChecked);
        }

		@Override
		public void toSee(int position) {
			getViewState().toLook(list, position);
		}
	}
}

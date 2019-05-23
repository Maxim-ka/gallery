package reschikov.geekbrains.gallery.presenter;

import androidx.viewpager2.widget.ViewPager2;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.files.ImageCash;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Watchable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Watchable> implements Seen {

    private final List<MyImage> list;
    private final RecyclePresenter recyclePresenter;
    private int scrollDirection = ViewPager2.ORIENTATION_HORIZONTAL;

    @Inject Data data;
    @Inject	ImageCash imageCash;

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
			MyImage myImage = list.get(i);
			if (myImage.getRowId() != 0 && myImage.isFavorite()) continue;
			if (myImage.getRowId() == 0 && myImage.isFavorite()) {
				added.add(myImage);
				continue;
			}
			if (myImage.getRowId() != 0 && !myImage.isFavorite()) removable.add(myImage);
			imageCash.removeFileFromCache(myImage.getPreview());
			imageCash.removeFileFromCache(myImage.getUrl());
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
	public int getOrientation() {
		return scrollDirection;
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
	        if (myImage.getRowId() != 0){
		        imageCash.removeFileFromCache(myImage.getPreview());
		        imageCash.removeFileFromCache(myImage.getUrl());
		        data.removeFromDatabase(myImage);
	        }
	        list.remove(myImage);
	        data.getImageList().remove(myImage);
	        getViewState().delete(pos);
        }

        @Override
        public void setFavorite(int pos, boolean isChecked) {
	        MyImage myImage = list.get(pos);
	        myImage.setFavorite(isChecked);
	        getViewState().check(pos);
        }

		@Override
		public void toSee(int position) {
			getViewState().toLook(position);
		}

		@Override
		public void getScrollDirection(int direction) {
			scrollDirection = direction;
		}
	}
}

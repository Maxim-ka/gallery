package reschikov.geekbrains.gallery.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Watchable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Watchable> implements Seen {

    private List<MyImage> list;
    private final RecyclePresenter recyclePresenter = new RecyclePresenter();
    private int spanCount;
    @Inject Data data;

	public RecyclePresenter getRecyclePresenter() {
        return recyclePresenter;
    }

	public void setSpanCount(int spanCount) {
		this.spanCount = spanCount;
	}

	public void initGalleryPresenter() {
	    list = data.getQueue().poll();
    }

	private void syncData(){
		List<MyImage> removable = new ArrayList<>();
		List<MyImage> added = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			MyImage myImage = list.get(i);
			if (myImage.getRowId() == 0 && myImage.isFavorite()) {
				added.add(myImage);
				continue;
			}
			if (myImage.getRowId() != 0 && !myImage.isFavorite()) removable.add(myImage);
		}
		if (!removable.isEmpty()) data.removeListMyImages(removable);
		if (!added.isEmpty()) data.insertListMyImages(added);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		syncData();
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
		public int getSpanCount() {
			return spanCount;
		}
	}
}

package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2.Shown;

@InjectViewState
public class Pager2Presenter extends MvpPresenter<Shown>  implements Seen {

	private List<MyImage> imageList;
	private int lastPosition;
	private Seen seen;

	public void setImageList(List<MyImage> imageList) {
		if (this.imageList == null)	this.imageList = imageList;
		getViewState().fillAdapter(this.imageList);
	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}

	public void subscribe(Seen seen) {
		this.seen = seen;
	}

	private void unsubscribe() {
		seen = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unsubscribe();
	}

	@Override
	public void delete(MyImage myImage) {
		Log.i("Page2Presenter delete: ", String.valueOf(myImage.getId()));
		int index = imageList.indexOf(myImage);
		Log.i("Page2Presenter index: ", String.valueOf(index));
		seen.delete(myImage);
		imageList.remove(myImage);
		getViewState().deleteFragment(index);
	}

	@Override
	public void setFavorite(MyImage myImage, boolean isChecked) {
		seen.setFavorite(myImage, isChecked);
	}
}

package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.view.mainActivity.Retentive;

@InjectViewState
public class ItemPresenter extends MvpPresenter<Retentive> {

	private Seen seen;
	private MyImage myImage;

	public void setMyImage(MyImage myImage) {
		this.myImage = myImage;
	}

	public void delete(){
		Log.i("ItemPresenter delete: ", String.valueOf(myImage.getId()));
		seen.delete(myImage);
		unsubscribe();
	}

	public void setFavorite(boolean isChecked){
		seen.setFavorite(myImage, isChecked);
	}

	public void subscribe(Seen seen) {
		this.seen = seen;
	}

	private void unsubscribe() {
		seen = null;
	}
}

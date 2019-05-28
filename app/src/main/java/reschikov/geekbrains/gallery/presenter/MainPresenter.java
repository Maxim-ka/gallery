package reschikov.geekbrains.gallery.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.net.ImageUploader;
import reschikov.geekbrains.gallery.view.mainActivity.Shown;

@InjectViewState
public class MainPresenter extends MvpPresenter<Shown> implements Shown{

	@Inject	Data data;
	@Inject	ImageUploader imageUploader;

	public void init(){
		data.setShown(this);
		imageUploader.setShown(this);
	}

	@Override
	public void indicate(String message) {
		getViewState().indicate(message);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		data.setShown(null);
		imageUploader.setShown(null);
	}
}

package reschikov.geekbrains.gallery.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.Selected;

@InjectViewState
public class PagerPresenter extends MvpPresenter<Selected> {

	@Inject
	Data data;

	public PagerPresenter() {
		AppDagger.getAppDagger().getAppComponent().inject(this);
		prepareQueue();
	}

	private void prepareQueue(){
		data.prepareQueue();
		getViewState().fillAdapter(data.getListPage());
	}
}

package reschikov.geekbrains.gallery.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.Selected;

@InjectViewState
public class PagerPresenter extends MvpPresenter<Selected> implements Observer {

	@Override
    public void add(MyImage myImage){
        getViewState().add(myImage);
    }

    @Override
    public void del(MyImage myImage){
        getViewState().del(myImage);
    }
}

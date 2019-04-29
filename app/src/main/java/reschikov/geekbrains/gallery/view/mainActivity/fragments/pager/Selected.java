package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import reschikov.geekbrains.gallery.data.MyImage;


public interface Selected extends MvpView {
    @StateStrategyType(AddToEndStrategy.class)
    void add(MyImage myImage);
    @StateStrategyType(AddToEndStrategy.class)
    void del(MyImage myImage);
}

package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import reschikov.geekbrains.gallery.data.MyImage;


public interface Changeable extends MvpView {
//    @StateStrategyType(AddToEndStrategy.class)
//    void move(int fromPos, int toPos);
//    @StateStrategyType(AddToEndStrategy.class)
//    void delete(int pos);
    @StateStrategyType(AddToEndStrategy.class)
    void check(MyImage myImage);
}

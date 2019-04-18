package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


public interface Selected extends MvpView {
    @StateStrategyType(AddToEndStrategy.class)
    void add(int id);
    @StateStrategyType(AddToEndStrategy.class)
    void del(int id);
}

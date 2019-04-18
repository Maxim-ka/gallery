package reschikov.geekbrains.gallery.view.mainActivity.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MoxyBindable extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void passString(String string);
}

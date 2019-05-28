package reschikov.geekbrains.gallery.view.mainActivity;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface Shown extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void indicate(String message);
}

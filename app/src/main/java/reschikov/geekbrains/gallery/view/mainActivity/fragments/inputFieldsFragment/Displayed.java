package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface Displayed extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showParameters(String title, String string);
	@StateStrategyType(SkipStrategy.class)
	void showReply();
	@StateStrategyType(SkipStrategy.class)
	void showServerResponse(String message);
}

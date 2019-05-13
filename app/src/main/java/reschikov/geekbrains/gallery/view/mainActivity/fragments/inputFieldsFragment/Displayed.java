package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface Displayed extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showParameters(String title, String string);
	void showReply();
}

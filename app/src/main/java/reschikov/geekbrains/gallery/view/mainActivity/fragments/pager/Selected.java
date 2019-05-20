package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import java.util.List;

public interface Selected extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void fillAdapter(List<String> nameList);
}

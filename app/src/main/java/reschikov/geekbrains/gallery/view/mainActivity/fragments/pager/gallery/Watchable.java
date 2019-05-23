package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface Watchable extends MvpView {
    @StateStrategyType(AddToEndStrategy.class)
    void delete(int pos);
    @StateStrategyType(AddToEndStrategy.class)
    void check(int pos);
	@StateStrategyType(SkipStrategy.class)
	void toLook(int position);
	@StateStrategyType(SkipStrategy.class)
	void updateRecyclerView();
}

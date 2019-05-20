package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import reschikov.geekbrains.gallery.data.MyImage;

public interface Watchable extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void delete(int pos);
    @StateStrategyType(SingleStateStrategy.class)
    void check(int pos);
	@StateStrategyType(SkipStrategy.class)
	void toLook(List<MyImage> list, int position);
	@StateStrategyType(SkipStrategy.class)
	void updateRecyclerView();
}

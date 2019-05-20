package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import reschikov.geekbrains.gallery.data.MyImage;

public interface Shown extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void fillAdapter(List<MyImage> list);
	@StateStrategyType(AddToEndSingleStrategy.class)
	void deleteFragment(int position);
}

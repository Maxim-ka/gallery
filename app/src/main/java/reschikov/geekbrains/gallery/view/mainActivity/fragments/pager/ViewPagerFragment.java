package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.dagger.AppDagger;
import reschikov.geekbrains.gallery.presenter.PagerPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.GalleryFragment;

public class ViewPagerFragment extends MvpAppCompatFragment implements Selected{

    @BindView(R.id.pages) ViewPager pager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    private Unbinder unbinder;
    private FragmentAdapter fragmentAdapter;

	@InjectPresenter
    PagerPresenter presenter;

    @ProvidePresenter
    PagerPresenter providePresenter(){
	    PagerPresenter pagerPresenter = new PagerPresenter();
	    AppDagger.getAppDagger().getAppComponent().inject(pagerPresenter);
	    pagerPresenter.prepareQueue();
        return pagerPresenter;
    }

	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        unbinder = ButterKnife.bind(this, view);
		fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
		pager.setAdapter(fragmentAdapter);
	    tabLayout.setupWithViewPager(pager);
        pager.setPageTransformer(true, new DepthTransformation());
        return view;
    }

	@Override
	public void fillAdapter(List<String> nameList) {
		for (int i = 0; i < nameList.size(); i++) {
			fragmentAdapter.addGalleryFragment(new GalleryFragment(), nameList.get(i));
		}
		fragmentAdapter.notifyDataSetChanged();
		pager.setOffscreenPageLimit(calculatePageLimit());
	}

    private int calculatePageLimit(){
        int limit = fragmentAdapter.getCount() / 5;
        if (limit < 1) return 1;
        return limit;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

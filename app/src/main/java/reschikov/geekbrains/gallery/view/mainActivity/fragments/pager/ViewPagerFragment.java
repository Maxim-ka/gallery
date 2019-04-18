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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;
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
        return new PagerPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        fragmentAdapter.addGalleryFragment(new GalleryFragment());
        pager.setPageTransformer(true, new DepthTransformation());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(pager);
        if (getActivity() != null){
            MyViewModelMyImage modelMyImage =  new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelMyImage.class);
            modelMyImage.getMutableLiveData().observe(this, myImage -> {
                if (myImage.isFavorite()) presenter.add(myImage);
                else presenter.del(myImage);
            });
        }
    }

    @Override
    public void add(int id) {
        fragmentAdapter.addFragment(id);
        pager.setOffscreenPageLimit(calculatePageLimit());
    }

    @Override
    public void del(int id) {
        fragmentAdapter.deleteFragment(id);
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

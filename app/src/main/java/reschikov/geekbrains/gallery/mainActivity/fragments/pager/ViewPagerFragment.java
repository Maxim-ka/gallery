package reschikov.geekbrains.gallery.mainActivity.fragments.pager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;
import reschikov.geekbrains.gallery.data.MyViewModelSpanCount;
import reschikov.geekbrains.gallery.mainActivity.Switchable;
import reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery.GalleryFragment;

public class ViewPagerFragment extends Fragment{

    public static ViewPagerFragment newInstance(int spanCount){
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt("spanCount", spanCount);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> listTitles = new ArrayList<>();
    private ArrayList<MyImage> list;
    private View view;
    private FragmentAdapter fragmentAdapter;
    private ViewPager pager;
    private MyViewModelSpanCount modelSpanCount;
    private Switchable switchable;
    private int spanCount;

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        modelSpanCount.setValueLiveData(spanCount);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        pager = view.findViewById(R.id.pages);
        if (getActivity() != null){
            modelSpanCount =  new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelSpanCount.class);
        }
        if (savedInstanceState == null) {
            if (getArguments() != null) spanCount = getArguments().getInt("spanCount");
            getImages();
        }
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        fragmentAdapter.addGalleryFragment(GalleryFragment.newInstance(list, spanCount));
        pager.setPageTransformer(true, new DepthTransformation());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            spanCount = savedInstanceState.getInt("spanCount");
            listTitles = savedInstanceState.getStringArrayList("listTitles");
            setSpanCount(spanCount);
        }
        setListFragment(listTitles);
        pager.setAdapter(fragmentAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        if (getActivity() != null){
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if (spanCount != 2) {
                    switchable.toggleFragments(R.id.navigation_gallery_grid);
                }
            }
            MyViewModelMyImage modelMyImage =  new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelMyImage.class);
            modelMyImage.getMutableLiveData().observe(this, myImage -> {
                if (myImage.isFavorite()) {
                    fragmentAdapter.addFragment(myImage.getResource());
                    listTitles.add(String.valueOf(myImage.getResource()));
                } else {
                    fragmentAdapter.deleteFragment(myImage.getResource());
                    listTitles.remove(String.valueOf(myImage.getResource()));
                }
                pager.setOffscreenPageLimit(calculatePageLimit());
            });
        }
    }

    private void setListFragment(ArrayList<String> listTitles){
        fragmentAdapter.setListFragment(listTitles);
        pager.setOffscreenPageLimit(calculatePageLimit());
    }

    private void getImages(){
        list = new ArrayList<>();
        list.add(new MyImage(R.drawable.image1));
        list.add(new MyImage(R.drawable.image2));
        list.add(new MyImage(R.drawable.image3));
        list.add(new MyImage(R.drawable.image4));
        list.add(new MyImage(R.drawable.image5));
        list.add(new MyImage(R.drawable.image6));
    }

    private int calculatePageLimit(){
        int limit = fragmentAdapter.getCount() / 5;
        if (limit < 1) return 1;
        return limit;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        switchable = (Switchable) context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spanCount", spanCount );
        outState.putStringArrayList("listTitles", listTitles);
    }
}

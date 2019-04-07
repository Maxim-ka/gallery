package reschikov.geekbrains.gallery.mainActivity.fragments.pager;

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
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;
import reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery.GalleryFragment;

public class ViewPagerFragment extends Fragment{

    private ArrayList<String> listTitles = new ArrayList<>();
    private View view;
    private FragmentAdapter fragmentAdapter;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        pager = view.findViewById(R.id.pages);
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        fragmentAdapter.addGalleryFragment(new GalleryFragment());
        pager.setPageTransformer(true, new DepthTransformation());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            listTitles = savedInstanceState.getStringArrayList("listTitles");
            setListFragment(listTitles);
        }
        pager.setAdapter(fragmentAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        if (getActivity() != null){
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

    private int calculatePageLimit(){
        int limit = fragmentAdapter.getCount() / 5;
        if (limit < 1) return 1;
        return limit;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("listTitles", listTitles);
    }
}

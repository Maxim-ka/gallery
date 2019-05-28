package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import java.util.LinkedList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.GalleryFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter{

    private final LinkedList<Fragment> listFragment = new LinkedList<>();
    private final LinkedList<String> listTitles = new LinkedList<>();

	FragmentAdapter(@NonNull FragmentManager fm) {
		super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
	}

    void addGalleryFragment(GalleryFragment fragment, String number){
        listFragment.add(fragment);
        listTitles.add(number);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }
}

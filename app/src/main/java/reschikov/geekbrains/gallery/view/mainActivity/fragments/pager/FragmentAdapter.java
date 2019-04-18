package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import java.util.LinkedList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.FavoriteImageFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.GalleryFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter{

    private final LinkedList<Fragment> listFragment = new LinkedList<>();
    private final LinkedList<String> listTitles = new LinkedList<>();

    FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    void addGalleryFragment(GalleryFragment fragment){
        listFragment.add(0, fragment);
        if (listTitles.isEmpty()) listTitles.add(0, "Gallery");
        notifyDataSetChanged();
    }

    void addFragment(int resource) {
        listFragment.add(FavoriteImageFragment.newInstance(resource));
        listTitles.add(String.valueOf(resource));
        notifyDataSetChanged();
    }

    void deleteFragment(int resource) {
        for (int i = 1; i < listFragment.size(); i++) {
            if (listFragment.get(i).getArguments().getInt("resource") == resource){
                listFragment.remove(listFragment.get(i));
                listTitles.remove(listTitles.get(i));
                notifyDataSetChanged();
                return;
            }
        }
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

package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import java.util.LinkedList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import reschikov.geekbrains.gallery.data.MyImage;
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

    void addFragment(MyImage myImage) {
        listFragment.add(FavoriteImageFragment.newInstance(myImage.getUrl()));
        listTitles.add(String.valueOf(myImage.getId()));
        notifyDataSetChanged();
    }

    void delFragment(MyImage myImage) {
    	String title = String.valueOf(myImage.getId());
	    for (int i = 1; i < listFragment.size(); i++) {
            if (listTitles.get(i).equals(title)){
                listFragment.remove(listFragment.get(i));
                listTitles.remove(listTitles.get(i));
                break;
            }
        }
	    notifyDataSetChanged();
    }
}

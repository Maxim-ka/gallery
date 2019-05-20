package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.ItemFragment;

public class MyAdapterViewPager2 extends FragmentStateAdapter {

	private final ArrayList<Fragment> listFragments = new ArrayList<>();

	MyAdapterViewPager2(@NonNull Fragment fragment) {
		super(fragment);
	}

	void addFragment(ItemFragment fragment){
		listFragments.add(fragment);
	}

	void delFragment(int position){
		ItemFragment itemFragment = (ItemFragment) listFragments.get(position);
		itemFragment.setSeen(null);
		listFragments.remove(itemFragment);
		notifyItemRemoved(position);
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {
		return listFragments.get(position);
	}

	@Override
	public int getItemCount() {
		return listFragments.size();
	}
}

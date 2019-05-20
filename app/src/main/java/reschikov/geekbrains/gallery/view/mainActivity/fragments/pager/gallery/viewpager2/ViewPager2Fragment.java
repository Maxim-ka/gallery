package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.presenter.Pager2Presenter;
import reschikov.geekbrains.gallery.presenter.Seen;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.ItemFragment;

public class ViewPager2Fragment extends MvpAppCompatFragment implements Shown {

	public static ViewPager2Fragment newInstance(ArrayList<MyImage> myImages, int position){
		ViewPager2Fragment fragment = new ViewPager2Fragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("myImages", myImages);
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.view_pager_2) ViewPager2 pager;
	private Unbinder unbinder;
	private MyAdapterViewPager2 adapterViewPager;
	private Seen seen;

	@InjectPresenter
	Pager2Presenter presenter;

	@ProvidePresenter
	Pager2Presenter providePresenter(){
		return new Pager2Presenter();
	}

	public void setSeen(Seen seen) {
		this.seen = seen;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_pager_2, container, false);
		unbinder = ButterKnife.bind(this, view);
		adapterViewPager = new MyAdapterViewPager2(this);
		pager.setAdapter(adapterViewPager);
		if (getArguments() != null){
			List<MyImage> myImageList = getArguments().getParcelableArrayList("myImages");
			int position = getArguments().getInt("position");
			if (myImageList != null) {
				presenter.setImageList(myImageList);
				presenter.setLastPosition(position);
				presenter.subscribe(seen);
			}
		}
		pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//		pager.setPageTransformer(new DepthTransformation());
		return view;
	}

	@Override
	public void fillAdapter(List<MyImage> list){
		for (int i = 0; i < list.size(); i++) {
			ItemFragment itemFragment = ItemFragment.newInstance(list.get(i));
			itemFragment.setSeen(presenter);
			adapterViewPager.addFragment(itemFragment);
		}
		adapterViewPager.notifyDataSetChanged();
		pager.setCurrentItem(presenter.getLastPosition(), false);
		pager.setOffscreenPageLimit(adapterViewPager.getItemCount() / 5);
	}

	@Override
	public void deleteFragment(int position) {
		adapterViewPager.delFragment(position);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		presenter.setLastPosition(pager.getCurrentItem());
		unbinder.unbind();
		seen = null;
	}
}

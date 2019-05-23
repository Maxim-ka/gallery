package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.presenter.GalleryPresenter;
import reschikov.geekbrains.gallery.presenter.Seen;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.MyAdapterRecycleView;

public class ViewPager2Fragment extends Fragment{

	public static ViewPager2Fragment newInstance(int position){
		ViewPager2Fragment fragment = new ViewPager2Fragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.view_pager_2) ViewPager2 pager;
	private Unbinder unbinder;
	private Seen seen;

	public void setSeen(Seen seen) {
		this.seen = seen;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_pager_2, container, false);
		unbinder = ButterKnife.bind(this, view);
		MyAdapterRecycleView adapter = new MyAdapterRecycleView(((GalleryPresenter) seen).getRecyclePresenter(), true);
		pager.setAdapter(adapter);
		if (getArguments() != null){
			int position = getArguments().getInt("position");
			pager.setCurrentItem(position, false);
		}
		pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageScrollStateChanged(int state) {
				super.onPageScrollStateChanged(state);
				if (state == ViewPager2.SCROLL_STATE_IDLE){
					if (pager.getOrientation() != seen.getOrientation()){
						pager.setOrientation(seen.getOrientation());
						// TODO: 22.05.2019 сделать задержку анимацией
					}
				}
			}
		});
//		pager.setPageTransformer(new DepthTransformation());
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		seen = null;
	}
}

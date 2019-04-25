package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.presenter.ItemPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.Retentive;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.ViewPagerFragment;

public class ItemFragment  extends MvpAppCompatFragment implements Retentive {

	static ItemFragment newInstance(MyImage myImage){
		ItemFragment fragment = new ItemFragment();
		Bundle args = new Bundle();
		args.putParcelable("myImage", myImage);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.image) ImageView imageView;
	@BindView(R.id.chip_favorite) Chip chipFavorite;
	@BindView(R.id.chip_delete) Chip chipDelete;
	private MyImage myImage;
	private Unbinder unbinder;

	@InjectPresenter
	ItemPresenter presenter;

	@ProvidePresenter
	ItemPresenter providePresenter(){
		return new ItemPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item, container, false);
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		unbinder = ButterKnife.bind(this, view);
		if (getArguments() != null){
			myImage = getArguments().getParcelable("myImage");
		}
		if (myImage != null){
			imageView.setImageResource(myImage.getResource());
			chipFavorite.setChecked(myImage.isFavorite());
		}
		presenter.setMyImage(myImage);
		if (getActivity() != null) {
			ViewPagerFragment viewPager = (ViewPagerFragment) getActivity().getSupportFragmentManager().findFragmentByTag("ViewPager");
			if (viewPager != null){
				presenter.subscribe(((GalleryFragment) viewPager.getFragmentAdapter().getItem(0)).presenter);
			}
		}
		chipDelete.setOnLongClickListener(v -> {
			presenter.delete();
			if (getActivity()!= null) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
			return true;
		});
		chipFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setFavorite(isChecked));
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
		presenter.unsubscribe();
	}
}

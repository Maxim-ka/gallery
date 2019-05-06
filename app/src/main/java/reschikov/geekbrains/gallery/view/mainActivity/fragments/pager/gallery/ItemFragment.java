package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.presenter.ItemPresenter;
import reschikov.geekbrains.gallery.presenter.Seen;
import reschikov.geekbrains.gallery.view.mainActivity.Retentive;

public class ItemFragment  extends MvpAppCompatFragment implements Retentive {

	private static final String KEY_MY_IMAGE = "keyMyImage";

	static ItemFragment newInstance(MyImage myImage){
		ItemFragment fragment = new ItemFragment();
		Bundle args = new Bundle();
		args.putParcelable(KEY_MY_IMAGE, myImage);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.image) ImageView imageView;
	@BindView(R.id.chip_favorite) Chip chipFavorite;
	@BindView(R.id.chip_delete) Chip chipDelete;
	private MyImage myImage;
	private Unbinder unbinder;
	private Seen seen;

	void setSeen(Seen seen) {
		this.seen = seen;
	}

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
			myImage = getArguments().getParcelable(KEY_MY_IMAGE);
		}
		if (myImage != null){
			Glide.with(this).load(myImage.getUrl()).into(imageView);
			chipFavorite.setChecked(myImage.isFavorite());
			presenter.setMyImage(myImage);
		}
		if (seen != null) presenter.subscribe(seen);
		chipDelete.setOnLongClickListener(v -> {
			presenter.delete();
			if (getActivity() != null) {
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
}
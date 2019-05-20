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
import androidx.cardview.widget.CardView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.ImageUploader;
import reschikov.geekbrains.gallery.presenter.ItemPresenter;
import reschikov.geekbrains.gallery.presenter.Seen;
import reschikov.geekbrains.gallery.view.mainActivity.Retentive;

public class ItemFragment  extends MvpAppCompatFragment implements Retentive {

	private static final String KEY_MY_IMAGE = "keyMyImage";

	public static ItemFragment newInstance(MyImage myImage){
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

	public void setSeen(Seen seen) {
		this.seen = seen;
	}

	@InjectPresenter
	ItemPresenter presenter;

	@ProvidePresenter
	ItemPresenter providePresenter(){
		return new ItemPresenter();
	}

	@Inject
	ImageUploader imageUploader;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item, container, false);
		CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(0, 16, 0, 16);
		view.setLayoutParams(layoutParams);
		unbinder = ButterKnife.bind(this, view);
		AppDagger.getAppDagger().getAppComponent().inject(this);
		if (getArguments() != null){
			myImage = getArguments().getParcelable(KEY_MY_IMAGE);
		}
		if (myImage != null){
			imageUploader.download(imageView, myImage.getUrl());
			chipFavorite.setChecked(myImage.isFavorite());
			presenter.setMyImage(myImage);
		}
		if (seen != null) presenter.subscribe(seen);
		chipDelete.setOnLongClickListener(v -> {
			presenter.delete();
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

package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.ImageUploader;

public class FavoriteImageFragment extends Fragment {

	private static final String KEY_URL = "keyUrl";

	public static FavoriteImageFragment newInstance(String url){
        FavoriteImageFragment fragment = new FavoriteImageFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.image_view) AppCompatImageView imageView;
    private String url;
    private Unbinder unbinder;

    @Inject
    ImageUploader imageUploader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_image, container, false);
	    unbinder = ButterKnife.bind(this, view);
	    AppDagger.getAppDagger().getAppComponent().inject(this);
        if (getArguments() != null){
            url = getArguments().getString(KEY_URL);
        }
	    imageUploader.download(imageView, url);
        return view;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_image, container, false);
	    unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null){
            url = getArguments().getString(KEY_URL);
        }
	    Glide.with(this).load(url).into(imageView);
        return view;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

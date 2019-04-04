package reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import reschikov.geekbrains.gallery.R;

public class FavoriteImageFragment extends Fragment {

    public static FavoriteImageFragment newInstance(int resource){
        FavoriteImageFragment fragment = new FavoriteImageFragment();
        Bundle args = new Bundle();
        args.putInt("resource", resource);
        fragment.setArguments(args);
        return fragment;
    }

    private int resource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_image, container, false);
        if (savedInstanceState == null && getArguments() != null){
            resource = getArguments().getInt("resource");
        }
        if (savedInstanceState != null){
            resource = savedInstanceState.getInt("resource");
        }
        AppCompatImageView imageView = view.findViewById(R.id.image_view);
        imageView.setImageResource(resource);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("resource", resource);
    }
}

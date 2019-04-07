package reschikov.geekbrains.gallery.mainActivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.mainActivity.Counted;
import reschikov.geekbrains.gallery.mainActivity.Switchable;

public class HomeFragment extends Fragment {

    private Counted counted;
    private Switchable switchable;
    private AppCompatImageView image;

    public AppCompatImageView getImage() {
        return image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        image = view.findViewById(R.id.image_home);
        ViewCompat.setTransitionName(image, Rule.HOME);
        MaterialButton button = view.findViewById(R.id.button);
        button.setOnClickListener(viewButton -> counted.count());
        view.setOnClickListener(viewButton -> switchable.toggleFragments(R.id.navigation_notifications));
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        counted = (Counted)context;
        switchable = (Switchable) context;
    }
}

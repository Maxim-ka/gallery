package reschikov.geekbrains.gallery;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class NotificationsFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Counted counted;
    private Switchable switchable;
    private AppCompatImageView image;

    public AppCompatImageView getImage() {
        return image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        image = view.findViewById(R.id.image_home);
        ViewCompat.setTransitionName(image, Rule.HOME);
        counted.reset();
        if (getContext() != null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                view.setBackground(getContext().getDrawable(R.drawable.animated_vector_bell));
            } else {
                AnimatedVectorDrawableCompat avdCompact = (AnimatedVectorDrawableCompat) getContext().getResources().getDrawable(R.drawable.animated_vector_bell);
                view.setBackground(avdCompact);
            }
            ((Animatable) view.getBackground()).start();
        }

        view.setOnClickListener(viewButton -> switchable.toggleFragments(R.id.navigation_home));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        counted = (Counted)context;
        switchable = (Switchable) context;
    }
}

package reschikov.geekbrains.gallery.view.mainActivity.fragments;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.presenter.MoxyPresenter;

public class NotificationsFragment extends MvpAppCompatFragment implements MoxyBindable{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.input_text)TextInputEditText inputText;
    @BindView(R.id.button_take)MaterialButton take;
    @BindView(R.id.text_view)AppCompatTextView textView;
    @BindDrawable(R.drawable.animated_vector_bell) Drawable bell;
    @InjectPresenter
    MoxyPresenter presenter;

    @ProvidePresenter
    MoxyPresenter providePresenter(){
        return new MoxyPresenter();
    }

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        take.setOnClickListener(viewButton -> presenter.tie(inputText.getText().toString()));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            view.setBackground(bell);
        } else {
            AnimatedVectorDrawableCompat avdCompact = (AnimatedVectorDrawableCompat) bell;
            view.setBackground(avdCompact);
        }
        ((Animatable) view.getBackground()).start();
        return view;
    }

    @Override
    public void passString(String string) {
        inputText.setText(null);
        textView.setText(string);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

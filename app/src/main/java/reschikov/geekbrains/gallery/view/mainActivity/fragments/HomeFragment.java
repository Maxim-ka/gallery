package reschikov.geekbrains.gallery.view.mainActivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.presenter.SimplePresenter;
import reschikov.geekbrains.gallery.view.mainActivity.Counted;

public class HomeFragment extends Fragment implements SimpleBindable {

    @BindView(R.id.input_text)TextInputEditText inputText;
    @BindView(R.id.button_take)MaterialButton take;
    @BindView(R.id.text_view) AppCompatTextView textView;
    @BindView(R.id.button) MaterialButton giveNotice;
    private Unbinder unbinder;
    private Counted counted;
    private SimplePresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new SimplePresenter(this);
        take.setOnClickListener(viewButton -> presenter.tie(inputText.getText().toString()));
        giveNotice.setOnClickListener(viewButton -> counted.count());
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            String text = savedInstanceState.getString("keyString");
            textView.setText(text);
            presenter.restore(text);
        }
    }

    @Override
    public void passString(String string) {
        inputText.setText(null);
        textView.setText(string);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("keyString", textView.getText().toString());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        counted = (Counted)context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

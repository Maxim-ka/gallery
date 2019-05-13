package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;
import reschikov.geekbrains.gallery.presenter.FieldsPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.Changing;

public class FieldsFragment extends MvpAppCompatFragment implements Displayed, View.OnClickListener {

	@BindView(R.id.what_layout)TextInputLayout whatLayout;
    @BindView(R.id.what) TextInputEditText what;
	@BindView(R.id.choice_type)	AppCompatButton buttonType;
	@BindView(R.id.choice_orientation) AppCompatButton buttonOrientation;
    @BindView(R.id.choice_category) AppCompatButton buttonCategory;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindBool(R.bool.is_portrait) boolean isPortrait;
    private Unbinder unbinder;
    private Changing changing;

	@InjectPresenter
	FieldsPresenter presenter;

	@ProvidePresenter
	FieldsPresenter providePresenter(){
		return new FieldsPresenter();
	}

	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container,false);
	    unbinder = ButterKnife.bind(this, view);
//        LinearLayout linearLayout = view.findViewById(R.id.bottom_sheet);
//        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED){
//                    login.setEnabled(false);
//                    password.setEnabled(false);
//                } else{
//                    login.setEnabled(true);
//                    password.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
        return view;
    }

    @OnClick({R.id.choice_type, R.id.choice_orientation, R.id.choice_category, R.id.fab})
    @Override
    public void onClick(View v) {
		switch (v.getId()){
			case R.id.choice_type:
				showSelectionDialog(presenter.getTypes(), "image Types");
				break;
			case R.id.choice_orientation:
				showSelectionDialog(presenter.getOrientations(), "image Orientations");
				break;
			case R.id.choice_category:
				showSelectionDialog(presenter.getCategories(), "image Categories");
				break;
			case R.id.fab:
				presenter.sendRequest(what.getText().toString());
				break;
		}
    }

    private void showSelectionDialog(SelectionParameter[] parameters, String title){
    	if (getActivity()== null) return;
    	SpinnerDialogFragment.newInstance(parameters, title)
		    .show(getActivity().getSupportFragmentManager(), title);
    }

    private void outputError(TextInputLayout textInputLayout, String message){
        textInputLayout.setError(message);
        textInputLayout.setEndIconDrawable(R.drawable.ic_warning_red_24dp);
        shake(textInputLayout);
    }

    private void shake(TextInputLayout textInputLayout){
        float[] offset = new float[]{5.0f, -5.0f, -20.0f, 20.0f, 25.0f, -25.0f, -5.0f, 5.0f, 15.0f, -15.0f};
        ObjectAnimator animation = ObjectAnimator.ofFloat(textInputLayout, "translationX", offset);
        animation.setDuration(1_000);
        animation.start();
    }

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		changing = (Changing) context;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void showParameters(String title, String string) {
		switch (title){
			case "image Types":
				buttonType.setText(string);
				break;
			case "image Orientations":
				buttonOrientation.setText(string);
				break;
			case "image Categories":
				buttonCategory.setText(string);
				break;
		}
	}

	@Override
	public void showReply() {
		int menuGallery = (isPortrait) ? R.id.navigation_gallery_list : R.id.navigation_gallery_grid;
		changing.toggleFragments(menuGallery);
	}
}

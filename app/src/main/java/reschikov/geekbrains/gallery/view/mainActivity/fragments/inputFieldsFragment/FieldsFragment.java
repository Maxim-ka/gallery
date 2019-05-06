package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;
import reschikov.geekbrains.gallery.presenter.FieldsPresenter;

public class FieldsFragment extends Fragment  implements SpinnerDialogFragment.DialogListener {

	@BindView(R.id.what_layout)TextInputLayout whatLayout;
    @BindView(R.id.what) TextInputEditText what;
	@BindView(R.id.choice_type)	AppCompatButton buttonType;
	@BindView(R.id.choice_orientation) AppCompatButton buttonOrientation;
    @BindView(R.id.choice_category) AppCompatButton buttonCategory;
	@BindArray(R.array.image_type) String[] types;
	@BindArray(R.array.orientation) String[] orientations;
    @BindArray(R.array.category) String[] categories;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container,false);
	    unbinder = ButterKnife.bind(this, view);
	    FieldsPresenter presenter = new FieldsPresenter();
	    if (savedInstanceState == null){
		    presenter.setTypes(types);
		    presenter.setOrientations(orientations);
		    presenter.setCategories(categories);
	    }
	    buttonType.setOnClickListener(v -> showSelectionDialog(presenter.getTypes(), "image Types"));
		buttonOrientation.setOnClickListener(v -> showSelectionDialog(presenter.getOrientations(), "image Orientations"));
		buttonCategory.setOnClickListener(v -> showSelectionDialog(presenter.getCategories(), "image Categories"));


//        loginInputLayout = view.findViewById(R.id.login_layout);
//        passwordInputLayout = view.findViewById(R.id.password_layout);
//        login = view.findViewById(R.id.login);
//        password = view.findViewById(R.id.password);
//        final MaterialButton apply = view.findViewById(R.id.button);
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
//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
//
//        login.addTextChangedListener(new MyAdapterTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (errorLogin && !s.toString().equals("")){
//                    loginInputLayout.setError(null);
//                    loginInputLayout.setEndIconDrawable(null);
//                    errorLogin = false;
//                }
//            }
//        });
//        password.addTextChangedListener(new MyAdapterTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count > before){
//                    char symbol = s.charAt(start);
//                    if (symbol < 48 || symbol > 57) errorPassword = true;
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (errorPassword && !s.toString().equals("")){
//                    try {
//                        Integer.parseInt(s.toString());
//                        revokeError();
//                        errorPassword = false;
//                    }catch (RuntimeException e){
//                        outputError(passwordInputLayout, "не число: " + s);
//                        errorPassword = true;
//                    }
//                }
//            }
//        });
//
//        apply.setOnClickListener(v -> {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                float depth = apply.getElevation();
//                ObjectAnimator animation = ObjectAnimator.ofFloat(apply, "translationZ", -depth / 2, depth / 2);
//                animation.setDuration(500);
//                animation.start();
//            }
//            if (login.getText() == null || login.getText().toString().equals("")){
//                outputError(loginInputLayout, "Не заполнено поле");
//                errorLogin = true;
//            } else loginInputLayout.setEndIconDrawable(R.drawable.ic_check_green_24dp);
//            if (password.getText() == null || password.getText().toString().equals("")){
//                outputError(passwordInputLayout, "Не заполнено поле");
//                errorPassword = true;
//            } else if (errorPassword) shake(passwordInputLayout);
//            else passwordInputLayout.setEndIconDrawable(R.drawable.ic_check_green_24dp);
//        });
        return view;
    }

    private void showSelectionDialog(SelectionParameter[] parameters, String title){
    	if (getActivity()== null) return;
    	SpinnerDialogFragment.newInstance(parameters, title)
		    .show(getActivity().getSupportFragmentManager(), title);
    }

	@Override
	public void onDialogPositiveClick(SpinnerDialogFragment dialog) {

	}

	@Override
	public void onDialogNegativeClick(SpinnerDialogFragment dialog) {

	}

//    private void revokeError(){
//        passwordInputLayout.setError(null);
//        passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
//    }

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
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}


}

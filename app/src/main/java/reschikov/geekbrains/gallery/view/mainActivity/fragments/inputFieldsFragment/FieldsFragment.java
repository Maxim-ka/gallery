package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import javax.inject.Inject;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.SelectionParameter;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.ImageUploader;
import reschikov.geekbrains.gallery.presenter.FieldsPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.Changing;
import reschikov.geekbrains.gallery.view.mainActivity.dialogs.Notice;

public class FieldsFragment extends MvpAppCompatFragment implements Displayed, View.OnClickListener, AdapterView.OnItemSelectedListener {

	@BindView(R.id.what_layout)TextInputLayout whatLayout;
    @BindView(R.id.what) TextInputEditText what;
	@BindView(R.id.choice_type)	AppCompatButton buttonType;
	@BindView(R.id.choice_orientation) AppCompatButton buttonOrientation;
    @BindView(R.id.choice_category) AppCompatButton buttonCategory;
    @BindView(R.id.choice_per_page) AppCompatSpinner spinner;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindBool(R.bool.is_portrait) boolean isPortrait;
    @BindBool(R.bool.is_small) boolean isSmall;
    private Unbinder unbinder;
    private Changing changing;
    private SharedPreferences preferences;

    @Inject
	ImageUploader uploader;

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
		AppDagger.getAppDagger().getAppComponent().inject(this);
	    if (getContext() != null){
		    preferences = getContext().getSharedPreferences("request parameters", Context.MODE_PRIVATE);
	    	SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, createNumberOnPage());
		    ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_list_item_checked);
		    spinner.setAdapter(spinnerAdapter);
		    spinner.setSelection(preferences.getInt("position" ,Rule.DEFAULT_PER_PAGE - 3));
	    }
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
		if (requestCode == 5 && permissions.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
				grantResults[1] == PackageManager.PERMISSION_GRANTED)){
			if (uploader.imageCash.isExternalStorageWritable()) {
				uploader.setAllowed(true);
				return;
			}
			createNoticeNoAccess();
		}
		uploader.setAllowed(false);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getActivity() == null) return;
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
			ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
		} else {
			if (uploader.imageCash.isExternalStorageWritable()) uploader.setAllowed(true);
			else {
				createNoticeNoAccess();
				uploader.setAllowed(false);
			}
		}
	}

	private void createNoticeNoAccess(){
		if (getView() != null )Snackbar.make(getView(), "There is no access to the folder MyGallery",Snackbar.LENGTH_INDEFINITE).show();
//		if (getActivity() != null) Notice.newInstance("There is no access to the folder MyGallery", "no access to external storage")
//			.show(getActivity().getSupportFragmentManager(), "no access to external storage");
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

    @OnItemSelected({R.id.choice_per_page})
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		int number = (int) parent.getItemAtPosition(position);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("position", position);
		editor.apply();
	    presenter.setNumber(number);
	}
	@OnItemSelected({R.id.choice_per_page})
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

    private Integer[] createNumberOnPage(){
		Integer[] arr = new Integer[198];
	    for (int i = 0; i < arr.length; i++) {
		    arr[i] = i + 3;
	    }
	    return arr;
    }

    private void showSelectionDialog(SelectionParameter[] parameters, String title){
    	if (getActivity()== null) return;
    	if (isSmall) getActivity().getSupportFragmentManager().beginTransaction()
		    .add(R.id.frame_master, SpinnerDialogFragment.newInstance(parameters, title))
	        .addToBackStack(null)
		    .commit();
    	else SpinnerDialogFragment.newInstance(parameters, title)
		    .show(getActivity().getSupportFragmentManager(), title);

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

	@Override
	public void showServerResponse(String message) {
		if (getActivity() != null) Notice.newInstance(message, "Server Error")
			.show(getActivity().getSupportFragmentManager(), "tag Server Error");
	}
}

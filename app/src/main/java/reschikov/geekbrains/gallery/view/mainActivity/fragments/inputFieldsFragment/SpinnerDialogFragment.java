package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;

public class SpinnerDialogFragment extends AppCompatDialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	static SpinnerDialogFragment newInstance(SelectionParameter[] parameters, String title){
		SpinnerDialogFragment fragment = new SpinnerDialogFragment();
		Bundle args = new Bundle();
		args.putParcelableArray("parameters", parameters);
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.viewList) ListView listView;
	@BindView(R.id.but_cancel) Button butCancel;
	@BindView(R.id.but_ok) Button butOk;
	private Unbinder unbinder;
	private String title;
	private Selectable selectable;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_spinner, container, false);
		unbinder = ButterKnife.bind(this,view);
		float scope = 0;
		if (getArguments() != null){
			title = getArguments().getString("title");
			if (getDialog() != null) getDialog().setTitle(title);
			SelectionParameter[] parameters = (SelectionParameter[]) getArguments().getParcelableArray("parameters");
			if (getContext() != null && parameters != null){
				if (parameters.length > 6) scope = 0.85f;
				listView.setAdapter(new MyAdapterSpinner(getContext(), android.R.layout.select_dialog_multichoice, parameters));
			}
		}
		if (scope != 0 && getActivity() != null) {
			DisplayMetrics metricsB = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metricsB);
			listView.getLayoutParams().height = (int) (metricsB.heightPixels / metricsB.density * scope);
		}
		return view;
	}

	@OnClick({R.id.but_cancel, R.id.but_ok})
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.but_cancel:
				resetChange((MyAdapterSpinner) listView.getAdapter());
				break;
			case R.id.but_ok:
		}
		selectable.toFinish(title);
		dismiss();
	}

	@OnItemClick({R.id.viewList})
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CheckedTextView checkBox = (CheckedTextView) view;
		SelectionParameter parameter = (SelectionParameter) parent.getItemAtPosition(position);
		parameter.setSelected(!checkBox.isChecked());
		if (selectable != null) selectable.select(position, !checkBox.isChecked(), title);
		checkBox.setChecked(!checkBox.isChecked());
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		if (getActivity() != null){
			FieldsFragment fragment = (FieldsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("Tag_Home");
			if (fragment != null) selectable = fragment.presenter;
		}
		return dialog;
	}

	private void resetChange(MyAdapterSpinner adapter){
		for (int i = 0; i < adapter.getCount(); i++) {
			selectable.select(i, false, title);
		}
	}

	public void onStart(){
		super.onStart();
		if (getDialog() != null){
			Window window = getDialog().getWindow();
			if (window != null){
				window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				window.setGravity(Gravity.CENTER);
			}
		}
	}

	@Override
	public void onCancel(@NonNull DialogInterface dialog) {
		super.onCancel(dialog);
		resetChange((MyAdapterSpinner) listView.getAdapter());
		selectable.toFinish(title);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
		selectable = null;
	}

	private static class MyAdapterSpinner extends ArrayAdapter<SelectionParameter> {

		MyAdapterSpinner(@NonNull Context context, int resource, @NonNull SelectionParameter[] objects) {
			super(context, resource, objects);
		}

		@NonNull
		@Override
		public View getView(int position, View convertView, @NonNull ViewGroup parent) {
			CheckedTextView checkBox;
			if (convertView != null) checkBox = (CheckedTextView) convertView;
			else checkBox = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.select_dialog_multichoice, parent, false);
			SelectionParameter parameter = getItem(position);
			if (parameter != null){
				checkBox.setChecked(parameter.isSelected());
				checkBox.setText(parameter.getParameter());
			}
			return checkBox;
		}
	}
}

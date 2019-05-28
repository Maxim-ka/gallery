package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.collection.ArraySet;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.SelectionParameter;

public class SpinnerDialogFragment extends AppCompatDialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	private static final float SCOPE_HEIGHT = 0.5f;

	static SpinnerDialogFragment newInstance(SelectionParameter[] parameters, String title, String key){
		SpinnerDialogFragment fragment = new SpinnerDialogFragment();
		Bundle args = new Bundle();
		args.putParcelableArray("parameters", parameters);
		args.putString("title", title);
		args.putString("key", key);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.viewList) ListView listView;
	@BindView(R.id.but_cancel) Button butCancel;
	@BindView(R.id.but_ok) Button butOk;
	private Unbinder unbinder;
	private String title;
	private String key;
	private Selectable selectable;
	private SharedPreferences preferences;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_spinner, container, false);
		unbinder = ButterKnife.bind(this,view);
		float scope = 0;
		if (getArguments() != null){
			key = getArguments().getString("key");
			title = getArguments().getString("title");
			if (getDialog() != null) getDialog().setTitle(title);
			SelectionParameter[] parameters = (SelectionParameter[]) getArguments().getParcelableArray("parameters");
			if (getContext() != null && parameters != null){
				preferences = getContext().getSharedPreferences(Rule.REQUEST_PARAMETERS, Context.MODE_PRIVATE);
				if (parameters.length > 6) scope = SCOPE_HEIGHT;
				listView.setAdapter(new MyAdapterSelectionParameters(getContext(), parameters));
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
				resetChange((MyAdapterSelectionParameters) listView.getAdapter());
				break;
			case R.id.but_ok:
				saveSelectedSettings();
				break;
		}
		selectable.toFinish(title);
		dismiss();
	}

	private void saveSelectedSettings(){
		Set<String> set = preferences.getStringSet(key, new ArraySet<>());
		if (set == null) return;
		if (!set.isEmpty()) set.clear();
		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			SelectionParameter parameter = (SelectionParameter) listView.getAdapter().getItem(i);
			if (parameter.isSelected())	set.add(parameter.getParameter());
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet(key, set);
		editor.apply();
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
			FieldsFragment fragment = (FieldsFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Rule.TAG_SEARCH);
			if (fragment != null) selectable = fragment.presenter;
		}
		return dialog;
	}

	private void resetChange(MyAdapterSelectionParameters adapter){
		for (int i = 0; i < adapter.getCount(); i++) {
			selectable.select(i, false, title);
		}
		Set<String> set = preferences.getStringSet(key, new ArraySet<>());
		if (set != null && !set.isEmpty()){
			set.clear();
			SharedPreferences.Editor editor = preferences.edit();
			editor.putStringSet(key, set);
			editor.apply();
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
		resetChange((MyAdapterSelectionParameters) listView.getAdapter());
		selectable.toFinish(title);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
		selectable = null;
	}

	private static class MyAdapterSelectionParameters extends ArrayAdapter<SelectionParameter> {

		MyAdapterSelectionParameters(@NonNull Context context, @NonNull SelectionParameter[] objects) {
			super(context, android.R.layout.select_dialog_multichoice, objects);
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

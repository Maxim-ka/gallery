package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;

public class SpinnerDialogFragment extends AppCompatDialogFragment implements View.OnClickListener{

	public interface DialogListener {
		void onDialogPositiveClick(SpinnerDialogFragment dialog);
		void onDialogNegativeClick(SpinnerDialogFragment dialog);
	}

	static SpinnerDialogFragment newInstance(SelectionParameter[] parameters, String title){
		SpinnerDialogFragment fragment = new SpinnerDialogFragment();
		Bundle args = new Bundle();
		args.putParcelableArray("parameters", parameters);
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.list_item) ListView listView;
	@BindView(R.id.but_cancel) Button butCancel;
	@BindView(R.id.but_ok) Button butOk;
	private DialogListener listener;
	private Unbinder unbinder;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_spinner, container, false);
		unbinder = ButterKnife.bind(this,view);
		setStyle(STYLE_NORMAL, 0);
		if (getArguments() != null){
			SelectionParameter[] parameters = (SelectionParameter[]) getArguments().getParcelableArray("parameters");
			if (getContext() != null && parameters != null){
				MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(), R.layout.item_adapter_spinner_drop_down, parameters);
				listView.setAdapter(adapter);
			}
		}
		return view;
	}

	@OnClick({R.id.but_cancel, R.id.but_ok})
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.but_cancel:
//				listener.onDialogNegativeClick(this);
				dismiss();
				break;
			case R.id.but_ok:

				break;
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		if (getArguments() != null){
			String title = getArguments().getString("title");
			dialog.setTitle(title);
		}

		return dialog;
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
//		listener = (DialogListener) context;
	}

	@Override
	public void onCancel(@NonNull DialogInterface dialog) {
		super.onCancel(dialog);
//		listener.onDialogNegativeClick(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

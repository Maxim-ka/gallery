package reschikov.geekbrains.gallery.view.mainActivity.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import reschikov.geekbrains.gallery.R;

public class Notice extends DialogFragment {

	public static Notice newInstance(String message){
		Notice fragment = new Notice();
		Bundle args = new Bundle();
		args.putString("message", message);
		fragment.setArguments(args);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		if (getActivity() != null){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("server response");
			builder.setIcon(R.drawable.ic_warning_red_24dp);
			if (getArguments() != null){
				String message = getArguments().getString("message");
				builder.setMessage(message);
			}
			builder.setPositiveButton("ok", (DialogInterface dialog, int id) -> dialog.dismiss());
			return builder.create();
		}
		return super.onCreateDialog(savedInstanceState);
	}
}

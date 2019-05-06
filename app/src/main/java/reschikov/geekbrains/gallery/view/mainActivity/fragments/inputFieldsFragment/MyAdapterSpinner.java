package reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.google.android.material.checkbox.MaterialCheckBox;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;

public class MyAdapterSpinner extends ArrayAdapter<SelectionParameter> {


	MyAdapterSpinner(@NonNull Context context, int resource, @NonNull SelectionParameter[] objects) {
		super(context, resource, objects);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		MaterialCheckBox checkBox;
		if (convertView != null) checkBox = (MaterialCheckBox) convertView;
		else checkBox = (MaterialCheckBox) LayoutInflater.from(getContext()).inflate(R.layout.item_adapter_spinner_drop_down, parent, false);
		SelectionParameter parameter = getItem(position);
		if ( parameter != null){
			checkBox.setChecked(parameter.isSelected());
			checkBox.setText(parameter.getParameter());
		}
		return checkBox;
	}
}

package reschikov.geekbrains.gallery.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectionParameter implements Parcelable {

	private String parameter;
	private boolean selected;

	private SelectionParameter(Parcel in) {
		parameter = in.readString();
		selected = in.readByte() != 0;
	}

	public static final Creator<SelectionParameter> CREATOR = new Creator<SelectionParameter>() {
		@Override
		public SelectionParameter createFromParcel(Parcel in) {
			return new SelectionParameter(in);
		}

		@Override
		public SelectionParameter[] newArray(int size) {
			return new SelectionParameter[size];
		}
	};

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public SelectionParameter(String parameter) {
		this.parameter = parameter;
		selected = true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(parameter);
		dest.writeByte((byte) (selected ? 1 : 0));
	}
}

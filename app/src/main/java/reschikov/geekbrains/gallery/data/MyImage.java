package reschikov.geekbrains.gallery.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MyImage implements Parcelable {

	@SerializedName("id")
	private int id;

	@SerializedName("webformatURL")
    private String url;

	@SerializedName("previewURL")
    private String preview;

    private boolean favorite;

	public static final Creator<MyImage> CREATOR = new Creator<MyImage>() {
		@Override
		public MyImage createFromParcel(Parcel in) {
			return new MyImage(in);
		}

		@Override
		public MyImage[] newArray(int size) {
			return new MyImage[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	private MyImage(Parcel in) {
		id = in.readInt();
        url = in.readString();
        preview = in.readString();
        favorite = in.readByte() != 0;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(url);
		dest.writeString(preview);
		dest.writeByte((byte) (favorite ? 1 : 0));
	}
}

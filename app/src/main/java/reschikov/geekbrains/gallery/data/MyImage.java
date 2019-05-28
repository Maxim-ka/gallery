package reschikov.geekbrains.gallery.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "table_myImage")
public class MyImage implements Parcelable {

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	private transient long rowId;

	@ColumnInfo(name = "id_image")
	@SerializedName("id")
	private int id;

	@ColumnInfo(name = "webformatURL")
	@SerializedName("webformatURL")
    private String url;

	@ColumnInfo(name = "previewURL")
	@SerializedName("previewURL")
    private String preview;

	@Ignore
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

	public long getRowId() {
		return rowId;
	}

	public void setRowId(long rowId) {
		this.rowId = rowId;
	}

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

	public MyImage() {}

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

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof  MyImage)) return false;
		MyImage image = (MyImage) obj;
		return id == image.id;
	}
}

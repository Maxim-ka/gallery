package reschikov.geekbrains.gallery.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MyImage implements Parcelable {

    private int resource;
    private boolean favorite;

    private MyImage(Parcel in) {
        resource = in.readInt();
        favorite = in.readByte() != 0;
    }


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

    public int getResource() {
        return resource;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public MyImage(int resource) {
        this.resource = resource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resource);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }
}

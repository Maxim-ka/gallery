package reschikov.geekbrains.gallery.presenter;

import reschikov.geekbrains.gallery.data.MyImage;

public interface Seen {
	void delete(MyImage myImage);
	void setFavorite(MyImage myImage, boolean isChecked);
}

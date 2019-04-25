package reschikov.geekbrains.gallery.presenter;

import reschikov.geekbrains.gallery.data.MyImage;

public interface Observer {
	void add(MyImage myImage);
	void del(MyImage myImage);
}

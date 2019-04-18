package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.presenter.Bindable;

public interface Settable {
    void bind(MyImage myImage, final Bindable bindable);
}

package reschikov.geekbrains.gallery.presenter;

import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;

public interface Bindable {
    void bindView(Settable settable, int position);
    int getItemCount();
    void move(int fromPos, int toPos);
    void delete(int pos);
    void setFavorite(int pos, boolean isChecked);
    void toSee(int position);
    int getSpanCount();
}

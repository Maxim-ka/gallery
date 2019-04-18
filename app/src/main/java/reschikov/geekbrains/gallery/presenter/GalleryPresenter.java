package reschikov.geekbrains.gallery.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Collections;
import java.util.List;
import reschikov.geekbrains.gallery.data.ListMyImage;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Settable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.Changeable;

@InjectViewState
public class GalleryPresenter extends MvpPresenter<Changeable>{

    private final List<MyImage> list;
    private final MyViewModelMyImage modelMyImage;
    private final RecyclePresenter recyclePresenter;

    public RecyclePresenter getRecyclePresenter() {
        return recyclePresenter;
    }

    public GalleryPresenter(MyViewModelMyImage modelMyImage) {
        this.modelMyImage = modelMyImage;
        list = new ListMyImage().getMyImageList();
        recyclePresenter = new RecyclePresenter();
    }

    private class RecyclePresenter implements Bindable{

        @Override
        public void bindView(Settable settable, int position) {
           settable.bind(list.get(position), this);
           getViewState().check(list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list == null) return 0;
            return list.size();
        }

        @Override
        public void move(int fromPos, int toPos) {
            Collections.swap(list, fromPos, toPos);
            Log.i("move", String.format("from %d to %d", fromPos, toPos));
        }

        @Override
        public void delete(int pos) {
            if (list.get(pos).isFavorite()){
                list.get(pos).setFavorite(false);
                modelMyImage.setValueLiveData(list.get(pos));
            }
            Log.i("delete", String.valueOf(list.get(pos)));
            list.remove(pos);
        }

        @Override
        public void setFavorite(int pos, boolean isChecked) {
            MyImage myImage = list.get(pos);
            myImage.setFavorite(isChecked);
            modelMyImage.setValueLiveData(myImage);
            getViewState().check(myImage);
            Log.i("favorite", String.format("%d - %s", myImage.getResource(), myImage.isFavorite()));
        }
    }
}

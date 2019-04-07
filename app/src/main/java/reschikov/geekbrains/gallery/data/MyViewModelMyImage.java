package reschikov.geekbrains.gallery.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModelMyImage extends ViewModel {

    private final MutableLiveData<MyImage> liveData = new MutableLiveData<>();

    public MutableLiveData<MyImage> getMutableLiveData() {
        return liveData;
    }

    public void setValueLiveData(MyImage image){
        liveData.setValue(image);
    }
}

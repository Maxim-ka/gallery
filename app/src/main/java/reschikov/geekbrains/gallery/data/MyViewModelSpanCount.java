package reschikov.geekbrains.gallery.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModelSpanCount extends ViewModel {

    private MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public LiveData<Integer> getLiveData() {
        return liveData;
    }

    public void setValueLiveData(int spanCol){
        liveData.setValue(spanCol);
    }
}

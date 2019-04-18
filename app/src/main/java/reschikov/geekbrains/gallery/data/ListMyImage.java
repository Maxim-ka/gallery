package reschikov.geekbrains.gallery.data;

import java.util.ArrayList;
import java.util.List;

import reschikov.geekbrains.gallery.R;

public class ListMyImage {

    private List<MyImage> myImageList;

    public List<MyImage> getMyImageList() {
        return myImageList;
    }

    public ListMyImage() {
        myImageList = new ArrayList<>();
        myImageList.add(new MyImage(R.drawable.image1));
        myImageList.add(new MyImage(R.drawable.image2));
        myImageList.add(new MyImage(R.drawable.image3));
        myImageList.add(new MyImage(R.drawable.image4));
        myImageList.add(new MyImage(R.drawable.image5));
        myImageList.add(new MyImage(R.drawable.image6));
    }
}

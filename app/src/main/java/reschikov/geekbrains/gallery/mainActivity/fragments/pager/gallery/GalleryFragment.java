package reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;
import reschikov.geekbrains.gallery.data.MyViewModelSpanCount;

public class GalleryFragment extends Fragment{

    private MyAdapterRecycleView myAdapter;
    private RecyclerView recyclerView;
    private Transition transition;
    private ArrayList<MyImage> list;
    private MyViewModelMyImage modelMyImage;
    private int spanCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_gallery, container, false);
        transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_item_gallery);
        if (getActivity() != null){
            MyViewModelSpanCount modelSpanCount =  new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelSpanCount.class);
            modelSpanCount.getLiveData().observe(this, this::setLayoutManager);
            modelMyImage = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelMyImage.class);
        }
        if (savedInstanceState == null) getImages();
        else {
            list = savedInstanceState.getParcelableArrayList("myImageList");
            spanCount = savedInstanceState.getInt("spanCount");
        }
        myAdapter = new MyAdapterRecycleView(list, modelMyImage);
        recyclerView.setAdapter(myAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        new ItemTouchHelper(new MyItemTouchHelperCallback(myAdapter)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    private void setLayoutManager(int spanCount){
        this.spanCount = spanCount;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        TransitionManager.beginDelayedTransition(recyclerView, transition);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.notifyDataSetChanged();
    }

    private void getImages(){
        list = new ArrayList<>();
        list.add(new MyImage(R.drawable.image1));
        list.add(new MyImage(R.drawable.image2));
        list.add(new MyImage(R.drawable.image3));
        list.add(new MyImage(R.drawable.image4));
        list.add(new MyImage(R.drawable.image5));
        list.add(new MyImage(R.drawable.image6));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spanCount", spanCount);
        outState.putParcelableArrayList("myImageList", list);
    }
}

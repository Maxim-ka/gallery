package reschikov.geekbrains.gallery.mainActivity.fragments.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;
import reschikov.geekbrains.gallery.R;

public class GalleryFragment extends Fragment {

    public static GalleryFragment newInstance(int spanCount){
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt("spanCount", spanCount);
        fragment.setArguments(args);
        return fragment;
    }

    private GridLayoutManager layoutManager;
    private MyAdapterRecycleView myAdapter;
    private RecyclerView recyclerView;
    private Transition transition;
    private int spanCount;

    public int getSpanCount() {
        return spanCount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_gallery, container, false);
        transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_item_gallery);
        if (savedInstanceState == null) spanCount = (getArguments() != null) ? getArguments().getInt("spanCount") : 1;
        else  spanCount = savedInstanceState.getInt("spanCount");
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapterRecycleView(getImage());
        recyclerView.setAdapter(myAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        new ItemTouchHelper(new MyItemTouchHelperCallback(myAdapter)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    public void setLayoutManager(int spanCount){
        this.spanCount = spanCount;
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        TransitionManager.beginDelayedTransition(recyclerView, transition);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.notifyDataSetChanged();
    }

    private List<Integer> getImage(){
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.image1);
        list.add(R.drawable.image2);
        list.add(R.drawable.image3);
        list.add(R.drawable.image4);
        list.add(R.drawable.image5);
        list.add(R.drawable.image6);
        return list;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spanCount", spanCount);
    }
}

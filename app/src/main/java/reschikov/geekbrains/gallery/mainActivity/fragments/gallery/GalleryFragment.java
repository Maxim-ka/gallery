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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import reschikov.geekbrains.gallery.R;

public class GalleryFragment extends Fragment {

    private MyAdapterRecycleView myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myAdapter = new MyAdapterRecycleView(getImage());
        recyclerView.setAdapter(myAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlag = 0;
                int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlag, swipeFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myAdapter.delete(viewHolder.getAdapterPosition());
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
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
}

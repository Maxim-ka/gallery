package reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.material.chip.Chip;
import java.util.Collections;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.MyViewModelMyImage;

class MyAdapterRecycleView extends RecyclerView.Adapter {

    private final List<MyImage> list;
    private MyViewModelMyImage modelMyImage;

    MyAdapterRecycleView(List<MyImage> list, MyViewModelMyImage modelMyImage) {
        this.list = list;
        this.modelMyImage = modelMyImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false), parent.getDisplay(), modelMyImage);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        MyImage myImage = list.get(position);
        myViewHolder.bind(myImage);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    void move(int fromPos, int toPos){
        Collections.swap(list, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    void delete(int pos){
        if (list.get(pos).isFavorite()){
            list.get(pos).setFavorite(false);
            modelMyImage.setValueLiveData(list.get(pos));
        }
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final Chip chip;
        private final MyViewModelMyImage modelMyImage;

        MyViewHolder(@NonNull View itemView, Display display, MyViewModelMyImage modelMyImage) {
            super(itemView);
            this.modelMyImage = modelMyImage;
            DisplayMetrics metricsB = new DisplayMetrics();
            display.getMetrics(metricsB);
            itemView.getLayoutParams().height = calculateHeightItem((int) (metricsB.heightPixels / metricsB.density));
            imageView = itemView.findViewById(R.id.image);
            chip = itemView.findViewById(R.id.chip);
        }

        void bind(final MyImage myImage) {
            imageView.setImageResource(myImage.getResource());
            chip.setChecked(myImage.isFavorite());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                myImage.setFavorite(isChecked);
                modelMyImage.setValueLiveData(myImage);
            });
        }

        private int calculateHeightItem(int height){
            if (height < 600)  return height;
            return (int) (height / 1.8f);
        }
    }
}

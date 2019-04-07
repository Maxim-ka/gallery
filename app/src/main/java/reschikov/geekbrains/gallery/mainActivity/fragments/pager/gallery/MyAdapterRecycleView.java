package reschikov.geekbrains.gallery.mainActivity.fragments.pager.gallery;

import android.util.DisplayMetrics;
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

class MyAdapterRecycleView extends RecyclerView.Adapter implements Removing{

    private final List<MyImage> list;
    private MyViewModelMyImage modelMyImage;

    MyAdapterRecycleView(List<MyImage> list, MyViewModelMyImage modelMyImage) {
        this.list = list;
        this.modelMyImage = modelMyImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        boolean isPortrait = parent.getContext().getResources().getBoolean(R.bool.is_portrait);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        DisplayMetrics metricsB = new DisplayMetrics();
        parent.getDisplay().getMetrics(metricsB);
        float scope = (isPortrait) ? 0.3f : 0.5f;
        itemView.getLayoutParams().height = (int) (metricsB.heightPixels / metricsB.density * scope);
        return new MyViewHolder(itemView, modelMyImage, this);
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

    @Override
    public void delete(int pos){
        if (list.get(pos).isFavorite()){
            list.get(pos).setFavorite(false);
            modelMyImage.setValueLiveData(list.get(pos));
        }
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final Chip chipFavorite;
        private final Chip chipDelete;
        private final MyViewModelMyImage modelMyImage;

        MyViewHolder(@NonNull View itemView, MyViewModelMyImage modelMyImage, final Removing removing) {
            super(itemView);
            this.modelMyImage = modelMyImage;
            imageView = itemView.findViewById(R.id.image);
            chipFavorite = itemView.findViewById(R.id.chip_favorite);
            chipDelete = itemView.findViewById(R.id.chip_delete);
            chipDelete.setOnLongClickListener(v -> {
                removing.delete(getAdapterPosition());
                return true;
            });
        }

        void bind(final MyImage myImage) {
            imageView.setImageResource(myImage.getResource());
            chipFavorite.setOnCheckedChangeListener(null);
            chipFavorite.setChecked(myImage.isFavorite());
            chipFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                myImage.setFavorite(isChecked);
                modelMyImage.setValueLiveData(myImage);
            });
        }
    }
}

package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.presenter.Bindable;

class MyAdapterRecycleView extends RecyclerView.Adapter implements Removing{

    private final Bindable bindable;

    MyAdapterRecycleView(Bindable bindable) {
        this.bindable = bindable;
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
        return new MyViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        bindable.bindView(myViewHolder, position);
    }

    @Override
    public int getItemCount() {
        return bindable.getItemCount();
    }

    void move(int fromPos, int toPos){
        bindable.move(fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void delete(int pos){
        bindable.delete(pos);
        notifyItemRemoved(pos);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements Settable {

        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.chip_favorite) Chip chipFavorite;
        @BindView(R.id.chip_delete) Chip chipDelete;

        MyViewHolder(@NonNull View itemView, final Removing removing) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chipDelete.setOnLongClickListener(v -> {
                removing.delete(getAdapterPosition());
                return true;
            });
        }

        @Override
        public void bind(final MyImage myImage, final Bindable bindable) {
	        Glide.with(imageView.getContext()).load(myImage.getPreview()).into(imageView);
	        imageView.setOnClickListener(v -> bindable.toSee(myImage));
            chipFavorite.setOnCheckedChangeListener(null);
            chipFavorite.setChecked(myImage.isFavorite());
            chipFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> bindable.setFavorite(getAdapterPosition(), isChecked));
        }
    }
}
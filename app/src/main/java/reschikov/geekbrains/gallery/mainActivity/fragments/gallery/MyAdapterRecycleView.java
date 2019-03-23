package reschikov.geekbrains.gallery.mainActivity.fragments.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import reschikov.geekbrains.gallery.R;

public class MyAdapterRecycleView extends RecyclerView.Adapter {

    private List<Integer> list;

    MyAdapterRecycleView(List<Integer> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        int number = list.get(position);
        myViewHolder.bind(number);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void delete(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        void bind(int number){
            imageView.setImageResource(number);
        }
    }
}

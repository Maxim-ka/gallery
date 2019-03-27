package reschikov.geekbrains.gallery.mainActivity.fragments.gallery;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final MyAdapterRecycleView myAdapter;

    MyItemTouchHelperCallback(MyAdapterRecycleView myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        myAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        myAdapter.delete(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) return;
        if (viewHolder != null) viewHolder.itemView.animate().scaleX(0.8f).scaleY(0.8f).setDuration(300);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            float width = viewHolder.itemView.getWidth();
            float offset = Math.abs(dX);
            if (width / 4 <= offset){
                viewHolder.itemView.setAlpha(1.0f - offset / width );
            } else viewHolder.itemView.setAlpha(1.0f);
        }
    }
}

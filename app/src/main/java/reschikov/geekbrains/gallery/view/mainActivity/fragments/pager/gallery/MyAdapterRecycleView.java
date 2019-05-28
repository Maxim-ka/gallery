package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.material.chip.Chip;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.ImageUploader;
import reschikov.geekbrains.gallery.presenter.Bindable;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.viewpager2.Observer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_CENTER;

public class MyAdapterRecycleView extends RecyclerView.Adapter implements Removing{

	private static final float HEIGHT_PARENT_DISPLAY = 0.5f;
	private final Bindable bindable;
    private final Observer observer;
    private final boolean isFullView;

	public MyAdapterRecycleView(Bindable bindable, Observer observer) {
        this.bindable = bindable;
        this.observer = observer;
        isFullView = observer != null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
	    if (isFullView){
		    itemView.getLayoutParams().height = MATCH_PARENT;
	    } else {
		    DisplayMetrics metricsB = new DisplayMetrics();
		    parent.getDisplay().getMetrics(metricsB);
		    itemView.getLayoutParams().height = (int) (metricsB.heightPixels / metricsB.density * HEIGHT_PARENT_DISPLAY);
	    }
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

	@Override
	public boolean isFullView() {
		return isFullView;
	}

	@Override
	public void setScrollDirection(int direction) {
		observer.changeOrientation(direction);
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder implements Settable {

        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.chip_favorite) Chip chipFavorite;
        @BindView(R.id.chip_delete) Chip chipDelete;
        @Inject ImageUploader imageUploader;
        private final Removing removing;

        MyViewHolder(@NonNull View itemView, final Removing removing) {
            super(itemView);
            this.removing = removing;
            ButterKnife.bind(this, itemView);
	        AppDagger.getAppDagger().getAppComponent().inject(this);
            chipDelete.setOnLongClickListener(v -> {
                removing.delete(getAdapterPosition());
                return true;
            });
	        if (removing.isFullView()) itemView.setOnTouchListener((v, event) -> {
	        	if (event.getAction() == MotionEvent.ACTION_MOVE){
	        		int historySize = event.getHistorySize();
	        		if (historySize > 1){
				        v.performClick();
				        float initialX = event.getHistoricalAxisValue(MotionEvent.AXIS_X, 1);
				        float initialY = event.getHistoricalAxisValue(MotionEvent.AXIS_Y, 1);
				        float currentX = event.getAxisValue(MotionEvent.AXIS_X);
				        float currentY = event.getAxisValue(MotionEvent.AXIS_Y);
				        if (initialX == currentX && initialY != currentY){
					         removing.setScrollDirection(ViewPager2.ORIENTATION_VERTICAL);
					        return true;
				        }
				        if (initialY == currentY && initialX != currentX){
				        	removing.setScrollDirection(ViewPager2.ORIENTATION_HORIZONTAL);
					        return true;
				        }
			        }
	        	}
		        return false;
	        });
        }

        @Override
        public void bind(final MyImage myImage, final Bindable bindable) {
	        String url;
	        ImageView.ScaleType scaleType;
        	if (removing.isFullView()) {
        		url = myImage.getUrl();
		        scaleType = FIT_CENTER;
	        } else {
		        url =  myImage.getPreview();
		        scaleType = (bindable.getSpanCount() == 1) ? FIT_CENTER : CENTER_CROP;
		        imageView.setOnClickListener(v -> bindable.toSee(getAdapterPosition()));
	        }
	        imageView.setScaleType(scaleType);
		    imageUploader.download(imageView, myImage, url);
            chipFavorite.setOnCheckedChangeListener(null);
            chipFavorite.setChecked(myImage.isFavorite());
            chipFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> bindable.setFavorite(getAdapterPosition(), isChecked));
        }
    }
}

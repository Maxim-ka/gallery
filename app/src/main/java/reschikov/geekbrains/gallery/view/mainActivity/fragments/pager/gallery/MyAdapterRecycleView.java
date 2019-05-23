package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.net.Uri;
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
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.ImageUploader;
import reschikov.geekbrains.gallery.presenter.Bindable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MyAdapterRecycleView extends RecyclerView.Adapter implements Removing{

    private final Bindable bindable;
    private final boolean isFullView;

	public MyAdapterRecycleView(Bindable bindable, boolean isFullView) {
        this.bindable = bindable;
        this.isFullView = isFullView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
	    if (isFullView){
		    itemView.getLayoutParams().height = MATCH_PARENT;
	    } else {
		    boolean isPortrait = parent.getContext().getResources().getBoolean(R.bool.is_portrait);
		    DisplayMetrics metricsB = new DisplayMetrics();
		    parent.getDisplay().getMetrics(metricsB);
		    float scope = (isPortrait) ? 0.3f : 0.5f;
		    itemView.getLayoutParams().height = (int) (metricsB.heightPixels / metricsB.density * scope);
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
		bindable.getScrollDirection(direction);
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder implements Settable {

        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.chip_favorite) Chip chipFavorite;
        @BindView(R.id.chip_delete) Chip chipDelete;
        @Inject ImageUploader imageUploader;
        private Removing removing;

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
        	if (removing.isFullView()) url = myImage.getUrl();
	        else {
		        url =  myImage.getPreview();
		        imageView.setOnClickListener(v -> bindable.toSee(getAdapterPosition()));
	        }
		    imageUploader.download(imageView, url);
	        renameImage(myImage, url);
            chipFavorite.setOnCheckedChangeListener(null);
            chipFavorite.setChecked(myImage.isFavorite());
            chipFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> bindable.setFavorite(getAdapterPosition(), isChecked));
        }

        private void renameImage(MyImage myImage, String url){
        	if (url.startsWith(Rule.PREFIX_HTTPS)){
        		if (url.equals(myImage.getPreview())){
        			myImage.setPreview(Uri.parse(myImage.getPreview()).getLastPathSegment());
        			return;
		        }
        	    if (url.equals(myImage.getUrl())) myImage.setUrl(Uri.parse(myImage.getUrl()).getLastPathSegment());
	        }
        }
    }
}

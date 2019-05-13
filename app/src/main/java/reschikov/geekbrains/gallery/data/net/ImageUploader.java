package reschikov.geekbrains.gallery.data.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUploader {

	private Context context;

	public ImageUploader(Context context) {
		this.context = context;
	}

	public void download(ImageView imageView, String url){
		Glide.with(context).load(url).into(imageView);
	}
}

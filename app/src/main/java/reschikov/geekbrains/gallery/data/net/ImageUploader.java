package reschikov.geekbrains.gallery.data.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.files.ImageCash;

public class ImageUploader {

	private Context context;
	private boolean allowed;
	public ImageCash imageCash;

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public ImageUploader(Context context) {
		this.context = context;
		imageCash = AppDagger.getAppDagger().getAppComponent().getImageCash();
	}

	public void download(ImageView imageView, String url){
		if (allowed) loadWithCaching(imageView, url);
		else loadByDefault(imageView, url);
	}

	private void loadByDefault(ImageView imageView, String url){
		Glide.with(context).load(url).into(imageView);
	}

	private void loadWithCaching(ImageView imageView, String url){
		if (url.startsWith(Rule.PREFIX_HTTPS))	{
			downloadViaInternet(imageView, url);
		} else {
			File file = imageCash.getFile(url);
			if (file != null) Glide.with(context).load(file).into(imageView);
		}
	}

	private void downloadViaInternet(ImageView imageView, String url){
		GlideApp
			.with(context)
			.asBitmap()
			.load(url)
			.listener(new RequestListener<Bitmap>() {
				@Override
				public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
					Snackbar.make(imageView, String.format("failed to load from %s", url), Snackbar.LENGTH_INDEFINITE).show();
					Toast.makeText(context, String.format("failed to load from %s", url), Toast.LENGTH_LONG).show();
					return false;
				}

				@Override
				public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
					imageCash.saveToCash(resource, url);
					return false;
				}
			})
			.into(imageView);
	}
}

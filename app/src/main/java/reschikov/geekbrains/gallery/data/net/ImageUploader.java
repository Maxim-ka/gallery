package reschikov.geekbrains.gallery.data.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.files.ImageCash;
import reschikov.geekbrains.gallery.view.mainActivity.Shown;

public class ImageUploader {

	private static final String DOT = ".";
	private final Context context;
	private boolean allowed;
	private Shown shown;

	@Inject  public ImageCash imageCash;

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setShown(Shown shown) {
		this.shown = shown;
	}

	public ImageUploader(Context context) {
		this.context = context;
	}

	public void download(ImageView imageView, MyImage myImage, String url){
		if (allowed) loadWithCaching(imageView, myImage, url);
		else loadByDefault(imageView, url);
	}

	private void loadByDefault(ImageView imageView, String url){
		Glide.with(context).load(url).into(imageView);
	}

	private void loadWithCaching(ImageView imageView, MyImage myImage, String url){
		if (url.startsWith(Rule.PREFIX_HTTPS))	{
			downloadViaInternet(imageView, myImage, url);
		} else {
			File file = imageCash.getFile(url);
			if (file != null) Glide.with(context).load(file).into(imageView);
		}
	}

	private void downloadViaInternet(ImageView imageView, final MyImage myImage, final String url){
		GlideApp
			.with(context)
			.asBitmap()
			.load(url)
			.listener(new RequestListener<Bitmap>() {
				@Override
				public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
					reportAnError(String.format("failed to load from %s, error %s", url, e.getMessage()));
					return false;
				}

				@Override
				public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
					try {
						String newName = renameImage(myImage, url);
						imageCash.saveToCash(resource, newName);
					} catch (IOException e) {
						reportAnError(String.format("not saved %s, error %s", url, e.getMessage()));
					}
					return false;
				}
			})
			.into(imageView);
	}

	private String renameImage(MyImage myImage, String url) {
		String name;
		if (url.equals(myImage.getPreview())){
			name = Uri.parse(myImage.getPreview()).getLastPathSegment();
			myImage.setPreview(name);
		} else {
			name = Uri.parse(myImage.getUrl()).getLastPathSegment();
			if (name != null){
				StringBuilder sb = new StringBuilder(name);
				int index = sb.lastIndexOf(DOT);
				sb.replace(0, index, String.valueOf(myImage.getId()));
				name = sb.toString();
				myImage.setUrl(name);
			}
		}
		return name;
	}

	private void reportAnError(String message){
		shown.indicate(message);
	}
}

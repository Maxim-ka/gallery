package reschikov.geekbrains.gallery.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.data.files.ImageCash;
import reschikov.geekbrains.gallery.data.net.ImageUploader;

@Module
class ImageUploaderModule {

	private final Context context;

	ImageUploaderModule(Context context) {
		this.context = context;
	}

	@Singleton
	@Provides
	ImageCash getImageCash(){
		return new ImageCash();
	}

	@Singleton
	@Provides
	ImageUploader provideImageUploader(){
		ImageUploader imageUploader = new ImageUploader(context);
		imageUploader.imageCash = getImageCash();
		return imageUploader;
	}
}

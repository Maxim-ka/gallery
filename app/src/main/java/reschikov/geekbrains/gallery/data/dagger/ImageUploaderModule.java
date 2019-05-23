package reschikov.geekbrains.gallery.data.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.data.files.ImageCash;
import reschikov.geekbrains.gallery.data.net.ImageUploader;

@Module
class ImageUploaderModule {

	private Context context;

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
		return new ImageUploader(context);
	}
}

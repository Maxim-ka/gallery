package reschikov.geekbrains.gallery.data.files;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCash {

	public void saveToCash(Bitmap bitmap, String name){
		if (!isExternalStorageWritable()) return;
		String newName = Uri.parse(name).getLastPathSegment();
		File fileImage = new File(getAlbumStorageDir(), newName);
		try (FileOutputStream fos = new FileOutputStream(fileImage)) {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (IOException e) {
			Log.e("saveToCash: ", e.getMessage());
		}
	}

	public File getFile(String name){
		File file;
		if ((file = new File(getAlbumStorageDir(), name)).exists()) return file;
		return null;
	}

	public void removeFileFromCache(String name){
		File file = new File(getAlbumStorageDir(), name);
		if (file.exists()) file.delete();
	}

	public boolean isExternalStorageWritable()	{
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	private File getAlbumStorageDir() {
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyGallery");
		if (!file.exists() && !file.mkdirs()) throw new RuntimeException("directory MyGallery not created");
		return file;
	}
}

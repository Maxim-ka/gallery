package reschikov.geekbrains.gallery.data.files;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCash {

	private static final int QUALITY = 100;

	public void saveToCash(Bitmap bitmap, String name) throws IOException {
		if (!isExternalStorageWritable()) return;
		File fileImage = new File(getAlbumStorageDir(), name);
		try (FileOutputStream fos = new FileOutputStream(fileImage)) {
			bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fos);
		}
	}

	public File getFile(String name){
		File file;
		if ((file = new File(getAlbumStorageDir(), name)).exists()) return file;
		return null;
	}

	public String[] getFileList(){
		return getAlbumStorageDir().list();
	}

	public void clearCache(){
		File[] files = getAlbumStorageDir().listFiles();
		if (files == null) return;
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
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

package reschikov.geekbrains.gallery.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

public class Data {

	private List<MyImage> imageList = new ArrayList<>();

	@Inject
	MyImageDao myImageDao;

	public List<MyImage> getImageList() {
		return imageList;
	}

	public Data() {
		if (AppDagger.getAppDagger() != null){
			AppDagger.getAppDagger().getAppComponent().inject(this);
			loadFromDatabase();
		}
	}

	public void removeFromDatabase(MyImage myImage){
		Disposable disposable = myImageDao.delete(myImage)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> imageList.remove(myImage));
	}

	public void removeListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.delete(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe();
	}

	public void insertListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.insert(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> {
					Log.i("insertMyImage: ", "добавлено ");
					imageList.clear();
					loadFromDatabase();
				},
				e -> Log.e("insertMyImage: ", e.getMessage()));
	}

	public void downloadServer(List<MyImage> hits){
		List<MyImage> temp = new ArrayList<>(imageList);
		label: for (int i = 0; i < hits.size(); i++) {
			for (int j = 0; j < temp.size() ; j++) {
				if (temp.get(j).equals(hits.get(i))){
					temp.remove(j);
					continue label;
				}
			}
			imageList.add(hits.get(i));
		}
	}

	public void loadFromDatabase(){
		Disposable disposable = myImageDao.getAll()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(myImages -> {
				for (int i = 0; i < myImages.size() ; i++) {
					myImages.get(i).setFavorite(true);
					imageList.add(myImages.get(i));
				}
//				Log.i("loadFromDatabase: ", "загрузка базы");
			}, e -> Log.e("loadFromDatabase: ", e.getMessage()));
	}
}

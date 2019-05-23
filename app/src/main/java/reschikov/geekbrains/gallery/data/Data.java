package reschikov.geekbrains.gallery.data;

import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

public class Data {

	private int numberPage;
	private final Queue<List<MyImage>> queue = new LinkedList<>();
	private final List<MyImage> imageList = new ArrayList<>();
	private final List<String> listPage = new ArrayList<>();

	@Inject
	MyImageDao myImageDao;

	public void setNumberPage(int number) {
		numberPage = (number <= Rule.DEFAULT_PER_PAGE)? 1 : number / Rule.DEFAULT_PER_PAGE;
	}

	public List<MyImage> getImageList() {
		return imageList;
	}

	public Queue<List<MyImage>> getQueue() {
		return queue;
	}

	public List<String> getListPage() {
		return listPage;
	}

	public Data() {
		if (AppDagger.getAppDagger() != null){
			AppDagger.getAppDagger().getAppComponent().inject(this);
			loadFromDatabase();
		}
	}

	public void prepareQueue(){
		if (!queue.isEmpty()) queue.clear();
		if (!listPage.isEmpty()) listPage.clear();
		int limit = imageList.size() / numberPage;
		int divisionResidue = imageList.size() % numberPage;
		int capacity = (divisionResidue == 0) ? limit : limit + divisionResidue;
		int sizeList = limit;
		int first = 0;
		for (int i = 0; i < numberPage; i++) {
			listPage.add(String.format(Locale.getDefault(),"%d - %d", first + 1, sizeList));
			List<MyImage> list = new ArrayList<>(capacity);
			for (int j = first; j < sizeList; j++) {
				list.add(imageList.get(j));
			}
			queue.add(list);
			first += limit;
			sizeList = (numberPage - 2 > i) ? sizeList + limit : sizeList + (imageList.size() - sizeList);
		}
	}

	public void removeFromDatabase(MyImage myImage){
		final String name = myImage.getPreview();
		Disposable disposable = myImageDao.delete(myImage)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> indicateCompletion(String.format("%s removed from base", name)),
				e -> indicateCompletion(String.format("%s not deleted, %s", name, e.getMessage())));
	}

	public void removeListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.delete(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> indicateCompletion("removed from base"),
				e -> indicateCompletion(String.format("removal problem %s", e.getMessage())));
	}

	public void insertListMyImages(List<MyImage> list){
		Disposable disposable = myImageDao.insert(list)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> indicateCompletion("added to base"),
				e -> indicateCompletion(String.format("error adding to database, %s", e.getMessage())));
	}

	public void reload(){
		if (numberPage == 0) return;
		imageList.clear();
		loadFromDatabase();
	}

	public void downloadServer(List<MyImage> hits){
		if (imageList.isEmpty()){
			imageList.addAll(hits);
			return;
		}
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

	void loadFromDatabase(){
		Disposable disposable = myImageDao.getAll()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(myImages -> {
				setNumberPage(myImages.size());
				for (int i = 0; i < myImages.size() ; i++) {
					myImages.get(i).setFavorite(true);
					imageList.add(myImages.get(i));
				}
				indicateCompletion("base load");
			},
			e -> indicateCompletion(String.format("base load %s", e.getMessage())),
			() -> indicateCompletion("base is empty"));
	}

	private void indicateCompletion(String message){
		Toast.makeText(AppDagger.getAppDagger().getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}

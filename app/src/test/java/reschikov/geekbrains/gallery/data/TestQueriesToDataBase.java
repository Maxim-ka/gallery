package reschikov.geekbrains.gallery.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.dagger.DaggerTestComponent;
import reschikov.geekbrains.gallery.data.dagger.MyImageDaoTestModule;
import reschikov.geekbrains.gallery.data.dagger.TestComponent;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

@RunWith(MockitoJUnitRunner.class)
public class TestQueriesToDataBase {

	private static final int NUMBER_OF_RECORDS = 5;
	private MyImage myImage;

	@InjectMocks
	Data data;
	
	@BeforeClass
	public static void setupClass(){
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
	}

	public List<MyImage> getFromDataBase(){
		List<MyImage> source = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
			MyImage myImage = new MyImage();
			myImage.setPreview(String.valueOf(i));
			myImage.setUrl(String.valueOf(i));
			myImage.setRowId(i + 1);
			myImage.setId(i + 1);
			source.add(myImage);
		}
		return source;
	}

	private Completable deleteMyImage(MyImage myImage){
		data.getImageList().remove(myImage);
		return Completable.complete();
	}

	@Before
	public void init(){
		TestComponent component = DaggerTestComponent.builder()
			.myImageDaoTestModule(new MyImageDaoTestModule(){
				@Override
				public MyImageDao provideMyImageDao() {
					MyImageDao myImageDao = super.provideMyImageDao();
					Mockito.when(myImageDao.getAll()).thenReturn(Maybe.just(getFromDataBase()));
					Mockito.when(myImageDao.delete(Mockito.any(MyImage.class))).thenReturn(deleteMyImage(myImage));
					return myImageDao;
				}
			})
			.build();
		component.inject(data);	
	}

	@Test
	public void loadFromDatabase(){
		data.loadFromDatabase();
		try {
			Thread.sleep(1_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(NUMBER_OF_RECORDS, data.getImageList().size());
		for (int i = 0; i < data.getImageList().size(); i++) {
			Assert.assertTrue(data.getImageList().get(i).isFavorite());
		}
	}

	@Test
	public void removeFromDatabase(){
		data.loadFromDatabase();
		try {
			Thread.sleep(2_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int index = new Random().nextInt(NUMBER_OF_RECORDS);
		myImage = data.getImageList().get(index);
		System.out.println(data.getImageList().size());
		System.out.println(index);
		data.removeFromDatabase(myImage);
		try {
			Thread.sleep(3_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(NUMBER_OF_RECORDS - 1, data.getImageList().size());
	}
}

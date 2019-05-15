package reschikov.geekbrains.gallery.data;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.dagger.MyImageDaoTestModule;
import reschikov.geekbrains.gallery.data.dagger.TestComponent;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

@RunWith(MockitoJUnitRunner.class)
public class TestQueriesToDataBase {

	MyImageDao myImageDao;

	Data data;
	@Spy
	List<MyImage> imageList;

	@BeforeClass
	public static void setupClass(){
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
	}

	public List<MyImage> getDataBase(){
		List<MyImage> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			MyImage myImage = new MyImage();
			myImage.setPreview(String.valueOf(i));
			myImage.setUrl(String.valueOf(i));
			myImage.setRowId(i + 1);
			myImage.setId(i + 1);
			list.add(myImage);
		}
		return list;
	}

	@Test
	public void createData(){
		TestComponent component = DaggerTestComponent.builder()
			.myImageDaoTestModule(new MyImageDaoTestModule(){
				@Override
				public MyImageDao provideMyImageDao() {
					MyImageDao myImageDao = super.provideMyImageDao();
					Mockito.when(myImageDao.getAll()).thenReturn(Maybe.just(getDataBase()));
					return myImageDao;
				}
			})
			.build();
		component.inject(data);
		imageList = data.getImageList();
		Assert.assertEquals(5, imageList.size());
		for (int i = 0; i < imageList.size(); i++) {
			Assert.assertTrue(imageList.get(i).isFavorite());
		}
	}

//	@Test
//	public void loadFromDatabase(){
//
//		data.loadFromDatabase();
//
//	}
}

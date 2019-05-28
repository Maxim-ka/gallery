package reschikov.geekbrains.gallery.dagger;
import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

@Module
public class MyImageDaoTestModule {
	@Singleton
	@Provides
	public MyImageDao provideMyImageDao(){
		return Mockito.mock(MyImageDao.class);
	}
}

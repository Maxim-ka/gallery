package reschikov.geekbrains.gallery.dagger;

import android.content.Context;
import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.database.DataBase;
import reschikov.geekbrains.gallery.data.database.MyImageDao;

@Module
class DataBaseModule {

	private final Context context;

	DataBaseModule(Context context) {
		this.context = context;
	}

	@Singleton
	@Provides
	DataBase provideDataBase(){
		return Room.databaseBuilder(context, DataBase.class, "MyImage").build();
	}

	@Singleton
	@Provides
	MyImageDao provideMyImageDao(DataBase dataBase){
		return dataBase.getMyImageDao();
	}

	@Singleton
	@Provides
	public Data getData(){
		Data data = new Data();
		AppDagger.getAppDagger().getAppComponent().inject(data);
		data.loadFromDatabase();
		return data;
	}
}

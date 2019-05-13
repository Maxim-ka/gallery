package reschikov.geekbrains.gallery.data.dagger;

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

	private Context context;

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
		return new Data();
	}
}

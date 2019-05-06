package reschikov.geekbrains.gallery.data.database;

import android.app.Application;
import androidx.room.Room;

public class AppDataBase extends Application {

	private static DataBase database;

	public static DataBase getDatabase() {
		return database;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		database = Room.databaseBuilder(getApplicationContext(), DataBase.class, "MyImage").build();
	}
}

package reschikov.geekbrains.gallery.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import reschikov.geekbrains.gallery.data.MyImage;

@Database(entities = {MyImage.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
	public abstract MyImageDao getMyImageDao();
}

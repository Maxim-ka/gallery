package reschikov.geekbrains.gallery.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import reschikov.geekbrains.gallery.data.MyImage;

@Dao
public interface MyImageDao {

	@Query("SELECT * FROM table_myImage")
	Maybe<List<MyImage>> getAll();

	@Insert
	Completable insert(List<MyImage> myImages);

	@Delete
	Completable delete(MyImage myImages);

	@Delete
	Completable delete(List<MyImage> myImages);
}

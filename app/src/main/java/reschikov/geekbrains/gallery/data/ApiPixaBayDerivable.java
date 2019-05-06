package reschikov.geekbrains.gallery.data;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiPixaBayDerivable {
	@GET("/api/")
	Single<Reply> getListImage(@Query("key") String key, @Query("lang") String lang, @Query("per_page") int number);
}

package reschikov.geekbrains.gallery.data.net;

import io.reactivex.Single;
import reschikov.geekbrains.gallery.data.Reply;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiPixaBayDerivable {
	@GET("/api/")
	Single<Response<Reply>> getListImage(@Query("key") String key, @Query("q") String q,
        @Query("lang") String lang, @Query("image_type") String type,
        @Query("orientation") String orientation, @Query("category") String category,
        @Query("per_page") int number);
}

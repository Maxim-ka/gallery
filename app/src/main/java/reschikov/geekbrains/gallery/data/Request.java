package reschikov.geekbrains.gallery.data;

import android.util.Log;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Request {

	interface ApiPixabay {
		@GET("/api/")
		Single<Reply> getListImage(@Query("key") String key, @Query("lang") String lang, @Query("per_page") int number);
	}

	private static final String KEY_PIXABAY = "12328769-7402b39b42648d41ed33f1053";
	private ApiPixabay derivable;

	public Request() {
		derivable = new Retrofit.Builder()
			.baseUrl("https://pixabay.com")
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ApiPixabay.class);
	}

	public Single<Reply> toRequest(){
		String lang = Locale.getDefault().getLanguage();
		return (derivable.getListImage(KEY_PIXABAY, lang,5)).subscribeOn(Schedulers.io());
	}
}

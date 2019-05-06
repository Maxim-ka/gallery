package reschikov.geekbrains.gallery.data;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {

	private static final String KEY_PIXABAY = "12328769-7402b39b42648d41ed33f1053";
	private ApiPixaBayDerivable derivable;

	public Request() {
		derivable = new Retrofit.Builder()
			.baseUrl("https://pixabay.com")
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ApiPixaBayDerivable.class);
	}

	public Single<Reply> toRequest(){
		String lang = Locale.getDefault().getLanguage();
		return (derivable.getListImage(KEY_PIXABAY, lang,10)).subscribeOn(Schedulers.io());
	}
}

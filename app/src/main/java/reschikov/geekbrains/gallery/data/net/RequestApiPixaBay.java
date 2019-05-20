package reschikov.geekbrains.gallery.data.net;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.Reply;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestApiPixaBay {

	private static final String KEY_PIXABAY = "12328769-7402b39b42648d41ed33f1053";

	private final ApiPixaBayDerivable derivable;
	private String q;
	private String type;
	private String orientation;
	private String category;
	private int perPage;

	public void setQ(String q) {
		this.q = q;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public RequestApiPixaBay() {
		Retrofit retrofit = AppDagger.getAppDagger().getAppComponent().getRetrofit();
		derivable = retrofit.create(ApiPixaBayDerivable.class);
	}

	public Single<Response<Reply>> toRequest(){
		String lang = Locale.getDefault().getLanguage();
		return (derivable.getListImage(KEY_PIXABAY, q, lang, type, orientation, category, perPage)).subscribeOn(Schedulers.io());
	}
}

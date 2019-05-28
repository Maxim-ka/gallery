package reschikov.geekbrains.gallery.data.net;

import java.util.Locale;
import javax.inject.Inject;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.data.Reply;
import retrofit2.Response;

public class ServerRequest {

	private String q;
	private String type;
	private String orientation;
	private String category;
	private int perPage;
	@Inject ApiPixaBayDerivable derivable;
	@Inject String keyPixabay;

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

	public Single<Response<Reply>> toRequest(){
		String lang = Locale.getDefault().getLanguage();
		return (derivable.getListImage(keyPixabay, q, lang, type, orientation, category, perPage)).subscribeOn(Schedulers.io());
	}
}

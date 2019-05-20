package reschikov.geekbrains.gallery.presenter;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.Reply;
import reschikov.geekbrains.gallery.data.SelectionParameter;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.RequestApiPixaBay;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.Displayed;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.Selectable;
import retrofit2.Response;

@InjectViewState
public class FieldsPresenter extends MvpPresenter<Displayed> implements Selectable {

	@Named("types")
	@Inject
	SelectionParameter[] types;
	@Named("orientations")
	@Inject
	SelectionParameter[] orientations;
	@Named("categories")
	@Inject
	SelectionParameter[] categories;
	@Inject
	RequestApiPixaBay request;

	private Data data;
	private String queryTypes;
	private String queryOrientations;
	private String queryCategories;
	private int number = Rule.DEFAULT_PER_PAGE;

	public SelectionParameter[] getTypes() {
		return types;
	}

	public SelectionParameter[] getOrientations() {
		return orientations;
	}

	public SelectionParameter[] getCategories() {
		return categories;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public FieldsPresenter() {
		AppDagger.getAppDagger().createRequestComponent().inject(this);
		data = AppDagger.getAppDagger().getAppComponent().getData();
	}

	public void sendRequest(String what){
		data.reload();
		if (what != null && !what.equals("")) request.setQ(what);
		if (queryTypes != null) request.setType(queryTypes);
		if (queryOrientations != null) request.setOrientation(queryOrientations);
		if (queryCategories != null) request.setCategory(queryCategories);
		request.setPerPage(number);
		Single<Response<Reply>> replySingle = request.toRequest();
		Disposable disposable = replySingle.observeOn(AndroidSchedulers.mainThread())
			.subscribe(response -> {
					if (response.isSuccessful() && response.body() != null){
						data.setNumberPage(number);
						data.downloadServer(response.body().getHits());
						getViewState().showReply();
					} else if (response.errorBody() != null){
						getViewState().showServerResponse(response.errorBody().string());
					}
				},
				e -> getViewState().showServerResponse(e.getMessage()));
	}

	private String getChoice(SelectionParameter[] parameters){
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].isSelected()){
				stringBuilder.append(parameters[i].getParameter()).append(", ");
			}
		}
		if (stringBuilder.length() == 0) return null;
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(", "));
		return stringBuilder.toString();
	}

	@Override
	public void select(int position, boolean isChecked, String title) {
		switch (title){
			case "image Types":
				types[position].setSelected(isChecked);
				break;
			case "image Orientations":
				orientations[position].setSelected(isChecked);
				break;
			case "image Categories":
				categories[position].setSelected(isChecked);
				break;
		}
	}

	@Override
	public void toFinish(String title) {
		switch (title){
			case "image Types":
				queryTypes = getChoice(types);
				getViewState().showParameters(title, queryTypes);
				break;
			case "image Orientations":
				queryOrientations = getChoice(orientations);
				getViewState().showParameters(title, queryOrientations);
				break;
			case "image Categories":
				queryCategories = getChoice(categories);
				getViewState().showParameters(title, queryCategories);
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppDagger.getAppDagger().clearRequestComponent();
	}
}

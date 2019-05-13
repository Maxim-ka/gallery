package reschikov.geekbrains.gallery.presenter;
import android.util.Log;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.data.Reply;
import reschikov.geekbrains.gallery.data.SelectionParameter;
import reschikov.geekbrains.gallery.data.dagger.AppDagger;
import reschikov.geekbrains.gallery.data.net.RequestApiPixaBay;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.Displayed;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.Selectable;

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

	public SelectionParameter[] getTypes() {
		return types;
	}

	public SelectionParameter[] getOrientations() {
		return orientations;
	}

	public SelectionParameter[] getCategories() {
		return categories;
	}

	public FieldsPresenter() {
		AppDagger.getAppDagger().createRequestComponent().inject(this);
		data = AppDagger.getAppDagger().getAppComponent().getData();
	}

	public void sendRequest(String what){
		if (what != null && !what.equals("")) request.setQ(what);
		if (queryTypes != null) request.setType(queryTypes);
		if (queryOrientations != null) request.setOrientation(queryOrientations);
		if (queryCategories != null) request.setCategory(queryCategories);
		Single<Reply> response = request.toRequest();
		Disposable disposable = response.observeOn(AndroidSchedulers.mainThread())
			.subscribe(reply -> {
					data.downloadServer(reply.getHits());
					Log.i("onFirstViewAttach: ", "загрузка с интернета");
					getViewState().showReply();
					AppDagger.getAppDagger().clearRequestComponent();
					data = null;
				},
				e -> Log.e("ServerError", e.getMessage()));
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
}

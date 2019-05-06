package reschikov.geekbrains.gallery.presenter;

import reschikov.geekbrains.gallery.data.SelectionParameter;

public class FieldsPresenter {

	private SelectionParameter[] types;
	private SelectionParameter[] orientations;
	private SelectionParameter[] categories;

	public void setTypes(String[] types) {
		if (this.types == null) this.types = createParameters(types);
	}

	public void setOrientations(String[] orientations) {
		if (this.orientations == null) this.orientations = createParameters(orientations);
	}

	public void setCategories(String[] categories) {
		if (this.categories == null) this.categories = createParameters(categories);
	}

	public SelectionParameter[] getTypes() {
		return types;
	}

	public SelectionParameter[] getOrientations() {
		return orientations;
	}

	public SelectionParameter[] getCategories() {
		return categories;
	}

	private SelectionParameter[] createParameters(String[] strings){
		SelectionParameter[] parameters = new SelectionParameter[strings.length];
		for (int i = 0; i < strings.length; i++) {
			parameters[i] = new SelectionParameter(strings[i]);
		}
		return parameters;
	}

}

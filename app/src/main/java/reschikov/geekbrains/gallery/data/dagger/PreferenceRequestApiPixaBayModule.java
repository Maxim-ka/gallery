package reschikov.geekbrains.gallery.data.dagger;

import android.content.Context;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.SelectionParameter;

@Module
class PreferenceRequestApiPixaBayModule {

	private final String[] types;
	private final String[] orientations;
	private final String[] categories;

	PreferenceRequestApiPixaBayModule(Context context) {
		types = context.getResources().getStringArray(R.array.image_type);
		orientations = context.getResources().getStringArray(R.array.orientation);
		categories = context.getResources().getStringArray(R.array.category);
	}

	@Named("types")
	@RequestScope
	@Provides
	SelectionParameter[] createType(){
		return createParameters(types);
	}

	@Named("orientations")
	@RequestScope
	@Provides
	SelectionParameter[] createOrientations(){
		return createParameters(orientations);
	}

	@Named("categories")
	@RequestScope
	@Provides
	SelectionParameter[] createCategories(){
		return createParameters(categories);
	}

	private SelectionParameter[] createParameters(String[] strings){
		SelectionParameter[] parameters = new SelectionParameter[strings.length];
		for (int i = 0; i < strings.length; i++) {
			parameters[i] = new SelectionParameter(strings[i]);
		}
		return parameters;
	}
}

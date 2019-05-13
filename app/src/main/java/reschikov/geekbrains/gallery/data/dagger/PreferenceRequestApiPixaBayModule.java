package reschikov.geekbrains.gallery.data.dagger;

import android.content.Context;
import android.content.SharedPreferences;

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
	private SharedPreferences sharedPreferences;

	PreferenceRequestApiPixaBayModule(Context context) {
		types = context.getResources().getStringArray(R.array.image_type);
		orientations = context.getResources().getStringArray(R.array.orientation);
		categories = context.getResources().getStringArray(R.array.category);
		sharedPreferences = context.getSharedPreferences("RequestApiPixaBay", Context.MODE_PRIVATE);
	}

	@RequestScope
	@Provides
	SharedPreferences getSharedPreferences(){
		return sharedPreferences;
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

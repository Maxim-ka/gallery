package reschikov.geekbrains.gallery.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.SelectionParameter;

@Module
class PreferenceRequestApiPixaBayModule {

	private final String[] types;
	private final String[] orientations;
	private final String[] categories;
	private final Context context;

	PreferenceRequestApiPixaBayModule(Context context) {
		this.context = context;
		types = context.getResources().getStringArray(R.array.image_type);
		orientations = context.getResources().getStringArray(R.array.orientation);
		categories = context.getResources().getStringArray(R.array.category);
	}

	@Named(Rule.TYPES)
	@RequestScope
	@Provides
	SelectionParameter[] createType(){
		return createParameters(types, Rule.TYPES);
	}

	@Named(Rule.ORIENTATIONS)
	@RequestScope
	@Provides
	SelectionParameter[] createOrientations(){
		return createParameters(orientations, Rule.ORIENTATIONS);
	}

	@Named(Rule.CATEGORIES)
	@RequestScope
	@Provides
	SelectionParameter[] createCategories(){
		return createParameters(categories, Rule.CATEGORIES);
	}

	private SelectionParameter[] createParameters(String[] strings, String key){
		SharedPreferences preferences = context.getSharedPreferences(Rule.REQUEST_PARAMETERS, Context.MODE_PRIVATE);
		Set<String> set = preferences.getStringSet(key, null);
		SelectionParameter[] parameters = new SelectionParameter[strings.length];
		for (int i = 0; i < strings.length; i++) {
			parameters[i] = new SelectionParameter(strings[i]);
			if (set != null && !set.isEmpty()){
				if (set.contains(strings[i])){
					parameters[i].setSelected(true);
				}
			}
		}
		return parameters;
	}
}

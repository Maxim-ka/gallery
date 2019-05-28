package reschikov.geekbrains.gallery.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.data.net.ApiPixaBayDerivable;
import reschikov.geekbrains.gallery.data.net.ServerRequest;
import retrofit2.Retrofit;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

@Module
class RequestModule {

	private final Context context;

	RequestModule(Context context) {
		this.context = context;
	}

	@RequestScope
	@Provides
	String provideAccessKey(){
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(Rule.KEY_PIXABAY, context.getResources().getString(R.string.key_pixabay));
	}

	@RequestScope
	@Provides
	ApiPixaBayDerivable provideApiPixaBayDerivable(){
		Retrofit retrofit = AppDagger.getAppDagger().getAppComponent().getRetrofit();
		return retrofit.create(ApiPixaBayDerivable.class);
	}


	@RequestScope
	@Provides
	ServerRequest provideRequest(){
		ServerRequest serverRequest = new ServerRequest();
		AppDagger.getAppDagger().createRequestComponent().inject(serverRequest);
		return serverRequest;
	}
}

package reschikov.geekbrains.gallery.dagger;

import android.app.Application;

public class AppDagger extends Application {

	private static AppDagger appDagger;
	private AppComponent appComponent;
	private RequestComponent requestComponent;

	public static AppDagger getAppDagger() {
		return appDagger;
	}

	public AppComponent getAppComponent() {
		return appComponent;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		appDagger = this;
		appComponent = generateAppComponent();
	}

	public RequestComponent createRequestComponent(){
		if (requestComponent == null)
			requestComponent = appComponent.plusRequestComponent(new PreferenceRequestApiPixaBayModule(getApplicationContext()), new RequestModule(getApplicationContext()));
		return requestComponent;
	}

	public void clearRequestComponent(){
		requestComponent = null;
	}

	private AppComponent generateAppComponent(){
		return DaggerAppComponent
			.builder()
			.dataBaseModule(new DataBaseModule(getApplicationContext()))
			.imageUploaderModule(new ImageUploaderModule(getApplicationContext()))
			.build();
	}
}

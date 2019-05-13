package reschikov.geekbrains.gallery.data.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class RetrofitModule {

	private static final String BASE_URL = "https://pixabay.com";

	@Singleton
	@Provides
	Retrofit getRetrofit(){
		return new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	}
}

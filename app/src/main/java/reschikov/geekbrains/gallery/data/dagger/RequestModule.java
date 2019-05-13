package reschikov.geekbrains.gallery.data.dagger;

import dagger.Module;
import dagger.Provides;
import reschikov.geekbrains.gallery.data.net.RequestApiPixaBay;

@Module
class RequestModule {

	@RequestScope
	@Provides
	RequestApiPixaBay provideRequest(){
		return new RequestApiPixaBay();
	}
}

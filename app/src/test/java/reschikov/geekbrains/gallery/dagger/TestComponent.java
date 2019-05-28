package reschikov.geekbrains.gallery.dagger;

import javax.inject.Singleton;

import dagger.Component;
import reschikov.geekbrains.gallery.data.Data;

@Singleton
@Component(modules = {MyImageDaoTestModule.class})
public interface TestComponent {
	void inject(Data data);
}

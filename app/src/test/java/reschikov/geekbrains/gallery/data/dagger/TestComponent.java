package reschikov.geekbrains.gallery.data.dagger;

import javax.inject.Singleton;

import dagger.Component;
import reschikov.geekbrains.gallery.data.Data;

@Singleton
@Component(modules = {MyImageDaoTestModule.class})
public interface TestComponent {
	void inject(Data data);
}

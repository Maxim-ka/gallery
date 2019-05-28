package reschikov.geekbrains.gallery.dagger;

import javax.inject.Singleton;

import dagger.Component;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.presenter.GalleryPresenter;
import reschikov.geekbrains.gallery.presenter.MainPresenter;
import reschikov.geekbrains.gallery.presenter.PagerPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.FieldsFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.MyAdapterRecycleView;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataBaseModule.class, RetrofitModule.class, ImageUploaderModule.class})
public interface AppComponent {
	Retrofit getRetrofit();
	Data getData();
	RequestComponent plusRequestComponent(PreferenceRequestApiPixaBayModule preferenceRequestApiPixaBayModule, RequestModule requestModule);
	void inject(Data data);
	void inject(MainPresenter mainPresenter);
	void inject(GalleryPresenter galleryPresenter);
	void inject(PagerPresenter pagerPresenter);
	void inject(MyAdapterRecycleView.MyViewHolder myViewHolder);
	void inject(FieldsFragment fieldsFragment);
}

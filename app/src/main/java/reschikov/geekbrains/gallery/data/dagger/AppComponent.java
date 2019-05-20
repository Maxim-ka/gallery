package reschikov.geekbrains.gallery.data.dagger;

import javax.inject.Singleton;

import dagger.Component;
import reschikov.geekbrains.gallery.data.Data;
import reschikov.geekbrains.gallery.presenter.GalleryPresenter;
import reschikov.geekbrains.gallery.presenter.PagerPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.ItemFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery.MyAdapterRecycleView;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataBaseModule.class, RetrofitModule.class, ImageUploaderModule.class})
public interface AppComponent {
	Retrofit getRetrofit();
	Data getData();
	RequestComponent plusRequestComponent(PreferenceRequestApiPixaBayModule preferenceRequestApiPixaBayModule, RequestModule requestModule);
	void inject(Data data);
	void inject(GalleryPresenter galleryPresenter);
	void inject(ItemFragment fragment);
	void inject(MyAdapterRecycleView.MyViewHolder myViewHolder);
	void inject(PagerPresenter pagerPresenter);
}

package reschikov.geekbrains.gallery.dagger;

import dagger.Subcomponent;
import reschikov.geekbrains.gallery.data.net.ServerRequest;
import reschikov.geekbrains.gallery.presenter.FieldsPresenter;

@RequestScope
@Subcomponent(modules = {PreferenceRequestApiPixaBayModule.class, RequestModule.class,})
public interface RequestComponent {
	void inject(FieldsPresenter fieldsPresenter);
	void inject(ServerRequest serverRequest);
}

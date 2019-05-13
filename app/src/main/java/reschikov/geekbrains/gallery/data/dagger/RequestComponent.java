package reschikov.geekbrains.gallery.data.dagger;

import dagger.Subcomponent;
import reschikov.geekbrains.gallery.presenter.FieldsPresenter;

@RequestScope
@Subcomponent(modules = {PreferenceRequestApiPixaBayModule.class, RequestModule.class,})
public interface RequestComponent {
	void inject(FieldsPresenter fieldsPresenter);
}

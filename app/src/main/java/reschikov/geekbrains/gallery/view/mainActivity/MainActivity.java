package reschikov.geekbrains.gallery.view.mainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.dagger.AppDagger;
import reschikov.geekbrains.gallery.presenter.MainPresenter;
import reschikov.geekbrains.gallery.data.MyViewModelSpanCount;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.FieldsFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.ViewPagerFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.NotificationsFragment;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.view.setting.SettingActivity;

import android.view.MenuItem;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends MvpAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Changing, Shown{

	private int theme;
	private MyViewModelSpanCount modelSpanCount;
	private Unbinder unbinder;
    @BindBool(R.bool.is_portrait) boolean isPortrait;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @InjectPresenter MainPresenter mainPresenter;

	@ProvidePresenter MainPresenter providePresenter(){
		MainPresenter mainPresenter = new MainPresenter();
		AppDagger.getAppDagger().getAppComponent().inject(mainPresenter);
		mainPresenter.init();
		return mainPresenter;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = getSelectedTheme();
        if (this.theme != theme) {
            this.theme = theme;
            setTheme(theme);
        }
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        modelSpanCount =  new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyViewModelSpanCount.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

		Intent intent = getIntent();
	    if (intent == null && savedInstanceState == null) {
		    loadFragment(new FieldsFragment(), Rule.TAG_SEARCH);
	    }
		if (intent != null){
			int item = intent.getIntExtra(Rule.MENU_ITEM, R.id.navigation_search);
			toggleFragments(item);
			intent = null;
		}

        if (!isPortrait && modelSpanCount.getLiveData().getValue() != null &&
                modelSpanCount.getLiveData().getValue() != 2){
           modelSpanCount.setValueLiveData(2);
        }
    }

    private int getSelectedTheme(){
        SharedPreferences sp = getSharedPreferences(Rule.NAME_THEME, MODE_PRIVATE);
        return sp.getInt(Rule.KEY_THEME, R.style.AppTheme0);
    }

    private void loadFragment(Fragment newFragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_master, newFragment, tag)
                .commit();
    }

    private void changeFragment(Fragment newFragment, String tag) {
        getSupportFragmentManager().beginTransaction()
//            .setCustomAnimations(R.animator.animator_enter, R.animator.animator_exit)
            .replace(R.id.frame_master, newFragment, tag)
            .commit();
    }

    private void checkItemMenu(){
        String tag = getTag();
        if (tag == null) return;
        switch (tag){
            case Rule.TAG_SEARCH:
                if (bottomNavigationView.getSelectedItemId() != R.id.navigation_search) bottomNavigationView.getMenu().findItem(R.id.navigation_search).setChecked(true);
                break;
            case "ViewPager":
                if (modelSpanCount.getLiveData().getValue() == null) return;
                switch (modelSpanCount.getLiveData().getValue()){
                    case 1:
                        if (bottomNavigationView.getSelectedItemId() != R.id.navigation_gallery_list) bottomNavigationView.setSelectedItemId(R.id.navigation_gallery_list);
                        return;
                    case 2:
                        if (bottomNavigationView.getSelectedItemId() != R.id.navigation_gallery_grid) bottomNavigationView.setSelectedItemId(R.id.navigation_gallery_grid);
                        return;
                }
                break;
            case "Notifications":
                if (bottomNavigationView.getSelectedItemId() != R.id.navigation_notifications) bottomNavigationView.getMenu().findItem(R.id.navigation_notifications).setChecked(true);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (theme != getSelectedTheme()) recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkItemMenu();
    }

    private boolean loadGallery(String tag, int spanCount){
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_gallery_list && spanCount == 1) return false;
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_gallery_grid && spanCount == 2) return false;
        if (!isPortrait && spanCount != 2) return false;
        modelSpanCount.setValueLiveData(spanCount);
        if (!"ViewPager".equals(tag)){
            changeFragment(new ViewPagerFragment(), "ViewPager");
        }
        return true;
    }

    private String getTag(){
	    if (!getSupportFragmentManager().getFragments().isEmpty()){
		    Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
		    return currentFragment.getTag();
	    }
	    return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		String tag = getTag();
        switch (item.getItemId()) {
            case R.id.navigation_search:
	            changeFragment(new FieldsFragment(), Rule.TAG_SEARCH);
	            return true;
            case R.id.navigation_gallery_list:
                return loadGallery(tag, 1);
            case R.id.navigation_gallery_grid:
                return loadGallery(tag, 2);
            case R.id.navigation_notifications:
	            changeFragment(new NotificationsFragment(), "Notifications");
	            return true;
            case R.id.navigation_setting:
                startActivity(new Intent(getBaseContext(), SettingActivity.class));
                return true;
        }
        return false;
    }

	@Override
	public void toggleFragments(int id) {
		bottomNavigationView.setSelectedItemId(id);
	}

	@Override
	public void indicate(String message) {
		Snackbar.make(bottomNavigationView, message, Snackbar.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}

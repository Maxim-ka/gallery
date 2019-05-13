package reschikov.geekbrains.gallery.view.mainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import reschikov.geekbrains.gallery.view.colorSelectionActivity.ColorThemeSelection;
import reschikov.geekbrains.gallery.data.MyViewModelSpanCount;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.inputFieldsFragment.FieldsFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.ViewPagerFragment;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.NotificationsFragment;
import reschikov.geekbrains.gallery.R;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Changing, Counted{

    private int theme;
    private int counter;
    private View viewBadge;
    private BottomNavigationItemView notifications;
    private TextView badge;
    private BottomNavigationView bottomNavigationView;
    private boolean isPortrait;
    private MyViewModelSpanCount modelSpanCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = getSelectedTheme();
        if (this.theme != theme) {
            this.theme = theme;
            setTheme(theme);
        }
        setContentView(R.layout.activity_main);
        isPortrait = getResources().getBoolean(R.bool.is_portrait);
        modelSpanCount =  new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyViewModelSpanCount.class);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        notifications = menuView.findViewById(R.id.navigation_notifications);
        viewBadge = LayoutInflater.from(this).inflate(R.layout.badge, menuView, false);
        badge = viewBadge.findViewById(R.id.text_badge);

        if (savedInstanceState == null) {
            loadFragment(new FieldsFragment(), "Tag_Home");
        }

        boolean isPortrait = getResources().getBoolean(R.bool.is_portrait);
        if (!isPortrait && modelSpanCount.getLiveData().getValue() != null &&
                modelSpanCount.getLiveData().getValue() != 2){
           modelSpanCount.setValueLiveData(2);
        }
    }

    private int getSelectedTheme(){
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        return sp.getInt("theme", R.style.AppTheme0);
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

    private void setBadge(){
        if (badge != null){
            if (counter > 9) badge.setText("9+");
            else badge.setText(String.valueOf(counter));
        }
    }

    private void checkItemMenu(){
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        String tag = currentFragment.getTag();
        if (tag == null) return;
        switch (tag){
            case "Tag_Home":
                if (bottomNavigationView.getSelectedItemId() != R.id.navigation_home) bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            counter = savedInstanceState.getInt("counter");
            if (counter != 0){
                notifications.addView(viewBadge);
                setBadge();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
    }

    public boolean loadGallery(String tag, int spanCount){
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_gallery_list && spanCount == 1) return false;
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_gallery_grid && spanCount == 2) return false;
        if (!isPortrait && spanCount != 2) return false;
        modelSpanCount.setValueLiveData(spanCount);
        if (!"ViewPager".equals(tag)){
            changeFragment(new ViewPagerFragment(), "ViewPager");
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        String tag = currentFragment.getTag();
        if (tag == null) return false;
        switch (item.getItemId()) {
            case R.id.navigation_home:
	            changeFragment(new FieldsFragment(),"Tag_Home");
	            return true;
            case R.id.navigation_gallery_list:
                return loadGallery(tag, 1);
            case R.id.navigation_gallery_grid:
                return loadGallery(tag, 2);
            case R.id.navigation_notifications:
	            changeFragment(new NotificationsFragment(), "Notifications");
	            return true;
            case R.id.navigation_setting:
                startActivity(new Intent(getBaseContext(), ColorThemeSelection.class));
                return true;
        }
        return false;
    }


    @Override
    public void count() {
        if (counter == 0) notifications.addView(viewBadge);
        counter++;
        if (counter <= 10) setBadge();
    }

    @Override
    public void reset() {
        counter = 0;
        badge.setText(null);
        notifications.removeView(viewBadge);
    }

	@Override
	public void toggleFragments(int id) {
		bottomNavigationView.setSelectedItemId(id);
	}
}

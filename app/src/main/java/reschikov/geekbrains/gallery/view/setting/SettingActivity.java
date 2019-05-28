package reschikov.geekbrains.gallery.view.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;
import reschikov.geekbrains.gallery.view.mainActivity.MainActivity;
import reschikov.geekbrains.gallery.view.setting.fragments.SettingFragment;

public class SettingActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

	@BindView(R.id.toolbar)	Toolbar toolbar;
	@BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
	private Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences(Rule.NAME_THEME, MODE_PRIVATE);
		setTheme(sp.getInt(Rule.KEY_THEME, R.style.AppTheme0));
		setContentView(R.layout.activity_setting);
		unbinder = ButterKnife.bind(this);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);
		bottomNavigationView.getMenu().findItem(R.id.navigation_setting).setChecked(true);

		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setTitle("preference");
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.frame_master, new SettingFragment(), "Setting")
			.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			returnMainActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_search:
				launchMainActivity(R.id.navigation_search);
				return true;
			case R.id.navigation_gallery_list:
				launchMainActivity(R.id.navigation_gallery_list);
				return true;
			case R.id.navigation_gallery_grid:
				launchMainActivity(R.id.navigation_gallery_grid);
				return true;
			case R.id.navigation_notifications:
				launchMainActivity(R.id.navigation_notifications);
				return true;
		}
		return false;
	}

	private void launchMainActivity(int menuItem){
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Rule.MENU_ITEM, menuItem);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		returnMainActivity();
	}

	private void returnMainActivity(){
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}

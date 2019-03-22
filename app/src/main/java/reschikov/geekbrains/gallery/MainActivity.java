package reschikov.geekbrains.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.transition.ArcMotion;
import androidx.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Counted, Switchable{

    private int counter;
    private View viewBadge;
    private BottomNavigationItemView notifications;
    private TextView badge;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        notifications = menuView.findViewById(R.id.navigation_notifications);
        viewBadge = LayoutInflater.from(this).inflate(R.layout.badge, menuView, false);
        badge = viewBadge.findViewById(R.id.text_badge);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_master, new HomeFragment(), "Home")
                    .commit();
        }
    }

    private void changeFragment(Fragment newFragment, View view, String tag) {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(1_000);
        changeBounds.setPathMotion(new ArcMotion());
        changeBounds.setInterpolator(new AccelerateDecelerateInterpolator());
        changeBounds.setResizeClip(true);

        newFragment.setSharedElementEnterTransition(changeBounds);


        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.animator.animator_enter, R.animator.animator_exit)
            .replace(R.id.frame_master, newFragment, tag)
            .addSharedElement(view, ViewCompat.getTransitionName(view))
            .commit();
    }

    private void setBadge(){
        if (badge != null){
            if (counter > 9) badge.setText("9+");
            else badge.setText(String.valueOf(counter));
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        String tag = currentFragment.getTag();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if ("Notifications".equals(tag)){
                    NotificationsFragment notificationsFragment = (NotificationsFragment) currentFragment;
                    changeFragment(new HomeFragment(), notificationsFragment.getImage(),"Home");
                    return true;
                }
                return false;
            case R.id.navigation_notifications:
                if ("Home".equals(tag)){
                    HomeFragment homeFragment = (HomeFragment) currentFragment;
                    changeFragment(new NotificationsFragment(), homeFragment.getImage(), "Notifications");
                    return true;
                }
                return false;
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

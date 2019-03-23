package reschikov.geekbrains.gallery.mainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.transition.ArcMotion;
import androidx.transition.ChangeBounds;
import reschikov.geekbrains.gallery.mainActivity.fragments.gallery.GalleryFragment;
import reschikov.geekbrains.gallery.mainActivity.fragments.HomeFragment;
import reschikov.geekbrains.gallery.mainActivity.fragments.NotificationsFragment;
import reschikov.geekbrains.gallery.R;

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
            loadFragment(new HomeFragment(), "Home");
        }
    }

    private void loadFragment(Fragment newFragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_master, newFragment, tag)
                .commit();
    }

    private void changeFragment(Fragment newFragment, View view, String tag) {
        String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName == null) return;

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(1_000);
        changeBounds.setPathMotion(new ArcMotion());
        changeBounds.setInterpolator(new AccelerateDecelerateInterpolator());
        changeBounds.setResizeClip(true);

        newFragment.setSharedElementEnterTransition(changeBounds);

        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.animator.animator_enter, R.animator.animator_exit)
            .replace(R.id.frame_master, newFragment, tag)
            .addSharedElement(view, transitionName)
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
        if (tag == null) return false;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                switch (tag){
                    case "Notifications":
                        NotificationsFragment notificationsFragment = (NotificationsFragment) currentFragment;
                        changeFragment(new HomeFragment(), notificationsFragment.getImage(),"Home");
                        return true;
                    case "Gallery":
                        loadFragment(new HomeFragment(), "Home");
                        return true;
                    default:
                        return false;
                }
            case R.id.navigation_gallery:
                loadFragment(new GalleryFragment(), "Gallery");
                return true;
            case R.id.navigation_notifications:
                switch (tag){
                    case "Home":
                        HomeFragment homeFragment = (HomeFragment) currentFragment;
                        changeFragment(new NotificationsFragment(), homeFragment.getImage(), "Notifications");
                        return true;
                    case "Gallery":
                        loadFragment(new NotificationsFragment(), "Notifications");
                        return true;
                    default:
                        return false;
                }
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

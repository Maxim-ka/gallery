package reschikov.geekbrains.gallery.colorSelectionActivity;

import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.transition.ArcMotion;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionManager;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import reschikov.geekbrains.gallery.R;

public class ColorThemeSelection extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.but_theme_0) MaterialButton buttonTheme0;
    @BindView(R.id.but_theme_1) MaterialButton buttonTheme1;
    @BindView(R.id.but_theme_2) MaterialButton buttonTheme2;
    @BindView(R.id.but_theme_3) MaterialButton buttonTheme3;
    @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
    private final int[] ids = new int[]{R.id.but_theme_0, R.id.current, R.id.but_theme_1, R.id.but_theme_2, R.id.but_theme_3};
    private Unbinder unbinder;
    private SharedPreferences sp;
    private ConstraintSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("theme", MODE_PRIVATE);
        setTheme(sp.getInt("theme", R.style.AppTheme0));
        setContentView(R.layout.activity_color_theme_selection);
        unbinder = ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("preference");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        set = new ConstraintSet();
    }

    @OnClick({R.id.but_theme_0, R.id.but_theme_1, R.id.but_theme_2, R.id.but_theme_3})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_theme_0:
                changeTheme(R.style.AppTheme0);
                break;
            case R.id.but_theme_1:
                changeTheme(R.style.AppTheme1);
                break;
            case R.id.but_theme_2:
                changeTheme(R.style.AppTheme2);
                break;
            case R.id.but_theme_3:
                changeTheme(R.style.AppTheme3);
                break;
        }
        moveView(v.getId());
//        recreate();
    }

    private void changeTheme(int theme){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("theme", theme);
        editor.apply();
    }

    private void moveView(int id){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("currentButton", id);
        editor.apply();
        set.clone(constraintLayout);
        TransitionManager.beginDelayedTransition(constraintLayout, createAnimate());
        createNewChainId(id);
        set.createVerticalChain(ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                ids, null, ConstraintSet.CHAIN_SPREAD);
        set.applyTo(constraintLayout);
    }

    private TransitionSet createAnimate(){
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds());
        set.setPathMotion(setWay());
        set.setInterpolator(new AnticipateOvershootInterpolator());
        set.setDuration(3_000);
        return set;
    }

    private ArcMotion setWay(){
        return new ArcMotion(){
            @Override
            public Path getPath(float startX, float startY, float endX, float endY) {
                float halfX = (endY > startY) ? startX / 2 : startX + startX / 2;
                float halfY = (startY + endY) / 2;
                Path path = new Path();
                path.moveTo(startX, startY);
//                path.cubicTo(startX, startY, halfX, halfY, endX, endY);
                path.lineTo(halfX,halfY);
                path.lineTo(endX, endY);
                return path;
            }
        };
    }

    private void createNewChainId(int newId){
        int temp = ids[0];
        ids[0] = newId;
        for (int i = 2; i < ids.length; i++) {
            if (ids[i] == newId) {
                ids[i] = temp;
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

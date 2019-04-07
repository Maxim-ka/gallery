package reschikov.geekbrains.gallery.colorSelectionActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ArcMotion;
import androidx.transition.Explode;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import reschikov.geekbrains.gallery.R;

public class ColorThemeSelection extends AppCompatActivity implements View.OnClickListener{

    private final int[] ids = new int[]{R.id.but_theme_0, R.id.but_theme_1};
    private ConstraintSet set;
    private MaterialButton buttonTheme0;
    private MaterialButton buttonTheme1;
//    private MaterialButton buttonTheme2;
//    private MaterialButton buttonTheme3;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_theme_selection);
        buttonTheme0 = findViewById(R.id.but_theme_0);
        buttonTheme1 = findViewById(R.id.but_theme_1);
//        buttonTheme2 = findViewById(R.id.but_theme_2);
//        buttonTheme3 = findViewById(R.id.but_theme_3);
        buttonTheme0.setOnClickListener(this);
        buttonTheme1.setOnClickListener(this);
//        buttonTheme2.setOnClickListener(this);
//        buttonTheme3.setOnClickListener(this);

        constraintLayout = findViewById(R.id.constraintLayout);

        set = new ConstraintSet();
        set.clone(constraintLayout);

        Log.i("R.id.but_theme_0", String.valueOf(R.id.but_theme_0));
        Log.i("R.id.but_theme_1", String.valueOf(R.id.but_theme_1));
//        Log.i("R.id.but_theme_2", String.valueOf(R.id.but_theme_2));
//        Log.i("R.id.but_theme_3", String.valueOf(R.id.but_theme_3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_theme_0:
                changeTheme(R.id.but_theme_0);
                break;
            case R.id.but_theme_1:
                changeTheme(R.id.but_theme_1);
                break;
//            case R.id.but_theme_2:
//                changeTheme(R.id.but_theme_2);
//                break;
//            case R.id.but_theme_3:
//                changeTheme(R.id.but_theme_3);
//                break;
        }
        v.setEnabled(false);
    }

    private void changeTheme(int id){
        TransitionManager.beginDelayedTransition(constraintLayout);

        int idCurrentBut = findCurrentButton();

        set.clear(idCurrentBut, ConstraintSet.TOP);
        set.clear(idCurrentBut, ConstraintSet.BOTTOM);
        set.clear(id, ConstraintSet.TOP);
        set.clear(id, ConstraintSet.BOTTOM);

        set.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        set.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        set.connect(id, ConstraintSet.BOTTOM, R.id.current, ConstraintSet.TOP);

        int top = identifyTop(idCurrentBut, id);
        Log.i("top", String.valueOf(top));
        set.connect(idCurrentBut, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(idCurrentBut, ConstraintSet.TOP, top, ConstraintSet.BOTTOM);
        set.connect(idCurrentBut, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        int bottom = identifyBottom(idCurrentBut, id);
        Log.i("bottom", String.valueOf(bottom));
        int endSide = (bottom != ConstraintSet.PARENT_ID) ? ConstraintSet.TOP : ConstraintSet.BOTTOM;
        set.connect(idCurrentBut, ConstraintSet.BOTTOM, bottom, endSide);

        set.applyTo(constraintLayout);
    }

    private int identifyBottom(int idOld, int idNew){
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == idOld || ids[i] == idNew) continue;
            if (ids[i] > idNew) return ids[i];
        }
        return ConstraintSet.PARENT_ID;
    }

    private int identifyTop(int idOld, int idNew){
        for (int i = ids.length - 1; i >=0; i--) {
            if (ids[i] == idOld || ids[i] == idNew) continue;
            if (ids[i] < idNew) return ids[i];
        }
        return R.id.current;
    }

    private int findCurrentButton(){
        if (!buttonTheme0.isEnabled()) {
            buttonTheme0.setEnabled(true);
            return R.id.but_theme_0;
        }
//        if (!buttonTheme1.isEnabled()) {
            buttonTheme1.setEnabled(true);
            return R.id.but_theme_1;
//        }
//        if (!buttonTheme2.isEnabled()){
//            buttonTheme2.setEnabled(true);
//            return R.id.but_theme_2;
//        }
//        buttonTheme3.setEnabled(true);
//        return R.id.but_theme_3;
    }
}

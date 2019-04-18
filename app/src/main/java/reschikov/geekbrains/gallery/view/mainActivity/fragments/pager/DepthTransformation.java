package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class DepthTransformation implements ViewPager.PageTransformer {


    @Override
    public void transformPage(@NonNull View page, float position) {

        if (position <= -1 || position >= 1){
            page.setAlpha(0.0f);
            return;
        }
        if (position == 0){
            page.setAlpha(1.0f);
            page.setScaleX(1.0f);
            page.setScaleY(1.0f);
            return;
        }
        if (position > -1 || position < 1){
            page.setTranslationX(-page.getWidth() * position);
            float offset = 1.0f - Math.abs(position);
            page.setAlpha(offset);
            page.setScaleX(offset);
            page.setScaleY(offset);
        }
    }
}

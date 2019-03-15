package reschikov.geekbrains.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_master, new FieldsFragment())
                .commit();
    }
}

package com.techexchange.mobileapps.lab2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    static final String KEY_SCORE = "Score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        if (frag == null) {
            frag = new QuizFragment();
            fm.beginTransaction().add(R.id.fragment_container, frag).commit();
        }

    }

}

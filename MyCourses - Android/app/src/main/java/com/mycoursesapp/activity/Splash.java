package com.mycoursesapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mycoursesapp.R;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getSharedPreferences("User", MODE_PRIVATE).getBoolean("login", false)){
                    startActivity(new Intent(Splash.this, Home.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }

            }
        },SPLASH_TIME_OUT);
    }
}

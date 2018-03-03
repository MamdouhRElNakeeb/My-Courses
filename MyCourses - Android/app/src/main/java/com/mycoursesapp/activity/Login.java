package com.mycoursesapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mycoursesapp.adapter.VPAdapter;
import com.mycoursesapp.R;
import com.mycoursesapp.fragment.Register;
import com.mycoursesapp.helper.LocaleManager;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class Login extends AppCompatActivity{

    ViewPager loginVP;

    public Button loginRegBtn;
    CircleIndicator circleIndicator;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginRegBtn = findViewById(R.id.loginRegBtn);

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginVP.getCurrentItem() == 0){

                    loginVP.setCurrentItem(1);
                    loginRegBtn.setText("Already user? ... Login here");
                }
                else {

                    loginVP.setCurrentItem(0);
                    loginRegBtn.setText("Don't have an account? Register here");
                }
            }
        });


        setupViewPager();
    }

    private void setupViewPager(){

        loginVP = findViewById(R.id.loginVP);
        circleIndicator = findViewById(R.id.page_indicator);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        com.mycoursesapp.fragment.Login login = new com.mycoursesapp.fragment.Login();
        Register register = new Register();
        adapter.addFragment(login, "Login");
        adapter.addFragment(register, "Register");
        loginVP.setAdapter(adapter);

        circleIndicator.setViewPager(loginVP);

        loginVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 1){
                    loginRegBtn.setText("Already user? ... Login here");
                }
                else {
                    loginRegBtn.setText("Don't have an account? Register here");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

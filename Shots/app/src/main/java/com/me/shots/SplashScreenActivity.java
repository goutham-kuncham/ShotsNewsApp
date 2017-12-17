package com.me.shots;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT=3000;
    String LOGIN_API_URL = "http://ec2-52-14-50-89.us-east-2.compute.amazonaws.com/api/auth";
    String LOGIN_result;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences=getSharedPreferences("MYSHAREDPREFERENCES",MODE_PRIVATE);
        String name=sharedPreferences.getString("email",null);
        String pass=sharedPreferences.getString("password",null);


        if (!(name == null || pass == null)) {
            Log.e("mytag","sharedprefworking======"+name+"-----"+pass);
            Intent homeint=new Intent(this,HomeActivity.class);
            startActivity(homeint);
            finish();}


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                finish();
            }
        },SPLASH_SCREEN_TIMEOUT);
    }
}

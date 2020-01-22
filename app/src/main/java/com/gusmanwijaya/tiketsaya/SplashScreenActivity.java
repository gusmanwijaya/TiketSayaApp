package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    //Deklarasi variabel
    Animation appsplash, bottomtotop;
    ImageView app_logo;
    TextView app_subtitle;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Load element
        app_logo = findViewById(R.id.app_logo);
        app_subtitle = findViewById(R.id.app_subtitle);

        //Load animation
        appsplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        //Run animation
        app_logo.startAnimation(appsplash);
        app_subtitle.startAnimation(bottomtotop);

        AmbilUsernameLokal();

    }

    public void AmbilUsernameLokal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");

        //check apakah sudah pernah login atau belum
        if (username_kunci_baru.isEmpty()){
            //Membuat timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Pindah ke activity lain
                    Intent intent = new Intent(SplashScreenActivity.this, GetStartedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000); //2000 ms = 2s
        }else {
            //Membuat timer untuk 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Pindah ke activity lain
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000); //2000 ms = 2s
        }
    }
}

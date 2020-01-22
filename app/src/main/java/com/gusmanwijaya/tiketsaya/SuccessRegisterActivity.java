package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessRegisterActivity extends AppCompatActivity {

    //Daftarkan/Deklarasi variabel
    Button btn_explore_now;
    Animation appsplash, toptobottom, bottomtotop;
    ImageView icon_success;
    TextView title, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        //Load element
        btn_explore_now = findViewById(R.id.btn_explore);
        icon_success = findViewById(R.id.icon_success);
        title = findViewById(R.id.app_title);
        subtitle = findViewById(R.id.app_subtitle);

        //Load animation
        appsplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        //Run animation
        icon_success.startAnimation(appsplash);
        title.startAnimation(toptobottom);
        subtitle.startAnimation(toptobottom);
        btn_explore_now.startAnimation(bottomtotop);

        //Set Click
        btn_explore_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessRegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity {

    Button btn_sign_in, btn_new_account_create;
    Animation ttb, btt;
    ImageView emblem_app;
    TextView intro_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // load animation
        ttb = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        btt = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account_create = findViewById(R.id.btn_new_account_create);

        emblem_app = findViewById(R.id.emblem_app);
        intro_app = findViewById(R.id.intro_app);

        // run animation
        emblem_app.startAnimation(ttb);
        intro_app.startAnimation(ttb);
        btn_sign_in.startAnimation(btt);
        btn_new_account_create.startAnimation(btt);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_new_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this,RegisterOneActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

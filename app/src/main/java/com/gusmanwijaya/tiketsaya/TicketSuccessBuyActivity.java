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

public class TicketSuccessBuyActivity extends AppCompatActivity {

    //Deklarasi variabel
    ImageView success_buy;
    TextView title_success_buy, subtitle_success_buy;
    Button btn_view_tickets, btn_my_dashboard;
    Animation appsplash, toptobottom, bottomtotop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success_buy);

        //Load element
        success_buy = findViewById(R.id.icon_success_ticket);
        title_success_buy = findViewById(R.id.app_title);
        subtitle_success_buy = findViewById(R.id.app_subtitle);
        btn_view_tickets = findViewById(R.id.btn_view_ticket);
        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);

        //Load animation
        appsplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        //Run animation
        success_buy.startAnimation(appsplash);
        title_success_buy.startAnimation(toptobottom);
        subtitle_success_buy.startAnimation(toptobottom);
        btn_view_tickets.startAnimation(bottomtotop);
        btn_my_dashboard.startAnimation(bottomtotop);

        //set click
        btn_view_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketSuccessBuyActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketSuccessBuyActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

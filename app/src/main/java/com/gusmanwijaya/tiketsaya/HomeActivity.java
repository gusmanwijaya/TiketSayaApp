package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    LinearLayout btn_ticket_pisa, btn_ticket_torri, btn_ticket_pagoda, btn_ticket_candi, btn_ticket_sphinx, btn_ticket_monas;
    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap, bio, user_balance;

    DatabaseReference databaseReference;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AmbilUsernameLokal();

        //load element
        btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_ticket_candi = findViewById(R.id.btn_ticket_candi);
        btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        btn_ticket_monas = findViewById(R.id.btn_ticket_monas);
        btn_to_profile = findViewById(R.id.btn_to_profile);
        photo_home_user = findViewById(R.id.photo_home_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);

        //connect database di firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);

        //mengeload data (refresh data)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //ambil data dari firebase
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " +dataSnapshot.child("user_balance").getValue().toString());

                //ambil url photo dari firebase
                Picasso.with(HomeActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_home_user);
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_ticket_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Pisa");
                startActivity(intent);
                finish();
            }
        });

        btn_ticket_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Torri");
                startActivity(intent);
                finish();
            }
        });

        btn_ticket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Pagoda");
                startActivity(intent);
                finish();
            }
        });

        btn_ticket_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Candi");
                startActivity(intent);
                finish();
            }
        });

        btn_ticket_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Sphinx");
                startActivity(intent);
                finish();
            }
        });

        btn_ticket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah activity
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakkan data kepada intent
                intent.putExtra("jenis_tiket", "Monas");
                startActivity(intent);
                finish();
            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void AmbilUsernameLokal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");
    }
}

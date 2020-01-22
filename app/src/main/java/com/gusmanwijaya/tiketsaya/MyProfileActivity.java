package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    RecyclerView myticket_place;
    Button btn_edit_profile, btn_back_home, btn_sign_out;
    ImageView photo_profile;
    TextView nama_lengkap, bio;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    DatabaseReference databaseReference, databaseReference1;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        AmbilUsernameLokal();

        //load element
        myticket_place = findViewById(R.id.myticket_place);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        photo_profile = findViewById(R.id.photo_profile);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        btn_back_home = findViewById(R.id.btn_back_home);
        btn_sign_out = findViewById(R.id.btn_sign_out);

        myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();

        //connect database di firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);

        //memproses data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mengambil data dari firebase
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());

                Picasso.with(MyProfileActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //menghapus username lokal
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_kunci, null);
                editor.apply();

                //pindah activity
                Intent intent = new Intent(MyProfileActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //connect ke database firebase
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_kunci_baru);

        //memproses data
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(MyProfileActivity.this, list);
                myticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void AmbilUsernameLokal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");
    }
}

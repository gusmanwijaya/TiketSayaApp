package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button btn_pay_now, btnplus, btnmines;
    TextView textJumlahTiket, textmybalance, texttotalharga, nama_wisata, lokasi, ketentuan;
    LinearLayout btn_back;
    Integer valueJumlahTiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView notice_uang;
    Integer sisa_balance = 0;

    DatabaseReference databaseReference, databaseReference1, databaseReference2, databaseReference3;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    String date_wisata = "";
    String time_wisata = "";

    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        AmbilUsernameLokal();

        //Load element
        btn_pay_now = findViewById(R.id.btn_pay_now);
        btnplus = findViewById(R.id.btnplus);
        btnmines = findViewById(R.id.btnmines);
        textJumlahTiket = findViewById(R.id.textjumlahtiket);
        textmybalance = findViewById(R.id.textmybalance);
        texttotalharga = findViewById(R.id.texttotalharga);
        notice_uang = findViewById(R.id.notice_uang);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);
        btn_back = findViewById(R.id.btn_back);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //mengambil data dari firebase berdasarkan intent
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);

        //mengeload data (refresh data)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menimpa data yang ada dengan data yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());
                valuetotalharga = valuehargatiket * valueJumlahTiket;
                texttotalharga.setText("US$ " + valuetotalharga + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data user dari firebase
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);

        //mengeload data dari firebase (refresh data)
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menimpa data yang ada dengan data yang baru
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());

                //Setting value baru untuk text view my balance
                textmybalance.setText("US$ " +mybalance+ "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Secara default, hide button mines
        textJumlahTiket.setText(valueJumlahTiket.toString());
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);

        //Secara default, hide notice uang tidak cukup
        notice_uang.setVisibility(View.GONE);

        //Set Click
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTiket+=1;
                textJumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1){
                    btnmines.animate().alpha(1).setDuration(300).start();
                    btnmines.setEnabled(true);
                }
                valuetotalharga = valuehargatiket * valueJumlahTiket;
                texttotalharga.setText("US$ " + valuetotalharga + "");
                if (valuetotalharga > mybalance){
                    btn_pay_now.animate().translationY(250).alpha(0).setDuration(300).start();
                    btn_pay_now.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTiket-=1;
                textJumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2){
                    btnmines.animate().alpha(0).setDuration(300).start();
                    btnmines.setEnabled(false);
                }
                valuetotalharga = valuehargatiket * valueJumlahTiket;
                texttotalharga.setText("US$ " + valuetotalharga + "");
                if (valuetotalharga < mybalance){
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(300).start();
                    btn_pay_now.setEnabled(true);
                    textmybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set state menjadi loading
                btn_pay_now.setEnabled(false);
                btn_pay_now.setText("Loading ...");

                //menyimpan data user kepada firebase dan membuat tabel baru "MyTickets"
                databaseReference2 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_kunci_baru)
                        .child(nama_wisata.getText().toString() + nomor_transaksi);

                //memproses data
                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        databaseReference2.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        databaseReference2.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        databaseReference2.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        databaseReference2.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        databaseReference2.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());
                        databaseReference2.getRef().child("date_wisata").setValue(date_wisata);
                        databaseReference2.getRef().child("time_wisata").setValue(time_wisata);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update data balance kepada user (yang saat ini login)
                //mengambil data user dari firebase
                databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);

                //memproses data
                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        databaseReference3.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //pindah activity
                Intent intent = new Intent(TicketCheckoutActivity.this, TicketSuccessBuyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void AmbilUsernameLokal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");
    }
}

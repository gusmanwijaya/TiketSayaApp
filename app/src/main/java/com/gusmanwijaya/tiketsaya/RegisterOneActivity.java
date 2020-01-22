package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    Button btn_continue;
    LinearLayout btn_back;
    EditText username, password, email_address;

    DatabaseReference databaseReference, databaseReference_username;
    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        //Load element
        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                //check apakah edit text username, password, dan email kosong atau tidak
                if (username.getText().toString().isEmpty()){
                    Toast.makeText(RegisterOneActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if (password.getText().toString().isEmpty()){
                    Toast.makeText(RegisterOneActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if (email_address.getText().toString().isEmpty()){
                    Toast.makeText(RegisterOneActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else {

                    //mengambil data username dari firebase
                    databaseReference_username = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(username.getText().toString());
                    databaseReference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check apakah username sudah terdaftar atau tidak
                            if (dataSnapshot.exists()){
                                Toast.makeText(RegisterOneActivity.this, "Username sudah terdaftar!", Toast.LENGTH_SHORT).show();
                                btn_continue.setEnabled(true);
                                btn_continue.setText("CONTINUE");
                            }else {

                                //Menyimpan data pada local storage handphone
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_kunci, username.getText().toString());
                                editor.apply();

                                //Simpan pada database (firebase)
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                        dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                        dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                        dataSnapshot.getRef().child("user_balance").setValue(800);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(RegisterOneActivity.this, "Periksa koneksi interner anda!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //Pindah activity
                                Intent intent = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }
}

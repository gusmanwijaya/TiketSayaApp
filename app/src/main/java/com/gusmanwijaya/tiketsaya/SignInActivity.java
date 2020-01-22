package com.gusmanwijaya.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    TextView create_new_account;
    Button btn_sign_in;
    EditText xusername, xpassword;

    DatabaseReference databaseReference;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Load element
        create_new_account = findViewById(R.id.tv_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterOneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading ...");

                //ambil username dan password dari edit text
                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                //kondisi edit text username dan password tidak boleh kosong
                if (username.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("SIGN IN");
                }else {
                    if (password.isEmpty()){
                        Toast.makeText(SignInActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        btn_sign_in.setEnabled(true);
                        btn_sign_in.setText("SIGN IN");
                    }else {

                        //connect database di firebase
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);

                        //memproses data
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                //check apakah username ada atau tidak
                                if (dataSnapshot.exists()){

                                    //ambil data password dari firebase
                                    String passwordDariFirebase = dataSnapshot.child("password").getValue().toString();

                                    //validasi password dengan password dari firebase
                                    if (password.equals(passwordDariFirebase)){

                                        //simpan username (key) ke penyimpanan lokal
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_kunci, xusername.getText().toString());
                                        editor.apply();
                                        
                                        //pindah activity
                                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }else {
                                        Toast.makeText(SignInActivity.this, "Password salah!", Toast.LENGTH_SHORT).show();
                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText("SIGN IN");
                                    }
                                }else {
                                    Toast.makeText(SignInActivity.this, "Username belum terdaftar!", Toast.LENGTH_SHORT).show();
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(SignInActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
                                btn_sign_in.setEnabled(true);
                                btn_sign_in.setText("SIGN IN");
                            }
                        });
                    }
                }
            }
        });
    }
}

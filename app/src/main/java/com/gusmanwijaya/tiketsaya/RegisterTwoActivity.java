package com.gusmanwijaya.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue, btn_add_photo;
    ImageView pic_photo_register_user;
    EditText bio, nama_lengkap;

    Uri photo_lokasi;
    Integer max_photo = 1;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        AmbilUsernameLokal();

        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        bio = findViewById(R.id.bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);


        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CariPhoto();

            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                //check apakah edit text nama & bio kosong atau tidak
                if (nama_lengkap.getText().toString().isEmpty()){
                    Toast.makeText(RegisterTwoActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if (bio.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterTwoActivity.this, "Bio tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else {

                    // menyimpan kepada firebase
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);
                    storageReference = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(username_kunci_baru);

                    // validasi untuk file (apakah ada?)
                    if (photo_lokasi != null){

                        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." +
                                AmbilFileExtension(photo_lokasi));

                        storageReference1.putFile(photo_lokasi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String uri_photo = uri.toString();
                                        databaseReference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        databaseReference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                        databaseReference.getRef().child("bio").setValue(bio.getText().toString());

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        //Pindah activity
                                        Intent intent = new Intent(RegisterTwoActivity.this, SuccessRegisterActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });

                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        });

                    }
                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    String AmbilFileExtension(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void CariPhoto(){

        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, max_photo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == max_photo && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_lokasi = data.getData();
            Picasso.with(this).load(photo_lokasi).centerCrop().fit().into(pic_photo_register_user);

        }

    }

    public void AmbilUsernameLokal(){

        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");

    }

}

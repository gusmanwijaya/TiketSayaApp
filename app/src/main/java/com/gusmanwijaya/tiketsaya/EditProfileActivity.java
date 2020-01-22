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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    ImageView photo_edit_profile;
    EditText xnama_lengkap, xbio, xusername, xpassword, xemail_address;
    Button btn_save, btn_add_photo;
    LinearLayout btn_back;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri photo_lokasi;
    Integer max_photo = 1;

    String USERNAME_KUNCI = "usernamekunci";
    String username_kunci = "";
    String username_kunci_baru = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        AmbilUsernameLokal();

        //load element
        photo_edit_profile = findViewById(R.id.photo_edit_profile);
        xnama_lengkap = findViewById(R.id.xnama_lengkap);
        xbio = findViewById(R.id.xbio);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        xemail_address = findViewById(R.id.xemail_address);
        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);
        btn_add_photo = findViewById(R.id.btn_add_photo);

        //connect database di firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_kunci_baru);
        storageReference = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(username_kunci_baru);

        //mengeload data (refresh data)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //mengambil data dari database di firebase
                xnama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xpassword.setText(dataSnapshot.child("password").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());

                Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_edit_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set state menjadi loading
                btn_save.setEnabled(false);
                btn_save.setText("Loading ...");

                //check bahwa edit text tidak boleh kosong
                if (xnama_lengkap.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE PROFILE");
                }else if (xbio.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Bio tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE PROFILE");
                }else if (xusername.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE PROFILE");
                }else if (xpassword.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE PROFILE");
                }else if (xemail_address.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE PROFILE");
                }else {

                    //memproses data
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //mengupdate data ke database di firebase
                            databaseReference.getRef().child("nama_lengkap").setValue(xnama_lengkap.getText().toString());
                            databaseReference.getRef().child("bio").setValue(xbio.getText().toString());
                            databaseReference.getRef().child("username").setValue(xusername.getText().toString());
                            databaseReference.getRef().child("password").setValue(xpassword.getText().toString());
                            databaseReference.getRef().child("email_address").setValue(xemail_address.getText().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //validasi file apakah ada atau tidak
                    if (photo_lokasi != null){
                        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "."
                                +AmbilFileExtension(photo_lokasi));

                        storageReference1.putFile(photo_lokasi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri_photo = uri.toString();
                                        databaseReference.getRef().child("url_photo_profile").setValue(uri_photo);
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        //pindah activity
                                        Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
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

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CariPhoto();
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
            Picasso.with(this).load(photo_lokasi).centerCrop().fit().into(photo_edit_profile);

        }
    }

    public void AmbilUsernameLokal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KUNCI, MODE_PRIVATE);
        username_kunci_baru = sharedPreferences.getString(username_kunci, "");
    }
}

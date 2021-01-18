package com.example.sarthiperson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonDatabaseStore extends AppCompatActivity {
    EditText personName,personAge,personPhoneNumber;
    Uri filePath;
    ImageView personImg;
    Button browse,addInformation;
    Bitmap bitmap;
    int rating=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_database_store);
        personImg=findViewById(R.id.personimg);
        addInformation=findViewById(R.id.addInformation);
        browse=findViewById(R.id.browsepersonimage);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(PersonDatabaseStore.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select image file"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        addInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){

                            String token=task.getResult().getToken();
                            Log.d("persontoken:","TOKEN:"+token);
                            FirebaseDatabase.getInstance().getReference("Tokens").child(personName.getText().toString()).child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                });
                uploadToFirebase();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filePath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                personImg.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadToFirebase() {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        personName=findViewById(R.id.personname);
        personAge=findViewById(R.id.personage);
        personPhoneNumber=findViewById(R.id.personphonenumber);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(50));
        uploader.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              dialog.dismiss();
                              FirebaseDatabase db=FirebaseDatabase.getInstance();
                              DatabaseReference root=db.getReference("persons");
                              DatabasePersonInformationStoreHolder obj=new DatabasePersonInformationStoreHolder(Integer.parseInt(personAge.getText().toString()),
                                      personName.getText().toString(),uri.toString(),rating);
                              root.child(personPhoneNumber.getText().toString()).setValue(obj);
                              personName.setText("");
                              personAge.setText("");
                              personPhoneNumber.setText("");
                              personImg.setImageResource(R.drawable.person);
                              Toast.makeText(PersonDatabaseStore.this, "your information uploaded", Toast.LENGTH_LONG).show();

                          }
                      });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");

                    }
                });

    }

}
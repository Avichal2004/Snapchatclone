
package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class snapActivity extends AppCompatActivity {


    Button btnOpenGallery , btnChooseuser;
    ImageView imgview;
    EditText edtMessage;


    public static final int PICK_IMAGE=1;
    Uri imageuri;
    StorageReference mStorageref;
    UUID imagename=UUID.randomUUID();
    StorageTask mUploadtask;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);
        btnOpenGallery=findViewById(R.id.btnOpenGallery);
        btnChooseuser=findViewById(R.id.btnSelectUser);
        imgview=findViewById(R.id.imageView);
        edtMessage=findViewById(R.id.edtMessage);

       btnOpenGallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openGallery();
           }


       });
       btnChooseuser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(snapActivity.this, Chooseuser.class);
               intent.putExtra("Imagename",imagename.toString());
               intent.putExtra("Imageurl",downloadUrl);
               intent.putExtra("message",edtMessage.getText().toString());
               startActivity(intent);
           }
       });
    }
    private void openGallery() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
          imageuri= data.getData();
          imgview.setImageURI(imageuri);
          uploadImage();
        }
    }

    private void uploadImage() {
        if (imageuri!=null){
            mStorageref= FirebaseStorage.getInstance().getReference().child("image").child(imagename+".jpg");
            mUploadtask =mStorageref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(snapActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                    mStorageref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl =task.getResult().toString();
                            Log.i("download url",downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(snapActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
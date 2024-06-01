package com.example.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.net.CookieHandler;

public class snapdetail extends AppCompatActivity {

    ImageView img;
    TextView text;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapdetail);
        img =findViewById(R.id.imageView3);
        text =findViewById(R.id.txtmessage);
        Intent intent=getIntent();

        String url =intent.getExtras().getString("url");
        String message =intent.getExtras().getString("message");
        key = intent.getStringExtra("key");
        text.setText(message);
        Picasso.get().load(url).into(img);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("snaps").child(key).removeValue();
    }
}
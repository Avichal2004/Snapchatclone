package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Chooseuser extends AppCompatActivity {

    ListView userlist;
    ArrayList<String> emaillist;
    ArrayList<String> uuidlist;
    ArrayAdapter arrayAdapter;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseuser);

        userlist=findViewById(R.id.listview);
        emaillist=new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,emaillist);
        uuidlist = new ArrayList<>();
        userlist.setAdapter(arrayAdapter);
        mref = FirebaseDatabase.getInstance().getReference();
        mref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emaillist.clear();
                uuidlist.clear();
                for (DataSnapshot postsnapshot:snapshot.getChildren()){
                       String email =postsnapshot.child("email").getValue().toString();
                      String uuid= postsnapshot.getKey();
                      uuidlist.add(uuid);
                       emaillist.add(email);
                       arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=getIntent();

                String url = intent.getExtras().getString("Imageurl");
                String imagename = intent.getExtras().getString("Imagename");
                String message = intent.getExtras().getString("message");

                String from= FirebaseAuth.getInstance().getCurrentUser().getEmail();

                HashMap<String,String> map =new HashMap<>();
                map.put("Url",url);
                map.put("From",from);
                map.put("Url",url);
                map.put("Url",url);

                FirebaseDatabase.getInstance().getReference().child("user").child(uuidlist.get(position)).child("snaps").push().setValue(map);

            }
        });
    }
}
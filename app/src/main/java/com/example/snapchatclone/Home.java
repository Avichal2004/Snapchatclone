package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Home extends AppCompatActivity {


    ListView snapslist;
    ArrayList<String> snap;
    ArrayList<DataSnapshot> snapshots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        snapslist =findViewById(R.id.snaplist);
        snap =new ArrayList<>();
        snapshots=new ArrayList<>();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,snap);
        snapslist.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("snaps").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            snap.clear();
                for (DataSnapshot postsnapshot:snapshot.getChildren()){
                    String email =postsnapshot.child("from").getValue().toString();
                    snap.add(email);
                    snapshots.add(postsnapshot);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        snapslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Home.this,snapdetail.class);
               DataSnapshot snapshotsss=snapshots.get(position);
                intent.putExtra("Imageurl",snapshotsss.child("url").getValue().toString());
                intent.putExtra("key",snapshotsss.getKey());
                intent.putExtra("message",snapshotsss.child("message").getValue().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.snapmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.takeSnap){
           Intent intent = new Intent(Home.this, snapActivity.class);
           startActivity(intent);
       } else if (item.getItemId()==R.id.logout) {
           Intent intent=new Intent(Home.this,MainActivity.class);
           startActivity(intent);
       }


        return super.onOptionsItemSelected(item);
    }
}
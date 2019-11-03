package com.example.android.roadcomplaint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nichay Jain on 11-06-2019.
 */

public class MyComplaints extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private DatabaseReference UsersRef;
    private String currentUserID;
    ArrayList<Details> al=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_list);
       mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.child("name").getValue().toString();
                    String description = ds.child("description").getValue().toString();
                    String address = ds.child("address").getValue().toString();
                    String latitude=ds.child("latitude").getValue().toString();
                    String longitude=ds.child("longitude").getValue().toString();
                    String image=ds.child("image").getValue().toString();
                    Details details=new Details(name,description,address,latitude,longitude,image);
                    al.add(details);
                    for(Details i:al)
                    {
                        Log.d("hello",i.getName().toString());
                    }







                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ListView listView = (ListView) findViewById(R.id.list);

        ComplaintAdapter mAdapter = new ComplaintAdapter(this,al);
        listView.setAdapter(mAdapter);
    }

    }


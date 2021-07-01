package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class facper extends AppCompatActivity {
    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facper);
        getSupportActionBar().hide();

        listView = findViewById(R.id.lv1);
        getIntent();
        final String subcode= getIntent().getStringExtra("subcode");
        
        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";

        DatabaseReference dr = FirebaseDatabase.getInstance(url).getReference("students");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String id=snapshot1.getKey().trim();
                    calc(id,subcode);
                    //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calc(String id,final String subcode) {
        final String idn=id;

        //url of the firebase database
        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference=FirebaseDatabase.getInstance(url).getReference("k8");
        //code to get the count of classes how many are attended by individual student
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    if ((dataSnapshot.hasChild(idn)) && (dataSnapshot.child("subject").getValue().toString().equals(subcode))) {
                        counter ++;
                    }
                }
                //Toast.makeText(getApplicationContext(),idn+" "+counter,Toast.LENGTH_SHORT).show();
                clscount(counter,idn,subcode);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        });
    }

    private void clscount(final int counter,final String idn,final String subcode) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference dr=FirebaseDatabase.getInstance(url).getReference("k8");
        
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count=0;//=snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.child("subject").getValue().toString().equals(subcode)){
                        count++;
                    }
                }
                int x = (int) (((double) counter / (double) count) * 100);
                String y=Integer.toString(x);

                //Toast.makeText(getApplicationContext(),idn+" "+y,Toast.LENGTH_SHORT).show();


                String dis=idn.concat("                        ").concat(y);

                arrayList.add(dis);
                //arrayList.add(y);
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
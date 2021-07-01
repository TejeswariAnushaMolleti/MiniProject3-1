package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getrecords extends AppCompatActivity {
    //define variable with types
    EditText et1, ets;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getrecords);
        //address the items used in the xml file
        et1 = findViewById(R.id.et1);
        ets = findViewById(R.id.et2);
        listView = findViewById(R.id.lv);
        getSupportActionBar().hide();       //hide action bar
    }

    //method to get records of the attendance
    public void getrecs(View view) {
        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        final String dateentered = et1.getText().toString().trim();
        ets = findViewById(R.id.et2);
        String etsub = ets.getText().toString().trim();
        if (etsub.equals("")) {

            Toast.makeText(getApplicationContext(), "enter subject id", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference dr = FirebaseDatabase.getInstance(url).getReference("k8");
            //listener for the reference
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.hasChild("code")) {
                            final String datepresent = dataSnapshot.child("code").getValue().toString();

                            if (dateentered.length() == 10) {
                                //if the date entered is in the yyyy.mm.dd format match with date present in the  database
                                String[] str = datepresent.split(" ");
                                if (dateentered.equals(str[0].trim())) {
                                    //method call
                                    cerive(dataSnapshot);
                                }
                            }
                            //if the date entered is yyyy.mm i.e., of the month
                            else if (dateentered.length() == 7) {
                                String sb = datepresent.substring(0, 7);
                                //if matched
                                if (dateentered.equals(sb)) {
                                    //call method
                                    cerive(dataSnapshot);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    //method definition
    private void cerive(DataSnapshot dataSnapshot) {
        ets = findViewById(R.id.et2);
        String etsub = ets.getText().toString().trim();

            DataSnapshot snapshot = dataSnapshot;
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
            //long s = snapshot.getChildrenCount();
            String sub = dataSnapshot.child("subject").getValue().toString();

            if (sub.equals(etsub)) {
                //code to add the data into list view and display
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    String v = dataSnapshot1.getKey();
                    if (v.equals("code")) {
                        String d = dataSnapshot1.getValue().toString();
                        arrayList.add(v);
                        arrayList.add(d);
                    } else if (v.equals("subject")) {
                        arrayList.add("   ");
                    } else {
                        arrayList.add(v);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                }
            }
        }
    }
package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class subcode extends AppCompatActivity {
    //define variable types
    private TextView tv, nam, cls, idd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseauth;
                                                                                                                                                                                    //@TargetApi(Build.VERSION_CODES.ECLAIR_0_1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                                                                                                                         //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subcode);
        getSupportActionBar().hide();  //hide action bar

        //get instance of logged in user
        mFirebaseauth= FirebaseAuth.getInstance();

        //get access of the items used in the xml file
        tv = findViewById(R.id.timed);
        nam = findViewById(R.id.name);
        cls = findViewById(R.id.cls);
        idd = findViewById(R.id.id_num);
        String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        firebaseDatabase = FirebaseDatabase.getInstance(url);

        //databaseReference = firebaseDatabase.getReference("students");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");                          //pattern "yyyy.MM.dd G 'at' HH:mm:ss Z" op:- 2021.04.27 AD at 17:03:30 +0530
        //String cdt = sdf.format(new Date());

        //get the strings sent to this layout
        Intent intent=getIntent();
        String user_id = intent.getStringExtra("userid");
        String user_name = intent.getStringExtra("name");
        String user_class = intent.getStringExtra("class");
        String cdt=intent.getStringExtra("cdt");

        //set the data into text view to display
        nam.setText(user_name);
        cls.setText(user_class);
        idd.setText(user_id);
        tv.setText(cdt);
    }

    //code to sign-out
    public void signout(View view) {
        FirebaseUser mfirebaseuser= mFirebaseauth.getCurrentUser();
        mFirebaseauth.signOut();
        Intent i=new Intent(subcode.this,Main2Activity.class);
        startActivity(i);
        finish();
    }
}

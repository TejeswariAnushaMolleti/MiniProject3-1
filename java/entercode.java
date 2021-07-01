package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class entercode extends AppCompatActivity {
    //define variables with types
    EditText ecode,idnum;
    Button btn;
    AwesomeValidation awesomeValidation,percentagevalidation;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entercode);
        getSupportActionBar().hide();
        //get instance of the firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        //relate the above defined types of variables with the ones defined in xml
        ecode = findViewById(R.id.encode);
        btn = findViewById(R.id.bts);
        idnum = findViewById(R.id.idn);

       //add validation
       awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
       //adding validation to the code entered
        awesomeValidation.addValidation(this, R.id.encode,
                ".{6}", R.string.invalid_code);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if validation successful
                if (awesomeValidation.validate()) {

                    //calling a method if successful
                    isUser();

                } else {
                    //validation not successful
                    Toast.makeText(getApplicationContext(),
                            "invalid code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //method definition
    private void isUser() {
        //get the user entered id from the text view
        final String userEnteredUserid = idnum.getText().toString().trim();
        //uri is url of our firebase database
        final String uri = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";

        //query to check whether there exists any user with the id equal to the entered in text view
        Query checkUser = FirebaseDatabase.getInstance(uri).getReference("students").orderByChild("id number").equalTo(userEnteredUserid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if user exists
                if (dataSnapshot.exists()) {
                    //get the following details of the user-student
                    final String nameFromDB = dataSnapshot.child(userEnteredUserid).child("name").getValue(String.class);
                    final String classFromDB = dataSnapshot.child(userEnteredUserid).child("class").getValue(String.class);
                    final String idFromDB = dataSnapshot.child(userEnteredUserid).child("id number").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(userEnteredUserid).child("email").getValue(String.class).trim();

                    mFirebaseAuth = FirebaseAuth.getInstance();
                    final FirebaseUser mfirebaseuser = mFirebaseAuth.getCurrentUser();
                    String gmail = mfirebaseuser.getEmail().trim();

                    //if the logged in user is equal to the one which the id is entered
                    if (gmail.equals(emailFromDB)) {
                        //get the code entered
                        final String userEnteredUsercode = ecode.getText().toString().trim();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");                          //pattern "yyyy.MM.dd G 'at' HH:mm:ss Z" op:- 2021.04.27 AD at 17:03:30 +0530
                        final String cdt = sdf.format(new Date());
                        //create hash map for the time stamp
                        final HashMap<String, Object> map = new HashMap<>();
                        map.put("time", cdt);

                        final String userEnterc = ecode.getText().toString().trim();

                        //to get the code from the temporary variable
                        final Query codee = FirebaseDatabase.getInstance(uri).getReference("tempk8").orderByChild("temp");   //.getKey(String.class);
                        codee.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    //if there exists code in the variable
                                    final String tempcd = snapshot.child("temp").getValue(String.class).trim();

                                    //if the code is empty
                                    if (tempcd.equals("")) {
                                        Toast.makeText(getApplicationContext(), "time up", Toast.LENGTH_SHORT).show();
                                    }
                                    //if the code in temp variable equals to the one entered by students
                                    if (tempcd.equals(userEnterc)) {
                                        //store the value of the code with the timestamp to mark attendance
                                        FirebaseDatabase.getInstance(uri).getReference("k8/" + userEnteredUsercode).child(userEnteredUserid)
                                                .setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.i("hikj", "onComplete");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("hikj", "onFailure" + e.toString());
                                                Toast.makeText(entercode.this, "error" + e, Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("hikj", "On Success");
                                            }
                                        });
                                        //send the values to next layout
                                        Intent intent = new Intent(entercode.this, subcode.class);
                                        intent.putExtra("name", nameFromDB);
                                        intent.putExtra("userid", idFromDB);
                                        intent.putExtra("class", classFromDB);
                                        intent.putExtra("cdt",cdt);
                                        startActivity(intent);
                                    } else {
                                        //if code doesn't exist
                                        Toast.makeText(getApplicationContext(), "check your code", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    else {
                        //if id of the logged in user doesn't match with the one entered
                        Toast.makeText(getApplicationContext(), "please check your id number", Toast.LENGTH_LONG).show();
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(entercode.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //method defined to get attendance percentage of the student
    public void percet(View view) {
        final String id = idnum.getText().toString().trim();

        if (id.length() == 7) {

            final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
            DatabaseReference fr = FirebaseDatabase.getInstance(url).getReference("students");

            //if there exists the user of the id entered
            fr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(id)) {
                        //method call
                        perce();
                    } else {
                        Toast.makeText(getApplicationContext(), "invalid id entered", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "invalid id entered", Toast.LENGTH_SHORT).show();
        }
    }


  //method definition
    private void perce () {
        //path of the firebase database
            final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
            DatabaseReference fr = FirebaseDatabase.getInstance(url).getReference("k8");
            //code to get the total count of the classes
            fr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int countclass = (int) snapshot.getChildrenCount();
                    //calling of the method
                    countedatnd(countclass);
                   // Toast.makeText(getApplicationContext(),Integer.toString(countclass),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
        });
    }
    //method definition
    private void countedatnd(int countclass) {
        final String id=idnum.getText().toString().trim();
        final int totcl=countclass;
        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();

        //url of the firebase database
        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference=FirebaseDatabase.getInstance(url).getReference("k8");
        //code to get the count of classes how many are attended by individual student
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    if (dataSnapshot.hasChild(id)) {
                        counter ++;
                    }
                }
                //method calling
                nextlay(totcl,counter);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        });
    }

    //method definition
    private void nextlay(int totcl, int counter) {
        String id=idnum.getText().toString();
        //code to calculate the percentage                                                                                                                                                               //        String t=Integer.toString(totcl);
        int x = (int) (((double) counter / (double) totcl) * 100);                                                                                                  //        String c=Integer.toString(counter);

        //send the percentage value to the next layout
        Intent i=new Intent(entercode.this,percentage.class);
        i.putExtra("tot",Integer.toString(x));
        i.putExtra("idnum",id);
        startActivity(i);
    }

    public void signoutt(View view) {

        FirebaseUser mfirebaseuser= mFirebaseAuth.getCurrentUser();
        mFirebaseAuth.signOut();
        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(i);
        finish();
    }
}
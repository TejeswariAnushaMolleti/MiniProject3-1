package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import static com.example.firstapp.R.string.invalid_confirm_password;
import static com.example.firstapp.R.string.invalid_email;
import static com.example.firstapp.R.string.invalid_name;
import static com.example.firstapp.R.string.invalid_password;

public class facReg extends AppCompatActivity {

    EditText etname, etmail, etpass, etcpass;
    Button bt;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_reg);

        //get instance of firebase auth
        mAuth = FirebaseAuth.getInstance();

        //hide action bar
        getSupportActionBar().hide();

        //relate the widgets from xml to respective types of variables
        etname = findViewById(R.id.et_ffname);
        etmail = findViewById(R.id.et_ffemail);
        etpass = findViewById(R.id.et_fpassword);
        etcpass = findViewById(R.id.et_fconfirmpassword);
        bt = findViewById(R.id.et_fbtn);

        //add validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //validation to name
        awesomeValidation.addValidation(this, R.id.et_ffname,
                RegexTemplate.NOT_EMPTY, invalid_name);
        //validation to email
        awesomeValidation.addValidation(this, R.id.et_ffemail,
                Patterns.EMAIL_ADDRESS, invalid_email);
        //validation to password
        awesomeValidation.addValidation(this, R.id.et_fpassword,
                ".{6,}", invalid_password);
        //validation to confirm password
        awesomeValidation.addValidation(this, R.id.et_fconfirmpassword,
                R.id.et_fpassword, invalid_confirm_password);
        //listener to submit button
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if validation successful
                if (awesomeValidation.validate()) {

                    //calling the register new user method
                    registerNewUser();

                } else {
                    //if validation failed
                    Toast.makeText(getApplicationContext()
                            , "credentials wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //definition of the register new user method
    private void registerNewUser() {
        final String name, email, password;
        email = etmail.getText().toString();
        password = etpass.getText().toString();
        name = etname.getText().toString();

        //if email is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if password entered is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(facReg.this,
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if email is not empty to check whether it is student's mail ot faculty's mail
        if (email != null) {
            String d = "";
            Integer i = 0;
            for (i = 1; i <= 6; i++) {
                d += email.charAt(i);
            }
            boolean b = d != null && d.matches("[0-9]+");
            boolean c = !b;
            //if not sudent's mail
            if (c) {
                //create user with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(facReg.this,
//                                        "created user with email and passcode",
//                                        Toast.LENGTH_LONG)
//                                        .show();
//                                //if user creation is successful
                                if (task.isSuccessful()) {
                                    Toast.makeText(facReg.this,
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    storedataof(name, email, password);
                                } else {
                                    //if registration is failed
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //to show the error message
                        Toast.makeText(facReg.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                //if it is student's mail
                Toast.makeText(getApplicationContext(),"invalid email",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //definition of method to store data
    private void storedataof(String name,String email,String password) {
        String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        firebaseDatabase = FirebaseDatabase.getInstance(url);

        //code to place all details of registered user in a single hash map
        HashMap<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("passcode",password);

        //code to set the values of hash inside the path faculty
        FirebaseDatabase.getInstance(url).getReference("faculty").child(name)
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("hikj", "onComplete");

                        Intent intent = new Intent(facReg.this, facLog.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("hikj", "onFailure" + e.toString());
                Toast.makeText(facReg.this,"error"+e,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("hikj", "On Success" );
            }
        });
    }

    //if already existing member
    public void facSign(View view) {
        Intent intent=new Intent(facReg.this,facLog.class);
        startActivity(intent);
        finish();
    }
}

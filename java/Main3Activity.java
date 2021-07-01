package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Main3Activity extends AppCompatActivity {

    //define the variables with types
    EditText etmail, etpassword;
    Button btsubmit;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //get authentication instamce
        mAuth = FirebaseAuth.getInstance();

        //hide action bar
        getSupportActionBar().hide();

        //relate the widgets used in the xml file for further use
        etmail = findViewById(R.id.et_email);
        etpassword = findViewById(R.id.et_password);
        btsubmit = findViewById(R.id.bt_submit);
        final TextView forgot=findViewById(R.id.forgot);

        //set on click listener for the forgot password text view
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main3Activity.this,"Please contact admin for your query",Toast.LENGTH_LONG).show();
            }
        });

        //set listener to login button
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginUserAccounts();
            }
        });
    }

    //define the method called in side listener of login method
    private void loginUserAccounts() {

        String email, password;
        email = etmail.getText().toString();
        password = etpassword.getText().toString();

        //displays toast if email is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //displays toast if password is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if email is not null then check whether it is student's mail or faulty's mail
        if (email != null) {
            String d = "";
            Integer i = 0;
            for (i = 1; i <= 6; i++) {
                d += email.charAt(i);
                }
            boolean b = d != null && d.matches("[0-9]+");
            //if students's mail then log in
            if (b) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task) {
                                        // sign-in success
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            //go to next layout
                                            Intent intent
                                                    = new Intent(Main3Activity.this,
                                                    entercode.class);
                                            startActivity(intent);
                                        } else {
                                            // sign-in failed
                                            Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                         }
            else {
                //invalid-credentials
                Toast.makeText(getApplicationContext(),"invalid credentials",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //code to go to register layout
    public void signup(View view) {
        Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
        startActivity(intent);
    }
}


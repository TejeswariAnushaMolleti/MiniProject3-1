package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
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

import static com.example.firstapp.R.string.invalid_email;
import static com.example.firstapp.R.string.invalid_password;

public class facLog extends AppCompatActivity {

    //define types of variables
    EditText etmail,etpassword;
    Button submit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_log);

        //get instance of the firebase
        mAuth = FirebaseAuth.getInstance();

        //to hide the action bar
        getSupportActionBar().hide();

        //relate the above defined variables to ones used in xml
        etmail=findViewById(R.id.et_fmail);
        etpassword=findViewById(R.id.et_fpass);
        submit=findViewById(R.id.bt_fsubmit);

        //code for forgot password
        final TextView forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(facLog.this,"Please contact admin for your query",Toast.LENGTH_LONG).show();
            }
        });

        //listener to login button
        submit.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling the method for user login
                loginUserAccounts();
            }
        }));
    }

    //method definition of the user to log in
    private void loginUserAccounts() {
        String email, password;
        email = etmail.getText().toString();
        password = etpassword.getText().toString();

        //if mail entered is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if password is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if email is entered
        if (email != null) {
            String d = "";
            Integer i = 0;
            for (i = 1; i <= 6; i++) {
                d += email.charAt(i);
            }
            boolean b = d != null && d.matches("[0-9]+");
            boolean c = !b;
            //if email is not ones of student
            if (c) {
                //login with the entered mail and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task) {
                                        //successful login
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            Intent intent
                                                    = new Intent(facLog.this,
                                                    gencode.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // sign-in failed
                                            Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
            } else {
                Toast.makeText(getApplicationContext(), "invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //to register as a new user
    public void signupfac(View view) {
        Intent intent=new Intent(facLog.this,facReg.class);
        startActivity(intent);
    }

}

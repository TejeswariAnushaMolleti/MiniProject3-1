package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.firstapp.R.string.invalid_class;
import static com.example.firstapp.R.string.invalid_confirm_password;
import static com.example.firstapp.R.string.invalid_email;
import static com.example.firstapp.R.string.invalid_name;
import static com.example.firstapp.R.string.invalid_password;

public class Main4Activity extends AppCompatActivity {

    EditText etName,etnumber,etclass,etemail,etpassword,etconfirmpassword;
    Button btsubmit;
    private FirebaseAuth mAuth;                                                //create firebase authentication instance
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //get instance of authentication
        mAuth=FirebaseAuth.getInstance();

        //hide action bar
        getSupportActionBar().hide();

        //relate variables with the widgets used in xml layout
        etName=findViewById(R.id.et_name);
        etnumber=findViewById(R.id.et_id);
        etclass=findViewById(R.id.et_class);
        etemail=findViewById(R.id.et_email);
        etpassword=findViewById(R.id.et_password);
        etconfirmpassword=findViewById(R.id.et_confirmpassword);
        btsubmit=findViewById(R.id.et_button);

        //assign variables validation starts
        awesomeValidation =new AwesomeValidation(ValidationStyle.BASIC);

        //validation for name
        awesomeValidation.addValidation(this,R.id.et_name,
                RegexTemplate.NOT_EMPTY, invalid_name);

        //validation for id number
        awesomeValidation.addValidation(this,R.id.et_id,
                ".{7}",R.string.invalid_id);

        //validation for class
        awesomeValidation.addValidation(this,R.id.et_class,
                RegexTemplate.NOT_EMPTY, invalid_class);

        //validation for password
        awesomeValidation.addValidation(this,R.id.et_password,
                ".{6,}", invalid_password);

        //validation for confirm password
        awesomeValidation.addValidation(this,R.id.et_confirmpassword,
                R.id.et_password, invalid_confirm_password);

        //adding listener for submit button to register a user
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if validation success
                if(awesomeValidation.validate()){

                    //method calling to register a new user.
                    registerNewUser();
                   }
                //if validation is failed
                else {
                    Toast.makeText(getApplicationContext()
                            ,"check your details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //method definition to register a new user
    private void registerNewUser() {
        final String name, email, password, id, classs;
        name = etName.getText().toString();
        id = etnumber.getText().toString();
        classs = etclass.getText().toString();
        email = etemail.getText().toString();
        password = etpassword.getText().toString();

        //if email field is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if password is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Main4Activity.this,
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //if email is entered to check if student mail or not
        if (email != null) {
            String d = "";
            Integer i = 0;
            for (i = 1; i <= 6; i++) {
                d += email.charAt(i);
            }
            boolean b = d != null && d.matches("[0-9]+");
            //if student mail is entered
            if (b) {
                //create user with the entered mail and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Main4Activity.this,
                                        "create user with email and pass",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                //if user registered successfully
                                if (task.isSuccessful()) {
                                    Toast.makeText(Main4Activity.this,
                                            "Registration successful!",
                                            Toast.LENGTH_SHORT)
                                            .show();

                                    //method to store data of users in realtime database
                                    storedataof(name, email, id, classs, password);
                                }
                                //if registration failed
                                else {
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
                        //if failed display what is the error
                        Toast.makeText(Main4Activity.this, "error" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                //if the entered mail doesn't belong to student
                Toast.makeText(getApplicationContext(),"invalid email",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method definition of storing data of users
    private void storedataof(String name,String email,String id,String classs,String password) {

        //url of the firebase database
        String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";
        //get instance of that firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(url);

        //create a hash to put all the data if user entered into one variable
        HashMap<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("id number",id);
        map.put("class",classs);
        map.put("passcode",password);

        //to set the data into database in the path "students"
        FirebaseDatabase.getInstance(url).getReference("students").child(id)
                            .setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("hikj", "onComplete");
                                    Intent i=new Intent(Main4Activity.this,Main3Activity.class);                                                        //getParent();
                                    startActivity(i);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("hikj", "onFailure" + e.toString());
                Toast.makeText(Main4Activity.this,"error"+e,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("hikj", "On Success" );
            }
        });
    }

    //if already have existing account
    public void sign(View view) {
        Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
        startActivity(intent);
    }
}

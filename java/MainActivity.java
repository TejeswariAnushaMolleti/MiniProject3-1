package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //define the types of variables to be used
    private Timer splashTimer;
    private static final long DELAY=2650;
    private boolean scheduled=false;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        setContentView(R.layout.activity_main);

        //get instance of firebase
        mFirebaseAuth=FirebaseAuth.getInstance();
        //get current user by firebase
        final FirebaseUser mfirebaseuser= mFirebaseAuth.getCurrentUser();

        //to hide action bar
        getSupportActionBar().hide();
        //set timer to the variable splashtimer
        splashTimer=new Timer();
        //assign task to splashtimer
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mfirebaseuser != null) {
                    //get email of the logged in user
                    String g = mfirebaseuser.getEmail();
                    String d="";
                    Integer i=0;
                    //code to know if user is student or faculyt
                    for(i=1;i<=6;i++) {
                        d += g.charAt(i);
                    }
                        boolean b=d!= null && d.matches("[0-9.]+");
                        if(b){
                            //if user is student then show enter code layout
                            Intent intent = new Intent(getApplicationContext(), entercode.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            //else show generate code layout
                            Intent intent = new Intent(getApplicationContext(), gencode.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                else {
                    //if no user is logged in just display the home screen
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
            //set the delay for the timer
        },DELAY);
        //change boolean to true for the code to execute after the delay
        scheduled=true;
    }
}

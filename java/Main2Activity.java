package com.example.firstapp;
//imports used in this layout
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main2);

        //hide action bar
        getSupportActionBar().hide();
    }

    //code to go to student log in screen
    public void stu(View view) {
        Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
        startActivity(intent);
        finish();
    }

    //code to go to faculty log in screen
    public void fac(View view) {
        Intent intent=new Intent(Main2Activity.this,facLog.class);
        startActivity(intent);
        finish();
    }
}

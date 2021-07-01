package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class addstu extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    EditText editText1,editText2,editText3,codetoadd;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstu);

        getSupportActionBar().hide();

        editText1=findViewById(R.id.et1);
        editText2=findViewById(R.id.et2);
        editText3=findViewById(R.id.et3);
        codetoadd=findViewById(R.id.codetoadd);
        button=findViewById(R.id.addstud);

        Intent i=getIntent();
        final String codegen=i.getStringExtra("codeg");
        Toast.makeText(getApplicationContext(),codegen,Toast.LENGTH_SHORT).show();
        codetoadd.setText(codegen);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String codetostu = codetoadd.getText().toString().trim();
                    final String stu1 = editText1.getText().toString().trim();
                    final String stu2 = editText2.getText().toString().trim();
                    final String stu3 = editText3.getText().toString().trim();

                    if (codetostu.length() == 6) {

                        final String url = "https://firstapp-70022-default-rtdb.asia-southeast1.firebasedatabase.app/";

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                        String cdt = sdf.format(new Date());

                        final HashMap<String, Object> map = new HashMap<>();
                        map.put("time", cdt);

                        Query checkcode = FirebaseDatabase.getInstance(url).getReference("k8");//.child(codetostu);
                        checkcode.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(codetostu)) {
                                    if (stu1.length() == 7) {

                                        FirebaseDatabase.getInstance(url).getReference("k8/" + codetostu).child(stu1)
                                                .setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.i("hijk", "Oncomplete");
                                                    }
                                                });
                                    }
                                    if (stu2.length() == 7) {

                                        FirebaseDatabase.getInstance(url).getReference("k8/" + codetostu).child(stu2)
                                                .setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.i("hijk", "Oncomplete");
                                                    }
                                                });
                                    }
                                    if (stu3.length() == 7) {

                                        FirebaseDatabase.getInstance(url).getReference("k8/" + codetostu).child(stu3)
                                                .setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.i("hijk", "Oncomplete");
                                                    }
                                                });
                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"invalid code",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),(CharSequence) e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
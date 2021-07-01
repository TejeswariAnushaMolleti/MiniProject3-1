package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class percentage extends AppCompatActivity {
    //define the variables to be used with the type
    TextView tv1,tv2;
    AnyChartView anyChartView;
    String[] attendance={"attended","not attended"};
    int[] percent=new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage);
        //hide action bar
        getSupportActionBar().hide();

        //get access from the items used in the xml file
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        anyChartView=findViewById(R.id.any_chart);

        //get the value from the before layout
        Intent i=getIntent();
        String ATTPER=i.getStringExtra("tot");
        String idnum=i.getStringExtra("idnum");

        Integer ATPER=Integer.valueOf(ATTPER);
        Integer REMPER=100-(ATPER);

        //set the values to the pie-chart
        try {
            if(ATPER<65){
                tv1.setText("Manage your attendance");
            }
            percent[0] = ATPER;
            percent[1] = REMPER;

            Pie pie = AnyChart.pie();
            List<DataEntry> dataEntries = new ArrayList<>();

            for (int j = 0; j < percent.length; j++) {
                dataEntries.add(new ValueDataEntry(attendance[j], percent[j]));
            }
            pie.data(dataEntries);
            anyChartView.setChart(pie);
            tv2.setText(idnum);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"error"+ e,Toast.LENGTH_SHORT).show();
        }
    }
}
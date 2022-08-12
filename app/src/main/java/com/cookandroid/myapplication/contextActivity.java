package com.cookandroid.myapplication;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class contextActivity extends AppCompatActivity {

     @Override protected void onCreate(Bundle savedInstaceState){
         super.onCreate(savedInstaceState);
         setContentView(R.layout.context);

         TextView titleSet = (TextView)findViewById(R.id.titleSet);
         TextView contextSet= (TextView)findViewById(R.id.contextSet);
         TextView namic = findViewById(R.id.nameContext);
         TextView times =findViewById(R.id.times);

         Intent inIntent = getIntent();
         titleSet.setText(inIntent.getStringExtra("title"));
         contextSet.setText(inIntent.getStringExtra("contexts"));
         times.setText(inIntent.getStringExtra("time"));

         Button buttonreturns = findViewById(R.id.backContext);
         buttonreturns.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
                 finish();
             }
         });
     }

}

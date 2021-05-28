package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Done extends AppCompatActivity {

   String placeName,placeD,placePhoto;
   int x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        Intent gi=getIntent();
        placeName=gi.getStringExtra("placeName");
        placeD=gi.getStringExtra("placeD");
        placePhoto=gi.getStringExtra("placePhoto");
        x=gi.getIntExtra("placeX",-1);
        x=gi.getIntExtra("placeY",-1);

    }

    public void info(View view) {
        Intent si = new Intent(Done.this,placeInfoShowing.class);
        si.putExtra("placeName",placeName);
        si.putExtra("placePhoto",placePhoto);
        si.putExtra("placeD",placeD);
        si.putExtra("placeX",x);
        si.putExtra("placeY",y);
        startActivity(si);
    }

    public void back(View view) {
        Intent si = new Intent(Done.this,choosing.class);
        startActivity(si);
    }
}

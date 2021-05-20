package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refMaps;

public class Done extends AppCompatActivity {

   String placeName,placeD,placePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        Intent gi=getIntent();
        placeName=gi.getStringExtra("placeName");
        placeD=gi.getStringExtra("placeD");
        placePhoto=gi.getStringExtra("placePhoto");

    }

    public void info(View view) {
        Intent si = new Intent(Done.this,PlaceInfo.class);
        si.putExtra("placeName",placeName);
        si.putExtra("placePhoto",placePhoto);
        si.putExtra("placeD",placeD);
        startActivity(si);
    }

    public void back(View view) {
        Intent si = new Intent(Done.this,choosing.class);
        startActivity(si);
    }
}

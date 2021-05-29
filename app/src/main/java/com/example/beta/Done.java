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


    }

    /**
     * This method sends the user back to the choosing acticity
     * @param view
     */
    public void back(View view) {
        Intent si = new Intent(Done.this, choosing.class);
        startActivity(si);
    }
}

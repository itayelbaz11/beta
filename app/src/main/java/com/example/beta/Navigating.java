package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import static com.example.beta.FBref.refStor;

public class Navigating extends AppCompatActivity {

    ImageView ivMap;
    TextView tvSteps,tvDirection;

    String mapId,nameStart,nameEnd;
    int xStart,yStart,xEnd,yEnd;

    Bitmap bMap;
    Spot[][] grid;
    boolean wall=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigating);

        ivMap=(ImageView) findViewById(R.id.ivMapN);
        tvSteps=(TextView) findViewById(R.id.tvStepsN);
        tvDirection=(TextView) findViewById(R.id.tvDirectionN);

        Intent gi=getIntent();
        mapId=gi.getStringExtra("mapId");
        nameStart=gi.getStringExtra("placeNamestart");
        nameEnd=gi.getStringExtra("placeNameEnd");
        xStart=gi.getIntExtra("placeXstart",-1);
        xEnd=gi.getIntExtra("placeXend",-1);
        yStart=gi.getIntExtra("placeYstart",-1);
        yEnd=gi.getIntExtra("placeYend",-1);

        StorageReference refImages=refStor.child(mapId+".jpg");
        final long MAX_SIZE = 1024*1024;
        refImages.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivMap.setImageBitmap(bMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        for(int i=0;i<bMap.getWidth();i++){
            for(int j=0;j<bMap.getHeight();j++){
                 if(bMap.getPixel(i,j)==Color.rgb(1,60,160)){
                     wall=true;
                 }
                 grid[i][j]=new Spot(i,j,wall);
                 wall=false;
            }
        }

    }

    public void startW(View view) {
    }

    public void stopW(View view) {
    }
}

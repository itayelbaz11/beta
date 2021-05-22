package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.example.beta.FBref.refMaps;
import static com.example.beta.FBref.refStor;

public class CreatingPath extends AppCompatActivity implements SensorEventListener {

    ImageView mapIV;

    Map map;
    String mapId;
    int xS,yS;

    private SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnometer;
    Sensor mStepCounter;
    boolean isCounterSensorPresent;
    float[] mGravity,mGeomagnetic;
    float azimuth,rotation;
    int x,y,z;


    Bitmap bMap,savedmap;

    boolean startwalkingtrue=false;
    int steps=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_path);

        mapIV = (ImageView) findViewById(R.id.mapIV);

        Intent gi = getIntent();
        xS = gi.getIntExtra("x", -1);
        yS = gi.getIntExtra("y", -1);
        mapId = gi.getStringExtra("mapId");

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(CreatingPath.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(CreatingPath.this,magnometer,SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent=true;
        }
        else{
            isCounterSensorPresent=false;
        }

        StorageReference refImages=refStor.child(mapId+".jpg");
        final long MAX_SIZE = 1024*1024;
        refImages.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                savedmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mapIV.setImageBitmap(bMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_STEP_COUNTER){

            if(sensorEvent.sensor==mStepCounter){
                if(startwalkingtrue) {
                    steps++;
                    if (steps == 3) {
                        steps = 0;
                        if (rotation >= 22.5 && rotation <= 67.5) {
                            for (int i = 0; i < 2; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS++;
                                yS--;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation >= 112.5 && rotation <= 157.5) {
                            for (int i = 0; i < 2; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS++;
                                yS++;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation >= 202.5 && rotation <= 247.5) {
                            for (int i = 0; i < 2; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS--;
                                yS++;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation >= 292.5 && rotation <= 337.5) {
                            for (int i = 0; i < 2; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS--;
                                yS--;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation > 67.5 && rotation < 112.5) {
                            for (int i = 0; i < 3; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS++;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }

                        else if (rotation > 157.5 && rotation < 202.5) {
                            for (int i = 0; i < 3; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                yS++;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation > 247.5 && rotation < 292.5) {
                            for (int i = 0; i < 3; i++) {
                                if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                xS--;
                                bMap.setPixel(xS, yS, Color.WHITE);}
                            }
                        }
                        else if (rotation > 337.5 || rotation < 22.5) {
                            for (int i = 0; i < 3; i++) {
                                if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
                                    yS--;
                                    bMap.setPixel(xS, yS, Color.WHITE);
                                }
                            }
                        }
                        mapIV.setImageBitmap(bMap);
                    }
                }
            }}

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = sensorEvent.values;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = sensorEvent.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimuth = orientation[0];
                rotation = (float) (( Math.toDegrees( azimuth ) + 360 ) % 360);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public boolean dontexceed(int x,int y,int xMax,int yMax){
        if((x<xMax)&&(x<=0)&&(y<yMax)&&(x<=0)){
            return true;
        }
        return false;
    }

    public void startWalking(View view) {
        startwalkingtrue=true;
    }

    public void cancel(View view) {
        startwalkingtrue=false;
        bMap=savedmap.copy(savedmap.getConfig(),true);
        mapIV.setImageBitmap(bMap);
    }

    public void pin(View view) {
        StorageReference refImages=refStor.child(mapId+".jpg");
        refImages.delete();

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bMap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data=baos.toByteArray();

        /**
         * uploading the compressed bitmap
         */
        UploadTask uploadTask =refImages.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CreatingPath.this, "There was a problem", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

        Intent si = new Intent(CreatingPath.this,AddingPlace.class);
        si.putExtra("mapId",mapId);
        si.putExtra("x",xS);
        si.putExtra("y",yS);
        si.putExtra("firstTime",false);
        startActivity(si);
    }



}

package com.example.beta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    boolean usersPermission=false;
    int steps=0;

    AlertDialog.Builder adb;

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
        //accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensorManager.registerListener(CreatingPath.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        //magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //sensorManager.registerListener(CreatingPath.this,magnometer,SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(mStepCounter!=null) {
            Toast.makeText(this, "step counter found", Toast.LENGTH_SHORT);
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            Toast.makeText(this, "No step counter!!!", Toast.LENGTH_SHORT);
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

        startwalkingtrue=false;



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //if (sensorEvent.sensor.getType() != Sensor.TYPE_STEP_COUNTER) {return;}
//        public static final int TYPE_ORIENTATION = 3;
//        public static final int TYPE_POSE_6DOF = 28;
//        public static final int TYPE_PRESSURE = 6;
//        public static final int TYPE_PROXIMITY = 8;
//        public static final int TYPE_RELATIVE_HUMIDITY = 12;
//        public static final int TYPE_ROTATION_VECTOR = 11;
//        public static final int TYPE_SIGNIFICANT_MOTION = 17;
//        public static final int TYPE_STATIONARY_DETECT = 29;
//        public static final int TYPE_STEP_COUNTER = 19;
//        public static final int TYPE_STEP_DETECTOR = 18;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            Log.d("myapp1", "Sensor is: STEP COUNTER");
            Toast.makeText(this, "step counter changed", Toast.LENGTH_SHORT);
        }
        else {
            Log.d("myapp1", "Another Sensor: " + Sensor.TYPE_STEP_COUNTER);
            Toast.makeText(this, "another sensor changed", Toast.LENGTH_SHORT);
        }

        return;

//        if(startwalkingtrue) {
//                if (sensorEvent.sensor == mStepCounter) {
//                        steps++;
//                        if (steps == 3) {
//                            steps = 0;
//                            if (rotation >= 22.5 && rotation <= 67.5) {
//                                 for (int i = 0; i < 2; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS++;
//                                        yS--;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation >= 112.5 && rotation <= 157.5) {
//                                for (int i = 0; i < 2; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS++;
//                                        yS++;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation >= 202.5 && rotation <= 247.5) {
//                                for (int i = 0; i < 2; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS--;
//                                        yS++;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation >= 292.5 && rotation <= 337.5) {
//                                for (int i = 0; i < 2; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS--;
//                                        yS--;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation > 67.5 && rotation < 112.5) {
//                                for (int i = 0; i < 3; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS++;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation > 157.5 && rotation < 202.5) {
//                                for (int i = 0; i < 3; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        yS++;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation > 247.5 && rotation < 292.5) {
//                                for (int i = 0; i < 3; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        xS--;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            } else if (rotation > 337.5 || rotation < 22.5) {
//                                for (int i = 0; i < 3; i++) {
//                                    if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
//                                        yS--;
//                                        bMap.setPixel(xS, yS, Color.WHITE);
//                                    }
//                                }
//                            }
//                             mapIV.setImageBitmap(bMap);
//                        }
//                    }
//                }

        }
    /*    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
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
        }*/



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
        if(usersPermission){
            startwalkingtrue=true;
        }
        else{
            adb=new AlertDialog.Builder(this);
            adb.setTitle("Users Permission");
            adb.setMessage("The app is about to track your walking, do you agree?");
            adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    usersPermission=true;
                    startwalkingtrue=true;
                    dialogInterface.cancel();
                }
            });
            adb.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog ad=adb.create();
            ad.show();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this,mStepCounter);
        }
    }



}

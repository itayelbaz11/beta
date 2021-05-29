package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beta.activities.AddingPlace;
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



    Bitmap bMap,savedmap,bMap2;

    boolean startwalkingtrue=false;
    boolean usersPermission=false;
    boolean downladpic=false;
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
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(CreatingPath.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(CreatingPath.this,magnometer,SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);


      /*  if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent=true;
        }
        else{
            isCounterSensorPresent=false;
        }
       */
        try {
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if(mStepCounter!=null) {
                Toast.makeText(this, "step counter found", Toast.LENGTH_SHORT);
                sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else
            {
                Toast.makeText(this, "No step counter!!!", Toast.LENGTH_SHORT);
            }
        }
        catch(Exception ex){
         Log.d("Debug","Exception:"+ ex.toString());
        }


        final ProgressDialog pd=ProgressDialog.show(this,"PIC","Downloading map.... ",true);
        StorageReference refImages=refStor.child(mapId+".jpg");
        final long MAX_SIZE = 2500*2500;
        refImages.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bMap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bMap=bMap2.copy(bMap2.getConfig(),true);
                savedmap= bMap2.copy(bMap2.getConfig(),true);
                mapIV.setImageBitmap(bMap);
                downladpic=true;
                startwalkingtrue=false;
               // bMap=createImage(1000,1000,Color.BLACK);
               // mapIV.setImageBitmap(bMap);
                pd.dismiss();
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
        if (downladpic) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                if (startwalkingtrue) {
                    if (sensorEvent.sensor == mStepCounter) {
                        steps++;
                        if (steps == 3) {
                            steps = 0;
                            if (rotation >= 22.5 && rotation <= 67.5) {
                         //       if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())){
                                for (int i = 0; i < 2; i++) {
                                        xS++;
                                        yS--;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                        //        }
                                }
                            } else if (rotation >= 112.5 && rotation <= 157.5) {
                        //        if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 2; i++) {
                                        xS++;
                                        yS++;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                       //         }
                            } else if (rotation >= 202.5 && rotation <= 247.5) {
                       //         if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 2; i++) {
                                        xS--;
                                        yS++;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                          //      }
                            } else if (rotation >= 292.5 && rotation <= 337.5) {
                            //    if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 2; i++) {
                                        xS--;
                                        yS--;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                          //      }
                            } else if (rotation > 67.5 && rotation < 112.5) {
                        //        if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 3; i++) {
                                        xS++;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                          //      }
                            } else if (rotation > 157.5 && rotation < 202.5) {
                        //        if (dontexceed(xS, yS, bMap.getWidth(), bMap.getHeight())) {
                                    for (int i = 0; i < 3; i++) {
                                        yS++;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                          //      }
                            } else if (rotation > 247.5 && rotation < 292.5) {
                         //       if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 3; i++) {
                                        xS--;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                          //      }
                            } else if (rotation > 337.5 || rotation < 22.5) {
                         //       if(dontexceed(xS,yS,bMap.getWidth(),bMap.getHeight())) {
                                    for (int i = 0; i < 3; i++) {
                                        yS--;
                                        bMap.setPixel(xS, yS, Color.WHITE);
                                    }
                        //        }
                            }
                            mapIV.setImageBitmap(bMap);
                        }
                    }
                }
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = sensorEvent.values;

            else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = sensorEvent.values;

            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];

                if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                    // orientation contains azimut, pitch and roll
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    azimuth = orientation[0];
                    rotation = (float) ((Math.toDegrees(azimuth) + 360) % 360);
                }
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
        usersPermission=false;
        bMap=savedmap.copy(savedmap.getConfig(),true);
        mapIV.setImageBitmap(bMap);
    }

    public void pin(View view) {
        if(startwalkingtrue) {
            StorageReference refImages = refStor.child(mapId + ".jpg");
            refImages.delete();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            /**
             * uploading the compressed bitmap
             */
            UploadTask uploadTask = refImages.putBytes(data);
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

            Intent si = new Intent(CreatingPath.this, AddingPlace.class);
            si.putExtra("mapId", mapId);
            si.putExtra("x", xS);
            si.putExtra("y", yS);
            si.putExtra("firstTime", false);
            startActivity(si);
        }
        else{
            Toast.makeText(this, "start walking", Toast.LENGTH_SHORT);
        }
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


    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

}

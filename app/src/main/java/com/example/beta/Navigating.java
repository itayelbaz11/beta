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
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Stack;

import static com.example.beta.FBref.refStor;

public class Navigating extends AppCompatActivity implements SensorEventListener {

    final int N=0;
    final int NE=1;
    final int E=2;
    final int SE=3;
    final int S=4;
    final int SW=5;
    final int W=6;
    final int NW=7;


    ImageView ivMap;
    TextView tvSteps,tvDirection,tvCurrentDirection;

    String mapId,nameStart,nameEnd,endPhoto,endD;
    int xStart,yStart,xEnd,yEnd;

    Bitmap bMap;
    Spot[][] grid;
    boolean wall=false;
    Spot start,end;
    Stack<Spot> Spath;
    Stack<Vector> pathV;

    boolean stepsB=false;
    boolean downladed=false;
    boolean usersPermission=false;

    private SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnometer;

    Bitmap bitmap,bmp;

    Sensor mStepCounter;
    boolean isCounterSensorPresent;
    int stepCount=0;

    float[] mGravity,mGeomagnetic;
    float azimuth,rotation;

    boolean isWalking=false;
    Vector tmpV;

    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigating);

        ivMap=(ImageView) findViewById(R.id.ivMapN);
        tvSteps=(TextView) findViewById(R.id.tvStepsN);
        tvDirection=(TextView) findViewById(R.id.tvDirectionN);
        tvCurrentDirection=(TextView) findViewById(R.id.tvCurrentDirectionN);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Navigating.this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(Navigating.this,magnometer,SensorManager.SENSOR_DELAY_FASTEST);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent=true;
        }
        else{
            isCounterSensorPresent=false;
        }

        Intent gi=getIntent();
        mapId=gi.getStringExtra("mapId");
        nameStart=gi.getStringExtra("placeNamestart");
        nameEnd=gi.getStringExtra("placeNameEnd");
        xStart=gi.getIntExtra("placeXstart",-1);
        xEnd=gi.getIntExtra("placeXend",-1);
        yStart=gi.getIntExtra("placeYstart",-1);
        yEnd=gi.getIntExtra("placeYend",-1);
        endD=gi.getStringExtra("placeD");
        endPhoto=gi.getStringExtra("placePhoto");


        final ProgressDialog pd=ProgressDialog.show(this,"PIC","Downloading map and creating instructions.... ",true);
        StorageReference refImages=refStor.child(mapId+".jpg");
        final long MAX_SIZE = 2500*2500;
        refImages.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivMap.setImageBitmap(bMap);
                grid=new Spot[bMap.getWidth()][bMap.getHeight()];
                for(int i=0;i<bMap.getWidth();i++){
                    for(int j=0;j<bMap.getHeight();j++){
                        if(bMap.getPixel(i,j)==Color.rgb(1,60,160)){
                            wall=true;
                        }
                        grid[i][j]=new Spot(i,j,wall);
                        wall=false;
                    }
                }
                start=grid[xStart][yStart];
                end=grid[xEnd][yEnd];
                Spath=pathFinding(start,end,grid);
                pathV=vectorPath(Spath);
                downladed=true;
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });







    }

    public void startW(View view) {
        if(usersPermission){
            isWalking=true;
        }
        else{
            adb=new AlertDialog.Builder(this);
            adb.setTitle("Users Permission");
            adb.setMessage("The app is about to track your walking, do you agree?");
            adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    usersPermission=true;
                    isWalking=true;
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

    public void stopW(View view) {

       // isWalking=false;
       // usersPermission=false;
    }

    public Stack<Spot> pathFinding(Spot start, Spot end, Spot[][] grid){
        Stack<Spot> path=new Stack<Spot>();
        ArrayList<Spot> openSet=new ArrayList<Spot>();
        ArrayList<Spot> closedSet=new ArrayList<Spot>();
        boolean nosolution=false;
        boolean keepgoing=true;
        int winner=0;

        openSet.add(start);
        while(keepgoing){
            winner=0;
            if(!openSet.isEmpty()){
                for(int x=0;x<openSet.size();x++){
                    if(openSet.get(x).getF()<openSet.get(winner).getF()){
                        winner=x;
                    }
                }
                Spot current=openSet.get(winner);
                if(current==end){
                    Spot tempS=current;
                    int e=0;
                    while (tempS != null) {
                        e++;
                        path.push(tempS);
                        tempS = tempS.previous;
                        //
                    }
                    keepgoing=false;
                    //done!!
                }
                openSet.remove(current);
                closedSet.add(current);

                current.addNeighbors(grid);
                ArrayList<Spot> neighbors=current.neighbors;

                for(int i3=0;i3<neighbors.size();i3++){
                    Spot neighbor=neighbors.get(i3);
                    if(!closedSet.contains(neighbor)&&(!neighbor.wall)){
                        double x=1;
                        if (Math.abs(current.i-neighbor.i)+Math.abs(current.j-neighbor.j)==2){
                            x=Math.pow(2,1/2);
                        }
                        double tempG=current.g+x;
                        if(openSet.contains(neighbor)){
                            if(tempG<neighbor.g){
                                neighbor.g=tempG;
                            }

                        }
                        else {
                            neighbor.g=tempG;
                            openSet.add(neighbor);
                        }

                        neighbor.h=heuristic(neighbor,end);
                        neighbor.f=neighbor.g+neighbor.h;
                        neighbor.previous=current;

                    }


                }
                //we can keep going
            }
            else {
                Toast.makeText(this, "no solution", Toast.LENGTH_SHORT).show();
                nosolution=true;
                keepgoing=false;
                //no solution
            }

        }
        return path;
    }

    public double heuristic(Spot n,Spot end){
        return Math.sqrt(Math.pow((n.i-end.i),2)+Math.pow((n.j-end.j),2));
    }

    public Stack<Vector> vectorPath(Stack<Spot> path){
        Stack vPath=new Stack<Vector>();
        Spot current=path.pop(),peek=path.peek();
        int currentD=getDirection(current.i,current.j,peek.i,peek.j);
        int temp;
        int steps=1;
        while (!path.isEmpty()){
            current=path.pop();
            if (!path.isEmpty()){
                peek=path.peek();
                temp=getDirection(current.i,current.j,peek.i,peek.j);
                if(temp!=currentD){
                    vPath.add(new Vector(currentD,steps));
                    currentD=temp;
                    steps=1;
                }
                else {
                    steps++;
                }
            }
            else{
                vPath.add(new Vector(currentD,steps));
            }
        }
        return reverseS(vPath);
    }

    /**
     * this method gets two spot's coordinations and returns the relvative direction.
     * @param x0
     * @param y0
     * @param xE
     * @param yE
     * @return
     */
    public int getDirection(int x0,int y0,int xE,int yE){
        int dX=xE-x0;
        int dY=yE-y0;
        int direction=20;
        switch (dX){
            case 1: {
                switch (dY){
                    case 1: direction=SE;break;
                    case 0: direction=E; break;
                    case -1: direction=NE;break;
                }
            }
            break;
            case 0:{
                switch (dY){
                    case 1: direction= S;break;
                    case -1: direction=N;break;
                }

            }
            break;
            case -1:{
                switch (dY){
                    case 1: direction= SW;break;
                    case 0: direction= W; break;
                    case -1: direction=NW;break;
                }
            }
            break;
        }
        return direction;
    }

    /**
     * this method reverses a Stack.
     * @param s1
     * @return
     */
    public Stack<Spot> reverseS(Stack<Spot> s1){
        Stack<Spot> s2=new Stack<Spot>();
        while(!s1.isEmpty()){
            s2.push(s1.pop());
        }
        return s2;
    }


    public int directionGet(double rotation){
        int direction=0;
        if(rotation>=22.5&&rotation<=67.5){
            direction=NE;
        }
        if(rotation>=112.5&&rotation<=157.5){
            direction=SE;
        }
        if(rotation>=202.5&&rotation<=247.5){
            direction=SW;
        }
        if(rotation>=292.5&&rotation<=337.5){
            direction=NW;
        }
        if(rotation>67.5&&rotation<112.5){
            direction=E;
        }
        if(rotation>157.5&&rotation<202.5){
            direction=S;
        }
        if(rotation>247.5&&rotation<292.5){
            direction=W;
        }
        if(rotation >337.5||rotation<22.5){
            direction=N;
        }
        return  direction;
    }

    public String directionName(int x){
        switch (x){
            case 0: return "N";
            case 1:return "NE";
            case 2:return "E";
            case 3: return "SE";
            case 4: return "S";
            case 5:return "SW";
            case 6: return "W";
            case 7: return "NW";
            default: return "Error";
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(downladed) {
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
                    rotation = (float) ((Math.toDegrees(azimuth) + 360) % 360);
                    tvCurrentDirection.setText("Current Direction"+directionName(directionGet(rotation)));
                }
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                if (sensorEvent.sensor == mStepCounter) {
                    if (isWalking) {
                        if (!pathV.isEmpty()) {
                            tmpV = pathV.peek();
                            if (tmpV.steps == 0) {
                                pathV.pop();
                            }
                            if (!pathV.isEmpty()) {
                                tmpV = pathV.peek();
                                tvSteps.setText("Steps:"+tmpV.steps);
                                tvDirection.setText("Direction:"+directionName(tmpV.direction));
                                int direction = directionGet(rotation);
                                tvCurrentDirection.setText("Current Direction:"+directionName(direction));
                                if (direction == tmpV.direction) {
                                    pathV.peek().steps--;
                                } else {
                                    if (direction == (tmpV.direction + 4) % 8) {
                                        tmpV.steps++;
                                    } else {
                                        pathV.push(new Vector((direction + 4) % 8, 1));
                                    }
                                }
                            }
                        } else {
                            Intent si = new Intent(Navigating.this, Done.class);
                            si.putExtra("placeName", nameEnd);
                            si.putExtra("placePhoto", endPhoto);
                            si.putExtra("placeD", endD);
                            si.putExtra("placeX", xEnd);
                            si.putExtra("placeY", yEnd);
                            startActivity(si);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_FASTEST);

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

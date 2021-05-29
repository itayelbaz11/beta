package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static com.example.beta.FBref.refStor;

public class placeInfoShowing extends AppCompatActivity {

    ImageView Iv;
    TextView NameTv,Xtv,Ytv,Dtv;
    String name,x,y,Description,photo;
    int xTmp,yTmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info_showing);

        NameTv=(TextView) findViewById(R.id.tvNameInfo);
        Xtv=(TextView) findViewById(R.id.tvInfoX);
        Ytv=(TextView) findViewById(R.id.tvInfoY);
        Dtv=(TextView) findViewById(R.id.DescriptionInfotv);
        Iv=(ImageView) findViewById(R.id.InfoIV);

        Intent gi=getIntent();
        name=gi.getStringExtra("placeName");
        Description=gi.getStringExtra("placeD");
        photo=gi.getStringExtra("placePhoto");
        xTmp=gi.getIntExtra("placeX",-1);
        yTmp=gi.getIntExtra("placeY",-1);

        if(xTmp!=-1&&yTmp!=-1){
            x=""+xTmp;
            y=""+yTmp;
            try {
                Download();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            finish();
        }
    }

    /**
     * Downloading the places image from the firebase storage
     * @throws IOException
     */
    public void Download() throws IOException {
        StorageReference refImages=refStor.child(photo+".jpg");
        final File localFile;
        localFile = File.createTempFile(photo,"jpg");
        refImages.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String filePath = localFile.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Iv.setImageBitmap(bitmap);
                NameTv.setText(name);
                Xtv.setText(x);
                Ytv.setText(y);
                Dtv.setText(Description);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(placeInfoShowing.this, "Image download failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This method sends the user back to the previous activity.
     * @param view
     */
    public void endAcivity(View view) {
        finish();
    }
}


/*
    StorageReference refImages=refStor.child(tmpP1.photo+".jpg");
    final File localFile= File.createTempFile(tmpP1.photo,"jpg");
            refImages.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
@Override
public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
        String filePath = localFile.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //  iV.setImageBitmap(bitmap);
        }
        }).addOnFailureListener(new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception exception) {
        Toast.makeText(StartingAndEnding.this, "Image download failed", Toast.LENGTH_LONG).show();
        }
        });*/
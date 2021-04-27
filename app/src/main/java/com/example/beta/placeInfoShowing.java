package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.storage.StorageReference;

import java.io.File;

import static com.example.beta.FBref.refStor;

public class placeInfoShowing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info_showing);
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
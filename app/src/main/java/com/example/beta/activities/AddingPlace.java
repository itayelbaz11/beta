package com.example.beta.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beta.CreatingPath;
import com.example.beta.Map;
import com.example.beta.Place;
import com.example.beta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.beta.FBref.refMaps;
import static com.example.beta.FBref.refStor;

public class AddingPlace extends AppCompatActivity {

    EditText nET,dET;
    ImageView picIv;

    String pName,pDescription,pURL;
    int pX,pY;
    String mapId;
    boolean firstTime;
    public Uri imguri;
    Map tmpMap;
    Place newPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_place);

        nET=(EditText) findViewById(R.id.placeNameET);
        dET=(EditText) findViewById(R.id.placeDescriptionET);
        picIv=(ImageView) findViewById(R.id.picIV);

        Intent gi=getIntent();
        pX=gi.getIntExtra("x",-1);
        pY=gi.getIntExtra("y",-1);
        mapId=gi.getStringExtra("mapId");
        firstTime=gi.getBooleanExtra("firstTime",true);
    }

    /**
     * This method import the image from the device's gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            imguri=data.getData();
            picIv.setImageURI(imguri);
        }
    }


    /**
     * This Method Chooses picture from gallery
     * @param view
     */
    public void uploadPic(View view) {
        Intent si=new Intent();
        si.setType("image/*");
        si.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(si,1);


    }


    /**
     * This method add a new Place object to the Map's places arraylist,
     * and it uploads the chosen image to the Firebase Storage
     * if its the first time (the previous activity was the NewPath activity, the app deletes the extra place (-1,-1)
     * @param view
     */
    public void createPlace(View view) {
        pName=nET.getText().toString();
        pDescription=dET.getText().toString();
        pURL=mapId+"X"+pName;
        newPlace=new Place(pX,pY,pName,pURL,pDescription);

        StorageReference refImagesp=refStor.child(pURL+".jpg");
        refImagesp.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(AddingPlace.this,"image uploaded successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

        Query query= refMaps.orderByChild("uid").equalTo(mapId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot d : snapshot.getChildren()) {
                    tmpMap = d.getValue(Map.class);
                }
                if(firstTime){
                    tmpMap.getPlaces().remove(0);
                }
                tmpMap.getPlaces().add(newPlace);
                refMaps.child(mapId).setValue(tmpMap);
                Intent si = new Intent(AddingPlace.this, CreatingPath.class);
                si.putExtra("mapId",mapId);
                si.putExtra("x",pX);
                si.putExtra("y",pY);
                startActivity(si);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }



}

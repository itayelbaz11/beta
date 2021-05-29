package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static com.example.beta.FBref.refMaps;
import static com.example.beta.FBref.refStor;

public class NewPath extends AppCompatActivity {

    EditText etPN;
    CheckBox publicCB,sCB,mCB,lCB;
    FirebaseUser user;

    String id;
    boolean newName=false;
    Map mTmp,newMap;
    int xy;

    Bitmap bPath;
    public Uri imguri;
    StorageReference mstorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_path);

        etPN=(EditText) findViewById(R.id.etPN);
        publicCB=(CheckBox) findViewById(R.id.publicCB);
        sCB=(CheckBox) findViewById(R.id.sCB);
        mCB=(CheckBox) findViewById(R.id.mCB);
        lCB=(CheckBox) findViewById(R.id.lCB);

        mstorageRef = FirebaseStorage.getInstance().getReference("Images");

        user= FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();

    }

    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }



    public void newPathName(View view) {
        boolean theresSize=false;

        String pathName=etPN.getText().toString();
        Random random=new Random();
        boolean publicc=publicCB.isChecked();

        if(sCB.isChecked()){
            xy=500;
            theresSize=true;
        }
        else{
            if (mCB.isChecked()){
                xy=1250;
                theresSize=true;
            }
            else{
                if(lCB.isChecked()){
                    xy=2500;
                    theresSize=true;
                }
            }
        }

        if(theresSize&&pathName!=null&&pathName!=""){

            String mapId=random.nextInt()+"";
            while(!newName) {
                Query query = refMaps.orderByChild("uid").equalTo(mapId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    //
                    @Override
                    public void onDataChange(DataSnapshot dS) {
                        mTmp=dS.getValue(Map.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                if (mTmp==null){
                    newName=true;
                }
                else{
                    mapId=random.nextInt()+"";
                }
            }

            bPath=createImage(xy,xy,Color.rgb(1,60,160));
            /**
             * references for the storage
             */
            StorageReference refImages=refStor.child(mapId+".jpg");
            StorageReference ImageImagesRef = refStor.child("images/"+mapId+".jpg");

            refImages.getName().equals(ImageImagesRef.getName());    // true
            refImages.getPath().equals(ImageImagesRef.getPath());    // false

            /**
             * compressing the bitmap
             */
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bPath.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data=baos.toByteArray();

            /**
             * uploading the compressed bitmap
             */
            UploadTask uploadTask =refImages.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(NewPath.this, "There was a problem", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });


            ArrayList<Place> places=new ArrayList<Place>();
            Place tmpP=new Place(-1,-1,"tmp","p","d");
            places.add(tmpP);
            newMap=new Map(mapId,pathName,id,places,publicc);
            refMaps.child(mapId).setValue(newMap);
            Intent si = new Intent(NewPath.this, AddingPlace.class);
            si.putExtra("mapId",mapId);
            si.putExtra("x",xy/2);
            si.putExtra("y",xy/2);
            si.putExtra("firstTime",true);
            startActivity(si);

        }
        else{
            Toast.makeText(NewPath.this, "Please insert map name and size", Toast.LENGTH_SHORT).show();
        }

    }


    public void sClick(View view) {
        mCB.setChecked(false);
        lCB.setChecked(false);
    }

    public void mClick(View view) {
        sCB.setChecked(false);
        lCB.setChecked(false);
    }

    public void lClick(View view) {
        sCB.setChecked(false);
        mCB.setChecked(false);
    }

}

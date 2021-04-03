package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import static com.example.beta.FBref.refMaps;

public class NewPath extends AppCompatActivity {

    EditText etPN;
    CheckBox publicCB,sCB,mCB,lCB;
    FirebaseUser user;
    String id;
    boolean newName=false;
    Map mTmp,newMap;
    int xy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_path);

        etPN=(EditText) findViewById(R.id.etPN);
        publicCB=(CheckBox) findViewById(R.id.publicCB);
        sCB=(CheckBox) findViewById(R.id.sCB);
        mCB=(CheckBox) findViewById(R.id.mCB);
        lCB=(CheckBox) findViewById(R.id.lCB);

        user= FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();

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

            ArrayList<ArrayList<Boolean>> path=new ArrayList<ArrayList<Boolean>>();
            reikPath(path,xy);
            ArrayList<Place> places=new ArrayList<Place>();
            newMap=new Map(mapId,pathName,id,path,places,publicc);
            refMaps.child(mapId).setValue(newMap);
        }
        else{
            Toast.makeText(NewPath.this, "Please insert map name and size", Toast.LENGTH_SHORT).show();
        }

    }

    public void reikPath(ArrayList<ArrayList<Boolean>> path,int size){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                path.add(new ArrayList<Boolean>());
                path.get(i).add(false);
            }
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

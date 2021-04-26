package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refMaps;

public class StartingAndEnding extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText startingET,endingET;
    ListView lvSt,lvEd;
    String st1,st2,mapid,StartName,EndName;
    Map map;
    ArrayList<Place> Plist=new ArrayList<Place>();
    ArrayList<String> PStList=new ArrayList<String>();
    ArrayList<Place> tmpPlist1=new ArrayList<Place>();
    ArrayList<String> tmpPStList1=new ArrayList<String>();
    ArrayList<Place> tmpPlist2=new ArrayList<Place>();
    ArrayList<String> tmpPStList2=new ArrayList<String>();
    ArrayAdapter adp,adp2;
    boolean isSearch1,isSearch2,ready1,ready2;
    int startingX,startingY,endingX,endingY;
    View view1,view2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_and_ending);

        startingET=(EditText) findViewById(R.id.startingET);
        endingET=(EditText) findViewById(R.id.endingET);

        lvSt = (ListView) findViewById(R.id.startingPoints);
        lvSt.setOnItemClickListener(this);
        lvSt.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvEd = (ListView) findViewById(R.id.endingPoints);
        lvEd.setOnItemClickListener(this);
        lvEd.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Intent gi=getIntent();
        mapid=gi.getStringExtra("map");

        Query query = refMaps.orderByChild("uid").equalTo(mapid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            //
            @Override
            public void onDataChange(DataSnapshot dS) {
                for (final DataSnapshot d : dS.getChildren()){
                    map=d.getValue(Map.class);
                }
                if(map.getPlaces()!=null){
                    Plist=map.getPlaces();
                    for(int i=0;i<Plist.size();i++){
                        PStList.add(Plist.get(i).name);
                    }
                }
                adp = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, PStList);
                lvSt.setAdapter(adp);
                adp2 = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, PStList);
                lvEd.setAdapter(adp2);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        isSearch1=false;
        isSearch2=false;
        ready1=false;
        ready2=false;

    }

    public void searchStart(View view) {
        String tmpSt=startingET.getText().toString();
        for (int i=0;i<Plist.size()-1;i++){
            if(PStList.get(i).indexOf(tmpSt)!=-1){
                tmpPlist1.add(Plist.get(i));
                tmpPStList1.add(PStList.get(i));
            }
        }
        adp = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, tmpPStList1);
        lvSt.setAdapter(adp);
        isSearch1=true;
    }

    public void infoStart(View view) {
    }

    public void searchEnd(View view) {
        String tmpSt=startingET.getText().toString();
        for (int i=0;i<Plist.size()-1;i++){
            if(PStList.get(i).indexOf(tmpSt)!=-1){
                tmpPlist2.add(Plist.get(i));
                tmpPStList2.add(PStList.get(i));
            }
        }
        adp2 = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, tmpPStList2);
        lvEd.setAdapter(adp2);
        isSearch2=true;
    }

    public void infoEnd(View view) {
    }

    public void startNavigating(View view) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            view.setBackgroundColor(Color.RED);
            switch(view.getId()) {
                case R.id.startingET:
                    if(isSearch1){
                         StartName=tmpPStList1.get(position);
                         startingX=tmpPlist1.get(position).getX();
                         startingY=tmpPlist1.get(position).getY();
                    }
                    else{
                        StartName=PStList.get(position);
                        startingX=Plist.get(position).getX();
                        startingY=Plist.get(position).getY();
                    }
                    view1=view;
                    ready1=true;
                    break;

                case R.id.endingET:
                    if(isSearch2){
                        StartName=tmpPStList2.get(position);
                        startingX=tmpPlist2.get(position).getX();
                        startingY=tmpPlist2.get(position).getY();
                    }
                    else{
                        StartName=PStList.get(position);
                        startingX=Plist.get(position).getX();
                        startingY=Plist.get(position).getY();
                    }
                    view2=view;
                    ready2=true;
                    break;

            }

    }

    public void cancelsearches(View view) {
        isSearch1=false;
        isSearch2=false;
        view1.setBackgroundColor(Color.rgb(227, 229, 250));
        view2.setBackgroundColor(Color.rgb(227, 229, 250));
        ready1=false;
        ready2=false;
        adp = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, PStList);
        lvSt.setAdapter(adp);
        adp2 = new ArrayAdapter<String>(StartingAndEnding.this,R.layout.support_simple_spinner_dropdown_item, PStList);
        lvEd.setAdapter(adp2);

    }
}

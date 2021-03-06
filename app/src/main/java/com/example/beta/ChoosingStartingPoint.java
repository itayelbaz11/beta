package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refMaps;

public class ChoosingStartingPoint extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lvSP;
    ArrayList<Place> Plist=new ArrayList<Place>();
    ArrayList<String> PStList=new ArrayList<String>();
    ArrayAdapter adp;
    String st,id,mapid;
    private FirebaseAuth mPDAuth;
    FirebaseUser user;
    Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_starting_point);

        lvSP = (ListView) findViewById(R.id.lvSP);
        lvSP.setOnItemClickListener(this);
        lvSP.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Intent gi=getIntent();
        mapid=gi.getStringExtra("map");

        /**
         * showing all of the places in the specific map that was chosen in the previous activity
         */

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
                adp = new ArrayAdapter<String>(ChoosingStartingPoint.this,R.layout.support_simple_spinner_dropdown_item, PStList);
                lvSP.setAdapter(adp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });





    }

    /**
     * This is activated when an item in the list view is clicked,
     * it sends the user to the next activity with the place's coordination (that was chosen)
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent si = new Intent(ChoosingStartingPoint.this,CreatingPath.class);
        si.putExtra("mapId",mapid);
        si.putExtra("x",Plist.get(position).getX());
        si.putExtra("y",Plist.get(position).getY());
        startActivity(si);

    }
}

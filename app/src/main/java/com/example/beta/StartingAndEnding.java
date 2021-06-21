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
        import android.widget.Toast;

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


    View view1,view2;
    Place tmpP1,tmpP2;



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

    /**
     * searhing a place by name in the starting's list view
     * @param view
     */
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

    /**
     * This method sends the user to the place info showing activity with the information on the chosen starting place
     * @param view
     */
    public void infoStart(View view){
        if(ready1){
            Intent si = new Intent(StartingAndEnding.this,placeInfoShowing.class);
            si.putExtra("placeName",tmpP1.getName());
            si.putExtra("placePhoto",tmpP1.getPhoto());
            si.putExtra("placeX",tmpP1.getX());
            si.putExtra("placeY",tmpP1.getY());
            si.putExtra("placeD",tmpP1.getDescription());
            startActivity(si);
        }
        else {
            Toast.makeText(StartingAndEnding.this, "Please choose an option", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * searhing a place by name in the ending's list view
     * @param view
     */
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

    /**
     * This method sends the user to the place info showing activity with the information on the chosen ending place
     * @param view
     */
    public void infoEnd(View view) {
        if(ready2){
            Intent si = new Intent(StartingAndEnding.this,placeInfoShowing.class);
            si.putExtra("placeName",tmpP2.getName());
            si.putExtra("placePhoto",tmpP2.getPhoto());
            si.putExtra("placeX",tmpP2.getX());
            si.putExtra("placeY",tmpP2.getY());
            si.putExtra("placeD",tmpP2.getDescription());
            startActivity(si);
        }
        else {
            Toast.makeText(StartingAndEnding.this, "Please choose an option", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method sends the user to the
     * @param view
     */
    public void startNavigating(View view) {
        if(ready1&&ready2){
            Intent si = new Intent(StartingAndEnding.this,Navigating.class);
            si.putExtra("mapId",mapid);
            si.putExtra("placeXstart",tmpP1.getX());
            si.putExtra("placeYstart",tmpP1.getY());
            si.putExtra("placeXend",tmpP2.getX());
            si.putExtra("placeYend",tmpP2.getY());
            si.putExtra("placeNamestart",tmpP1.getName());
            si.putExtra("placeNameEnd",tmpP2.getName());
            si.putExtra("placePhoto",tmpP2.getPhoto());
            si.putExtra("placeD",tmpP2.getDescription());
            startActivity(si);
        }
        else{
            Toast.makeText(StartingAndEnding.this, "Please choose places", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is activated when a list view item is clicked,
     * the clicked item in the specific list view that was clicked will be saved as one of the users choices, and the item will be colord in red
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        view.setBackgroundColor(Color.RED);
        if(adapterView==lvSt) {
            if (isSearch1) {
                tmpP1 = tmpPlist1.get(position);
            } else {
                tmpP1 = Plist.get(position);
            }
            if(view1!=null)
              view1.setBackgroundColor(Color.rgb(227, 229, 250));
            view1 = view;
            ready1 = true;
        }
        else{
            if(isSearch2){
                tmpP2=tmpPlist2.get(position);
            }
            else{
                tmpP2=Plist.get(position);
            }
            if(view2!=null)
               view2.setBackgroundColor(Color.rgb(227, 229, 250));
            view2=view;
            ready2=true;
        }

    }


    /**
     * This method will cancel the searches that had been clicked, and color the list view back to its original color
     * @param view
     */
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

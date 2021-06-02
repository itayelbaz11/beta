package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.List;

import static com.example.beta.FBref.refMaps;
import static com.example.beta.FBref.refUsers;

public class SearchingPath extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText pathnameET;
    ListView lvSP;
    ArrayList<Map> Mlist=new ArrayList<Map>(); //רשימת עצמי מפה
    ArrayList<String> MStList=new ArrayList<String>();//רשימת תצוגת שמות מפות
    ArrayAdapter adp;
    String st,st2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_path);

        pathnameET=(EditText) findViewById(R.id.pathnameET);
        lvSP = (ListView) findViewById(R.id.lvSPN);
        lvSP.setOnItemClickListener(this);
        lvSP.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void fetchData( final List<DataSnapshot> dataSnapshotList) {
        if(dataSnapshotList.isEmpty()) {
            adp = new ArrayAdapter<String>(SearchingPath.this,R.layout.support_simple_spinner_dropdown_item, MStList);
            lvSP.setAdapter(adp);
        } else {
            DataSnapshot data = dataSnapshotList.remove(dataSnapshotList.size() - 1);

            Map mTmp = data.getValue(Map.class);
            if(mTmp.publicc){
                Mlist.add(mTmp);
                st = mTmp.getMapname();
                Query query2 = refUsers.orderByChild("uid").equalTo(mTmp.getUidcreator());
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(final DataSnapshot d : snapshot.getChildren()) {
                            User albert = d.getValue(User.class);
                            st2 = albert.getName();
                            MStList.add(st+" by:"+st2);
                        }

                        fetchData(dataSnapshotList);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        }
    }

    /**
     * Searching paths by a name that is written in the searching line.
     * @param view
     */
    public void searchP(View view) {
        String pathname;
        pathname=pathnameET.getText().toString();
        Query query = refMaps.orderByChild("mapname").equalTo(pathname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dS) {
                MStList.clear();
                Mlist.clear();

                final List<DataSnapshot> dataSnapshotList = new ArrayList<>();
                for(DataSnapshot ds : dS.getChildren()) {
                    dataSnapshotList.add(ds);
                }

                fetchData(dataSnapshotList);
//                for(final DataSnapshot data : dS.getChildren()) {
//                   Map mTmp = data.getValue(Map.class);
//                   if(mTmp.publicc){
//                      Mlist.add(mTmp);
//                      st = mTmp.getMapname();
//                      Query query2 = refUsers.orderByChild("uid").equalTo(mTmp.getUidcreator());
//                      query2.addListenerForSingleValueEvent(new ValueEventListener() {
//                           @Override
//                           public void onDataChange(@NonNull DataSnapshot snapshot) {
//                               for(final DataSnapshot d : snapshot.getChildren()) {
//                                   User albert = d.getValue(User.class);
//                                   st2 = albert.getName();
//                                   MStList.add(st+" by:"+st2);
//                               }
//                           }
//                           @Override
//                           public void onCancelled(@NonNull DatabaseError error) { }
//                       });
//                   }
//                }
//                adp = new ArrayAdapter<String>(ChoosingGuideMap.this,R.layout.support_simple_spinner_dropdown_item, MStList);
//                lvCP.setAdapter(adp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    /**
     * When a map's name is clicked the user is sent to the choosing stating and ending points activity, this activity will send the aps id to the next activity
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent si = new Intent(SearchingPath.this,StartingAndEnding.class);
        si.putExtra("map",Mlist.get(position).getUid());
        startActivity(si);

    }
}

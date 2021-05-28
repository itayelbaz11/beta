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

    public void searchP(View view) {
        String pathname;
        pathname=pathnameET.getText().toString();
        Query query = refMaps.orderByChild("mapname").equalTo(pathname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            //
            @Override
            public void onDataChange(DataSnapshot dS) {
                MStList.clear();
                Mlist.clear();
                for(final DataSnapshot data : dS.getChildren()) {
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
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                        MStList.add(st+" by:"+st2);
                    }
                }
                adp = new ArrayAdapter<String>(SearchingPath.this,R.layout.support_simple_spinner_dropdown_item, MStList);
                lvSP.setAdapter(adp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent si = new Intent(SearchingPath.this,StartingAndEnding.class);
        si.putExtra("map",Mlist.get(position).getUid());
        startActivity(si);

    }
}

package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.beta.FBref.refMaps;
import static com.example.beta.FBref.refUsers;

public class ChoosingGuideMap extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lvCP;
    ArrayList<Map> Mlist=new ArrayList<Map>(); //רשימת עצמי מפה
    ArrayList<String> MStList=new ArrayList<String>();//רשימת תצוגת שמות מפות
    ArrayAdapter adp;
    String st,st2,id;
    FirebaseUser user;
    EditText et;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_guide_map);

        et=(EditText) findViewById(R.id.et);
        img=(ImageView) findViewById(R.id.img);

        lvCP = (ListView) findViewById(R.id.lvCP);
        lvCP.setOnItemClickListener(this);
        lvCP.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        user=FirebaseAuth.getInstance().getCurrentUser();
        id=user.getUid();



}

 /**
  * This method sends the user to the newpath activity when the button is clicked
  */
    public void newPath(View view) {
        Intent si = new Intent(ChoosingGuideMap.this,NewPath.class);
        startActivity(si);

    }

    /**
     * This method is started when selecting a list view item, it sends the user to the next activity and sends the chosen map
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent si = new Intent(ChoosingGuideMap.this,ChoosingStartingPoint.class);
        si.putExtra("map",Mlist.get(position).getUid());
        startActivity(si);

    }

    /**
     * this method shows only the maps that the user created in the list view above.
     * @param view
     */
    public void myPaths(View view) {
        et.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        Query query = refMaps.orderByChild("uidcreator").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            //
            @Override
            public void onDataChange(DataSnapshot dS) {
                MStList.clear();
                Mlist.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    Map mTmp = data.getValue(Map.class);
                    Mlist.add(mTmp);
                    st = mTmp.getMapname();
                    MStList.add(st+"");
                }
                adp = new ArrayAdapter<String>(ChoosingGuideMap.this,R.layout.support_simple_spinner_dropdown_item, MStList);
                lvCP.setAdapter(adp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    /**
     * This method changes the activity's view to public paths searching view
     * @param view
     */
    public void publicPaths(View view) {
        et.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);

    }

    private void fetchData( final List<DataSnapshot> dataSnapshotList) {
        if(dataSnapshotList.isEmpty()) {
            adp = new ArrayAdapter<String>(ChoosingGuideMap.this,R.layout.support_simple_spinner_dropdown_item, MStList);
            lvCP.setAdapter(adp);
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
     * this method shows public maps with the map name that was searched in the list view above,
     * it is activated when the serching button is clicked
     * @param view
     */
    public void searchP(View view) {
        String pathname;
        pathname=et.getText().toString();
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

}

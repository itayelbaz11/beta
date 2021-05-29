package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class listViewVector extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;

    ArrayList<String> instructions;

    ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_vector);

        lv=(ListView) findViewById(R.id.lvVector);
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Intent gi=getIntent();
        instructions=gi.getStringArrayListExtra("instructions");

        adp = new ArrayAdapter<String>(listViewVector.this,R.layout.support_simple_spinner_dropdown_item, instructions);
        lv.setAdapter(adp);
    }

    public void backV(View view) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setBackgroundColor(Color.RED);
    }
}

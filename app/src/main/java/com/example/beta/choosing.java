package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class choosing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);
    }

    public void guider(View view) {
        Intent si = new Intent(choosing.this,ChoosingGuideMap.class);
        startActivity(si);
    }

    public void navigator(View view) {
        Intent si = new Intent(choosing.this,SearchingPath.class);
        startActivity(si);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.choosingI) {

        }
        else if (id==R.id.creditsI) {
            Intent si = new Intent(choosing.this,credits.class);
            startActivity(si);
        }
        else if (id==R.id.logoutI) {
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent si = new Intent(choosing.this,Login.class);
            startActivity(si);
        }
        return true;
    }
}

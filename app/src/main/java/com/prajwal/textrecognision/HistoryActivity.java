package com.prajwal.textrecognision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    databaseHelper databaseHelper;
    ListView historyList;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Adding Custom toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Readify - History");


        historyList = findViewById(R.id.historyView);
        getHistory();

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String t = parent.getItemAtPosition(position).toString();
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.popup_play){
                            Intent intent = new Intent(HistoryActivity.this,Player.class);
                            intent.putExtra("text",t );
                            startActivity(intent);
                        }
                        return true;
                    }
                });
            }
        });

    }
    public HistoryActivity(){
        databaseHelper = new databaseHelper(this);
    }
    public HistoryActivity(Context context){
        databaseHelper = new databaseHelper(context);
    }
    private void getHistory(){
        ArrayList<String> arrayList = databaseHelper.getData(User.getUsername());
        //String[] arr = {"Item 1","Item 2", "Item 3", "Item 4"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_history,R.id.textView,arrayList);
        historyList.setAdapter(arrayAdapter);

    }
    public void storeData(String uname,String data){
        if(databaseHelper.insertData(uname, data)){
            Toast.makeText(HistoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(HistoryActivity.this, "Fail", Toast.LENGTH_SHORT).show();
        }

    }


}
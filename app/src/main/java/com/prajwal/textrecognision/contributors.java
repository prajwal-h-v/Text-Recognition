package com.prajwal.textrecognision;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class contributors extends AppCompatActivity {


    String[] nameList= {"SHUBHANG C S", "PRAJWAL H V", "SAHIL SADIK"};
    String info[]={"ISE-BMSCE","ISE-BMSCE","ISE-BMSCE"};
    int photos[] = {R.drawable.shubhang, R.drawable.prajwal, R.drawable.sahil};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);
        ListView simpleList = findViewById(R.id.simpleView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),nameList,info,photos);
        simpleList.setAdapter(customAdapter);
    }
}
package com.prajwal.textrecognision;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String name[];
    int photo[];
    String info[];
    LayoutInflater inflter;
    public CustomAdapter(Context applicationcontext, String[] name,String[] Info, int[] photo) {
        this.context = applicationcontext;
        this.name = name;
        this.photo = photo;
        info=Info;
        inflter = (LayoutInflater.from(applicationcontext)); // Create a new LayoutInflater instance associated with a particular Context

    }


    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.contributors_list,null);
        TextView names = (TextView)view.findViewById(R.id.tex1);
        TextView infoss = (TextView)view.findViewById(R.id.textView2);
        ImageView icon = (ImageView)view.findViewById(R.id.i1);
        icon.setImageResource(photo[i]);
        names.setText(name[i]);
        infoss.setText(info[i]);
        return view;
    }
    }

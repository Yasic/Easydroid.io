package com.example.esir.enotepad;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by ESIR on 2015/5/28.
 */
public class titlelayout extends LinearLayout {
    public titlelayout(final Context context,AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.myactionbar,this);
        Button menubutton = (Button)findViewById(R.id.menubutton);
        Button asyncbutton = (Button)findViewById(R.id.syncbutton);
        Button sortbutton = (Button)findViewById(R.id.sortbutton);
        menubutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        asyncbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Syncbutton is touched!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.esir.enotepad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by ESIR on 2015/5/30.
 */
public class noteactionbarlayout extends RelativeLayout {
    public noteactionbarlayout(final Context context,AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.noteactionbar, this);
        Button notebackbutton = (Button)findViewById(R.id.notebackbutton);
        Button notesavebutton = (Button)findViewById(R.id.notesavebutton);
    }
}

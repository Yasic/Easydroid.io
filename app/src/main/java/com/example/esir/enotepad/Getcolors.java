package com.example.esir.enotepad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/8/22.
 */
public class Getcolors {
    private List<Integer> list = new ArrayList(){{
        add(R.color.color_0);
        add(R.color.color_1);
        add(R.color.color_2);
        add(R.color.color_3);
        add(R.color.color_4);
        add(R.color.color_5);
        add(R.color.color_6);
        add(R.color.color_7);
        add(R.color.color_8);
        add(R.color.color_9);
        add(R.color.color_10);
        add(R.color.color_11);
        add(R.color.color_12);
        add(R.color.color_13);
        add(R.color.color_14);
        add(R.color.color_15);
        add(R.color.color_16);
        add(R.color.color_17);
        add(R.color.color_18);
    }};

    public Getcolors(){
        this.list = list;
    }

    public Integer getColor(int i){
        return this.list.get(i);
    }

    public Integer getColorCount(){
        return list.size();
    }

}

package com.example.esir.enotepad;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ESIR on 2015/8/9.
 */
public class Getfonts {
    public Typeface typeface;
    public Context context;
    public Typeface get_adele_light_webfont(Context context){
        typeface = Typeface.createFromAsset(context.getAssets(), "Fonts/adele-light-webfont.ttf");
        return typeface;
    }

    public Getfonts(Context context){
        this.typeface = typeface;
        this.context = context;
    }

    public


    Typeface get_Blackletter686BT(){
        typeface = Typeface.createFromAsset(context.getAssets(),"Fonts/Blackletter686BT.TTF");
        return typeface;
    }

    public Typeface get_Bookman_Old_Style(){
        typeface = Typeface.createFromAsset(context.getAssets(),"Fonts/Bookman_Old_Style.TTF");
        return typeface;
    }

    public Typeface get_DIN_Regular(){
        typeface = Typeface.createFromAsset(context.getAssets(),"Fonts/DIN-Regular.otf");
        return typeface;
    }

    public Typeface get_Frutiger(){
        typeface = Typeface.createFromAsset(context.getAssets(),"Fonts/Frutiger.ttf");
        return typeface;
    }

    public Typeface get_Futura_Book(){
        typeface = Typeface.createFromAsset(context.getAssets(),"Fonts/Futura-Book.otf");
        return typeface;
    }
}

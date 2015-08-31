package com.example.esir.homebutton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ESIR on 2015/7/29.
 */
public class Welcome_Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        TextView textView1 = (TextView)findViewById(R.id.welcome_Yasic);
        TextView textView2 = (TextView)findViewById(R.id.welcome_EasyTouch);
        Typeface typeface = Typeface.createFromAsset(getBaseContext().getAssets(), "Fonts/typewriter-monospacedletter-gothic-regular.ttf");
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);

        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent serviceintent = new Intent(Welcome_Activity.this, FloatWindowService.class);
                startService(serviceintent);

                Intent mainintent =  new Intent(Welcome_Activity.this,MainActivity.class);
                startActivity(mainintent);
                finish();
            }
        };
        timer.schedule(timerTask, 2000);
    }
}

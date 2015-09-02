package com.example.esir.homebutton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    public static final int CMD_STOP_SERVICE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_bottom_in_animation, R.anim.slide_still_out_animation);//activity切换动画
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView testtextview = (TextView)findViewById(R.id.mainactivity_title);
        Typeface typeface = Typeface.createFromAsset(getBaseContext().getAssets(), "Fonts/typewriter-monospacedletter-gothic-regular.ttf");
        testtextview.setTypeface(typeface);

        Button exit_button = (Button)findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitdialog();//退出对话框
            }
        });
    }

    @Override
    public boolean onKeyDown(int KeyCode,KeyEvent event){
        if(KeyCode == KeyEvent.KEYCODE_BACK){
            exitdialog();
        }
        return super.onKeyDown(KeyCode,event);
    }

    public void exitdialog(){
        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Leave?App will stop running..");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDestroy();//复写方法
                System.exit(0);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy(){
        /*Intent intent = new Intent();
        intent.setAction("stop service");
        intent.putExtra("cmd",CMD_STOP_SERVICE);//为0则关闭service
        sendBroadcast(intent);//发送广播*/
        Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
        stopService(intent);
        super.onDestroy();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

package com.example.esir.enotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;

/**
 * Created by ESIR on 2015/8/30.
 */
public class ReminderAlarm extends Activity {
    private MediaPlayer alarmMusic;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        alarmMusic = MediaPlayer.create(ReminderAlarm.this,
                RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE));
        alarmMusic.setLooping(true);
        alarmMusic.start();
        /*new AlertDialog.Builder(this).
                setTitle("").
                setMessage("").
                setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmMusic.stop();
                        ReminderAlarm.this.finish();
                    }
                });*/
    }
}

package com.example.esir.enotepad;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ESIR on 2015/8/29.
 */
public class ReminderDialog extends Dialog implements View.OnClickListener {
    private Context context;
    public Button reminderDialog_cancelbutton,reminderDialog_okbutton;
    public EditText reminderDialog_Title,reminderDialog_Description;
    public ReminderDialog_setting reminderDialog_setting;

    public ReminderDialog(Context context,int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.reminderdialog);
        initbutton();
        initedittext();
    }

    private void initbutton(){
        reminderDialog_cancelbutton = (Button)findViewById(R.id.reminderdialog_cancelbutton);
        reminderDialog_cancelbutton.setOnClickListener(this);
        reminderDialog_okbutton = (Button)findViewById(R.id.reminderdialog_okbutton);
        reminderDialog_okbutton.setOnClickListener(this);
    }

    private void initedittext(){
        reminderDialog_Title = (EditText)findViewById(R.id.reminderdialog_title);
        reminderDialog_Description = (EditText)findViewById(R.id.reminderdialog_description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reminderdialog_okbutton:
                if(reminderDialog_setting != null){
                    reminderDialog_setting.onReminderSetting(reminderDialog_Title.getText().toString(), reminderDialog_Description.getText().toString());
                    dismiss();
                }
                dismiss();
                break;
            case R.id.reminderdialog_cancelbutton:
                dismiss();
                break;
        }
    }

    public void setOnNotebookDialogListener(ReminderDialog_setting listener) {
        reminderDialog_setting = listener;
    }
}

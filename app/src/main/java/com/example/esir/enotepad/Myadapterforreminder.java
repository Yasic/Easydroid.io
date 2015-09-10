package com.example.esir.enotepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ESIR on 2015/8/30.
 */
public class Myadapterforreminder extends BaseAdapter {
    private Context context;
    private List<Reminder> reminders;

    public  Myadapterforreminder(Context context,List<Reminder> reminders){
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public int getCount(){
        return (reminders == null)?0:reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView reminder_Title;
        TextView reminder_Description;
        TextView reminder_Time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Reminder reminder = (Reminder)getItem(position);
        ViewHolder viewholder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.reminderitem,null);
        }
        viewholder = new ViewHolder();
        viewholder.reminder_Title = (TextView)convertView.findViewById(R.id.reminder_title);
        viewholder.reminder_Description = (TextView)convertView.findViewById(R.id.reminder_description);
        viewholder.reminder_Time = (TextView)convertView.findViewById(R.id.reminder_time);
        viewholder.reminder_Title.setText(reminder.Title);
        viewholder.reminder_Description.setText(reminder.Description);
        viewholder.reminder_Time.setText(reminder.Time);
        return convertView;
    }
}

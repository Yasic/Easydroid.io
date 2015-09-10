package com.example.esir.enotepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.Base;

import java.util.List;

/**
 * Created by ESIR on 2015/6/6.
 */
public class Myadapterfornotebook extends BaseAdapter {
    private Context context;
    private List<Notebook> Notebooks;

    public  Myadapterfornotebook(Context context,List<Notebook> Notebooks){
        this.context = context;
        this.Notebooks = Notebooks;
    }

    @Override
    public int getCount(){
        return (Notebooks == null)?0:Notebooks.size();
    }

    @Override
    public Object getItem(int position) {
        return Notebooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView notebook_Title;
        TextView notebook_Count;
        TextView notebook_Description;
        TextView notebook_Time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notebook notebook = (Notebook)getItem(position);
        ViewHolder viewholder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.notebookgridviewitem,null);
        }
        viewholder = new ViewHolder();
        viewholder.notebook_Title = (TextView)convertView.findViewById(R.id.notebook_title);
        viewholder.notebook_Count = (TextView)convertView.findViewById(R.id.notebook_count);
        viewholder.notebook_Description = (TextView)convertView.findViewById(R.id.notebook_description);
        viewholder.notebook_Time = (TextView)convertView.findViewById(R.id.notebook_time);
        viewholder.notebook_Title.setText(notebook.notebookTitle);
        viewholder.notebook_Count.setText(notebook.notebookCount);
        viewholder.notebook_Description.setText(notebook.notebookDescription);
        viewholder.notebook_Time.setText(notebook.notebookTime);
        return convertView;
    }
}

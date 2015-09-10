package com.example.esir.enotepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ESIR on 2015/5/28.
 */
public class Myadapter extends BaseAdapter {
    private Context context;
    private List<Drawmenu> Drawmenu;
    public Myadapter(Context context,List<Drawmenu> Drawmenu)
    {
        this.Drawmenu = Drawmenu;
        this.context = context;
    }
    @Override
    public int getCount() {
        return (Drawmenu==null)?0:Drawmenu.size();
    }

    @Override
    public Object getItem(int position) {
        return Drawmenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView imageview;
        TextView textview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Drawmenu drawmenu = (Drawmenu)getItem(position);
        ViewHolder viewholder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listviewitem,null);
            viewholder = new ViewHolder();
            viewholder.imageview = (ImageView)convertView.findViewById(R.id.listviewitem1);
            viewholder.textview = (TextView)convertView.findViewById(R.id.listviewitem2);
            viewholder.textview.setText(drawmenu.title);
            viewholder.imageview.setImageResource(drawmenu.picture);
        }
        return convertView;
    }
}

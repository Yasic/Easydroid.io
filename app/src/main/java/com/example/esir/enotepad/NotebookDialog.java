package com.example.esir.enotepad;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/8/29.
 */
public class NotebookDialog extends Dialog implements View.OnClickListener {

    Context context;
    Button notebookdialog_cancelbutton;
    Button notebookdialog_okbutton;
    NotebookDialog_setting DialogListener = null;
    EditText notebookdialog_title;
    EditText notebookdialog_description;
    public NotebookDialog(Context context,int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.notebookdialog);
        initColorList();
        initbutton();
        initedittext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notebookdialog_okbutton:
                NotebookDialog notebookDialog = new NotebookDialog(getContext(), R.style.notebookDialog);
                //notebookDialog.setOnNotebookDialogListener(Fragment2.this);
                notebookDialog.show();
                /*if(DialogListener != null){
                    DialogListener.onSetting(notebookdialog_title.getText().toString(),notebookdialog_description.getText().toString());
                    dismiss();
                }*/
                break;
            case R.id.notebookdialog_cancelbutton:
                dismiss();
                break;
        }
    }

    private void initColorList(){
        RecyclerView notebookdialog_ColorRecyclerView = (RecyclerView)findViewById(R.id.notebookdialog_ColorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        notebookdialog_ColorRecyclerView.setLayoutManager(layoutManager);
        List Colorlist = new ArrayList<Integer>();
        Getcolors getcolors = new Getcolors();
        for(int i = 0;i < getcolors.getColorCount();i++){
            Colorlist.add(i);
        }
        final ColorRecyclerview_adapter colorRecyclerview_adapter = new ColorRecyclerview_adapter(getContext(),Colorlist);
        notebookdialog_ColorRecyclerView.setAdapter(colorRecyclerview_adapter);
    }

    private void initbutton(){
        notebookdialog_cancelbutton = (Button)findViewById(R.id.notebookdialog_cancelbutton);
        notebookdialog_cancelbutton.setOnClickListener(this);
        notebookdialog_okbutton = (Button)findViewById(R.id.notebookdialog_okbutton);
        notebookdialog_okbutton.setOnClickListener(this);
    }

    private void initedittext(){
        notebookdialog_title = (EditText)findViewById(R.id.notebookdialog_title);
        notebookdialog_description = (EditText)findViewById(R.id.notebookdialog_description);
    }

    public void setOnNotebookDialogListener(NotebookDialog_setting listener) {
        DialogListener = listener;
    }
}

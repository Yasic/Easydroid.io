package com.example.esir.enotepad;

/**
 * Created by ESIR on 2015/6/6.
 */
public class Notebook {
    public String notebookTitle;
    public String notebookCount;
    public String notebookDescription;
    public String notebookTime;
    public String notebookColor;

    public Notebook(String notebookTitle,String notebookCount,String notebookDescription,String notebookTime,String notebookColor){
        super();
        this.notebookTitle = notebookTitle;
        this.notebookCount = notebookCount;
        this.notebookDescription = notebookDescription;
        this.notebookTime = notebookTime;
        this.notebookColor = notebookColor;
    }
}

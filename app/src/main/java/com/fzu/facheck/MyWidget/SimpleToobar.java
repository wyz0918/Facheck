package com.fzu.facheck.MyWidget;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.fzu.facheck.R;

public class SimpleToobar extends Toolbar {

    private TextView mTextLeftTitle;
    private TextView mTextMiddleTitle;
    public SimpleToobar(Context context) {
        this(context,null);
    }
    public SimpleToobar(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public SimpleToobar(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }
    private void init(final Context context){
        LayoutInflater.from(context).inflate(R.layout.simple_toolar_layout,this);
        mTextLeftTitle=(TextView)findViewById(R.id.txt_left);
        mTextMiddleTitle=(TextView)findViewById(R.id.txt_title);
    }
    public void setMainTitle(String text){
        this.setTitle("");
        mTextMiddleTitle.setText(text);
    }
    public void setLeftTitleClickListener(OnClickListener onClickListener){
        mTextLeftTitle.setOnClickListener(onClickListener);
    }
}

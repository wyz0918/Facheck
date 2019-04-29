package com.fzu.facheck.module.common;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class StartActivity extends RxBaseActivity {

    @BindView(R.id.login_button1)
    Button loginbtn;
    @BindView(R.id.register)
    Button regiterbtn;
    @Override
    public int getLayoutId() {
        return R.layout.start_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        GradientDrawable myGrad= (GradientDrawable) regiterbtn.getBackground();
        myGrad.setColor(Color.parseColor("#B0E0E6"));
        myGrad=(GradientDrawable)loginbtn.getBackground();
        myGrad.setColor(Color.parseColor("#00CDCD"));
    }

    @Override
    public void initToolBar() {

    }
    @OnClick({R.id.login_button1,R.id.register})
    void onClick(View view){
        switch (view.getId()){
            case R.id.login_button1:
                Intent intent1=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.register:
                Intent intent2=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }
}

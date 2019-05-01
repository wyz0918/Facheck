package com.fzu.facheck.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.module.common.CommonActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyPageFragment extends RxLazyFragment {
    @BindView(R.id.nametext)
    TextView nameText;
    @BindView(R.id.phonetext)
    TextView phoneText;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {

    }
    @OnClick({R.id.identify,R.id.exitlogin,R.id.updatepassword,R.id.theAbout})
    void onClick(View view){
        int data;
        Intent intent=new Intent(getActivity(),CommonActivity.class);
        switch(view.getId()){
            case R.id.identify:
                data=1;
                intent.putExtra("flag",data);
                startActivity(intent);
                break;
            case R.id.updatepassword:
                data=2;
                intent.putExtra("flag",data);
                startActivity(intent);
                break;
            case R.id.theAbout:
                data=3;
                intent.putExtra("flag",data);
                startActivity(intent);
                break;
            case R.id.exitlogin:
                getActivity().finish();
                break;
        }
    }
}

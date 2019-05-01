package com.fzu.facheck.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxBaseActivity;
import com.fzu.facheck.home.IdentityFragment;
import com.fzu.facheck.home.TheAboutFragment;
import com.fzu.facheck.home.UpdatePwFragment;
import com.fzu.facheck.utils.ToastUtil;

import butterknife.BindView;

public class CommonActivity extends RxBaseActivity {

    String title;
    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    @BindView(R.id.toolbar_title)
    TextView title_text;
    @Override
    public int getLayoutId() {
        return R.layout.common_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        int flag=intent.getIntExtra("flag",0);
        switch (flag){
            case 0:
                ToastUtil.showShort(this,"程序运行出错");
                break;
            case 1:
                title="身份认证";
                replaceFragment(new IdentityFragment());
                break;
            case 2:
                title="修改密码";
                replaceFragment(new UpdatePwFragment());
                break;
            case 3:
                title="关于我们";
                replaceFragment(new TheAboutFragment());
        }
    }

    @Override
    public void initToolBar() {
        mtoolbar.setNavigationIcon(R.drawable.backicon);
        title_text.setText(title);
        mtoolbar.setNavigationOnClickListener(v->finish());
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.common,fragment);
        transaction.commit();
    }
}

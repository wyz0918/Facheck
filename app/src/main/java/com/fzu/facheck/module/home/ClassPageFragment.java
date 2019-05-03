package com.fzu.facheck.module.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;

import butterknife.BindView;

public class ClassPageFragment extends RxLazyFragment {
    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    @BindView(R.id.toolbar_title)
    TextView title_text;
    private String classname="数据结构";
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_class_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initToolbar();
    }

    private void initToolbar(){
        mtoolbar.setNavigationIcon(R.drawable.backicon);
        title_text.setText(classname);
       // mtoolbar.setNavigationOnClickListener(v->finish());
    }
}

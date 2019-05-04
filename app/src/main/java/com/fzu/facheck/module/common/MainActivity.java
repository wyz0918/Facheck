package com.fzu.facheck.module.common;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxBaseActivity;
import com.fzu.facheck.module.home.HomePageFragment;
import com.fzu.facheck.utils.ConstantUtil;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends RxBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,EasyPermissions.PermissionCallbacks{



    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private TextView mTextMessage;

    private HomePageFragment mHomePageFragment;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};



    public void requestPermission(){
        if(EasyPermissions.hasPermissions(this, permissions)){

        }else{
            EasyPermissions.requestPermissions(this, "需要申请您手机权限", ConstantUtil.RC_CAMERA__CALENDAR_STORAGE_PHONE_LOCATION,permissions);
        }
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        requestPermission();
        //初始化Fragment
        initFragments();
        initNavigationView();


    }


    @Override
    public void initNavigationView() {

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }


    /**
     * 初始化Fragments
     */
    private void initFragments() {
        mHomePageFragment = HomePageFragment.newInstance();

//        fragments = new Fragment[]{
//                mHomePageFragment,
//
//        };
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mHomePageFragment)
                .show(mHomePageFragment).commit();
    }

    @Override
    public void initToolBar() {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        new AppSettingsDialog.Builder(this)
        .setRationale("点名功能需要上述权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .build()
                .show();


    }
}

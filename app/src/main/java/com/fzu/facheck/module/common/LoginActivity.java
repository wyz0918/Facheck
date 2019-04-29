package com.fzu.facheck.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fzu.facheck.MainActivity;
import com.fzu.facheck.R;
import com.fzu.facheck.base.RxBaseActivity;
import com.fzu.facheck.entiy.logininfo.StateInfo;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends RxBaseActivity {

    @BindView(R.id.toobar)
    Toolbar mToolbar;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @Override
    public int getLayoutId() {
        return R.layout.login_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        username.setText(null);
        password.setText(null);
    }

    @Override
    public void initToolBar() {
        mToolbar.setNavigationIcon(R.drawable.backicon);
        mToolbar.setTitle("登录");
        mToolbar.setNavigationOnClickListener(v->finish());
    }
    @OnClick({R.id.login_button,R.id.forget_pass})
    void onClick(View view){
        switch (view.getId()){
            case R.id.login_button:
                String un=username.getText().toString();
                String pw=password.getText().toString();
                if(TextUtils.isEmpty(un)||TextUtils.isEmpty(pw))
                    ToastUtil.showShort(LoginActivity.this,"手机号或密码不能为空");
                else if(un.length()<11)
                    ToastUtil.showShort(LoginActivity.this,"手机号错误");
                else if(pw.length()<6)
                    ToastUtil.showShort(LoginActivity.this,"密码过短");
                else{
                    final JSONObject userobject=new JSONObject();
                    try {
                        userobject.put("phoneNumber",un);
                        userobject.put("password",pw);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=utf-8"),userobject.toString());
                    RetrofitHelper.getLoAPI().getserver("login",requestBody)
                            .compose(bindToLifecycle())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<StateInfo>() {
                                @Override
                                public void onCompleted() { }
                                @Override
                                public void onError(Throwable e) {
                                    if(e instanceof HttpException){
                                        HttpException httpException=(HttpException)e;
                                        if(httpException.code()==500)
                                            ToastUtil.showShort(LoginActivity.this,"服务器出错");
                                    }
                                    else
                                        ToastUtil.showShort(LoginActivity.this,"请求失败");
                                }
                                @Override
                                public void onNext(StateInfo stateInfo) {
                                    if(stateInfo.code.equals("0100")){
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else if(stateInfo.code.equals("0101"))
                                        ToastUtil.showShort(LoginActivity.this,"密码错误");
                                    else
                                        ToastUtil.showShort(LoginActivity.this,"未知情况错误");
                                }
                            });
                }
                break;
            case R.id.forget_pass:
                Intent intent=new Intent(LoginActivity.this,GetNewPassActivity.class);
                startActivity(intent);
                break;
        }
    }
}

package com.fzu.facheck.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fzu.facheck.MainActivity;
import com.fzu.facheck.R;
import com.fzu.facheck.base.RxBaseActivity;
import com.fzu.facheck.entiy.logininfo.StateInfo;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.TimeCountUtil;
import com.fzu.facheck.utils.ToastUtil;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends RxBaseActivity {

    @BindView(R.id.toobar)
    Toolbar mToolbar;
    @BindView(R.id.name)
    EditText usernameText;
    @BindView(R.id.pass)
    EditText passwordText;
    @BindView(R.id.photo_input)
    EditText phoneText;
    @BindView(R.id.varifi)
    EditText varifyText;
    @BindView(R.id.get_varification)
    Button getvarifybtn;
    @Override
    public int getLayoutId() {
        return R.layout.register_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        MobSDK.init(this,"2ad6399bb9865","55335739821de02f5fcfd8819903afea");
        final EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event,int result,Object data){
                if(result==SMSSDK.RESULT_COMPLETE){
                    if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        JSONObject userobject = new JSONObject();
                        try {
                            userobject.put("username", usernameText.getText().toString());
                            userobject.put("password", passwordText.getText().toString());
                            userobject.put("phoneNumber", phoneText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), userobject.toString());
                        RetrofitHelper.getLoAPI().getserver("register", requestBody)
                                .compose(bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<StateInfo>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e instanceof HttpException) {
                                            HttpException httpException = (HttpException) e;
                                            if (httpException.code() == 500)
                                                ToastUtil.showShort(RegisterActivity.this, "服务器出错");
                                        } else
                                            ToastUtil.showShort(RegisterActivity.this, "请求失败");
                                    }

                                    @Override
                                    public void onNext(StateInfo stateInfo) {
                                        if (stateInfo.code.equals("0000")) {
                                            ToastUtil.showShort(RegisterActivity.this, "注册成功");
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else if (stateInfo.code.equals("0001"))
                                            ToastUtil.showShort(RegisterActivity.this, "账号已存在");
                                        else
                                            ToastUtil.showShort(RegisterActivity.this, "未知情况错误");
                                    }
                                });
                    }
                    else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        infomation("验证码已发送");
                    }
                    else;
                }
                else{
                    infomation("验证码错误");
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void initToolBar() {
        mToolbar.setNavigationIcon(R.drawable.backicon);
        mToolbar.setTitle("注册");
        mToolbar.setNavigationOnClickListener(v->finish());
    }
    @OnClick({R.id.OK,R.id.get_varification})
    void onClick(View view){
        String pt=phoneText.getText().toString();
        String vr=varifyText.getText().toString();
        switch(view.getId()){
            case R.id.get_varification:
                if(pt.length()<11)
                    ToastUtil.showShort(RegisterActivity.this,"请输入正确手机号");
                else{
                    TimeCountUtil time = new TimeCountUtil(60000, 1000, getvarifybtn);
                    SMSSDK.getVerificationCode("86",pt);
                    time.start();
                }
                break;
            case R.id.OK:
                if(TextUtils.isEmpty(usernameText.getText()))
                    ToastUtil.showShort(RegisterActivity.this,"昵称不能为空");
                else if(TextUtils.isEmpty(passwordText.getText()))
                    ToastUtil.showShort(RegisterActivity.this,"密码不能为空");
                else if(passwordText.getText().toString().length()<6)
                    ToastUtil.showShort(RegisterActivity.this,"密码过短");
                else if(TextUtils.isEmpty(pt))
                    ToastUtil.showShort(RegisterActivity.this,"手机号不能为空");
                else if(TextUtils.isEmpty(vr))
                    ToastUtil.showShort(RegisterActivity.this,"验证码不能为空");
                else if(pt.length()<11)
                    ToastUtil.showShort(RegisterActivity.this,"手机号错误");
                else
                    SMSSDK.submitVerificationCode("86",pt,vr);
                break;
        }
    }
    private void infomation(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}

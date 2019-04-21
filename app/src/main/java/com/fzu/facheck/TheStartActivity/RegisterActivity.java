package com.fzu.facheck.TheStartActivity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fzu.facheck.MyWidget.SimpleToobar;
import com.fzu.facheck.R;
import com.fzu.facheck.WorkClass.HttpConection;
import com.fzu.facheck.WorkClass.TimeCount;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private SimpleToobar simpleToobar;
    Button getvarifyBtn;
    Button okBtn;
    EditText usernameText,passwordText,photoText,varifyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initUI();
        getvarifyBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }
    private void initUI(){
        initStatus();
        getvarifyBtn=(Button)findViewById(R.id.get_varification);
        okBtn=(Button)findViewById(R.id.OK);
        usernameText=(EditText)findViewById(R.id.name);
        passwordText=(EditText)findViewById(R.id.pass);
        photoText=(EditText)findViewById(R.id.photo_input);
        varifyText=(EditText)findViewById(R.id.varifi);
        MobSDK.init(this,"2ad6399bb9865","55335739821de02f5fcfd8819903afea");
        final EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event,int result,Object data){
                if(result==SMSSDK.RESULT_COMPLETE)
                {
                    if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                    {
                        JSONObject userobject=new JSONObject();
                        try {
                            userobject.put("username",usernameText.getText().toString());
                            userobject.put("password",passwordText.getText().toString());
                            userobject.put("phoneNumber",photoText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpConection.sendPostRequest("http://172.26.93.218:5000/register", userobject.toString(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                infomation("请求失败");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful())
                                {
                                    String code=null;
                                    try {
                                        JSONObject job=new JSONObject(response.body().string());
                                        code=job.getString("state");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(code=="000")
                                    {
                                        infomation("注册成功");
                                        finish();
                                    }
                                    else if(code=="001")
                                    {
                                        infomation("注册失败,账号已存在");
                                    }
                                    else{
                                        infomation("未知情况错误");
                                    }
                                }
                                else
                                {
                                    if(response.code()==500)
                                        infomation("服务器出错");
                                    else infomation("客户端出错");
                                }
                            }
                        });
                    }
                    else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE)
                        infomation("验证码已发送");
                    else;
                }
                else
                {
                    infomation("验证码错误");
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }
    private void initStatus(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.parseColor("#00CDCD"));
        simpleToobar=(SimpleToobar)findViewById(R.id.simple_toolbar);
        simpleToobar.setMainTitle("注册账号");
        simpleToobar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_varification:
                if(photoText.getText().toString().length()<11)
                    Toast.makeText(RegisterActivity.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                else {
                    TimeCount time = new TimeCount(60000, 1000, getvarifyBtn);
                    SMSSDK.getVerificationCode("86",photoText.getText().toString());
                    time.start();
                }
                break;
            case R.id.OK:
                String pt=photoText.getText().toString();
                String vr=varifyText.getText().toString();
                if(TextUtils.isEmpty(usernameText.getText()))
                    Toast.makeText(RegisterActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(passwordText.getText()))
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                else if(passwordText.getText().toString().length()<6)
                    Toast.makeText(RegisterActivity.this,"密码过短",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(pt))
                    Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(vr))
                    Toast.makeText(RegisterActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                else if(pt.length()<11)
                    Toast.makeText(RegisterActivity.this,"手机号错误",Toast.LENGTH_SHORT).show();
                else
                {
                    //SMSSDK.submitVerificationCode("86",pt,vr);
                    JSONObject userobject=new JSONObject();
                    try {
                        userobject.put("username",usernameText.getText().toString());
                        userobject.put("password",passwordText.getText().toString());
                        userobject.put("phoneNumber",photoText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpConection.sendPostRequest("http://172.26.93.218:5000/register", userobject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            infomation("请求失败");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful())
                            {
                                String code=null;
                                try {
                                    JSONObject job=new JSONObject(response.body().string());
                                    code=job.getString("state");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(code.equals("0000"))
                                {
                                    infomation("注册成功");
                                    finish();
                                }
                                else if(code.equals("0001"))
                                {
                                    infomation("注册失败,账号已存在");
                                }
                                else{
                                    infomation("未知情况错误");
                                }
                            }
                            else
                            {
                                if(response.code()==500)
                                    infomation("服务器出错");
                                else infomation("客户端出错");
                            }
                        }
                    });
                }
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
}

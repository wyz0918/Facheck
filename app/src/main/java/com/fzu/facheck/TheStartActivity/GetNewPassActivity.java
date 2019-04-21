package com.fzu.facheck.TheStartActivity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


public class GetNewPassActivity extends AppCompatActivity implements View.OnClickListener{
    private SimpleToobar simpleToobar;
    EditText newpassText,newpassText1,photoText,varifyText;
    Button okbtn,getVar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_pass);
        initUI();
        okbtn.setOnClickListener(this);
        getVar.setOnClickListener(this);
    }
    private void initUI(){
        initStatus();
        newpassText=(EditText)findViewById(R.id.newpass);
        newpassText1=(EditText)findViewById(R.id.newpass1);
        photoText=(EditText)findViewById(R.id.photo_input);
        varifyText=(EditText)findViewById(R.id.varifi);
        getVar=(Button)findViewById(R.id.get_varification);
        okbtn=(Button)findViewById(R.id.ok1);
        MobSDK.init(this,"2ad6399bb9865","55335739821de02f5fcfd8819903afea");
        final EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event,int result,Object data){
                if(result==SMSSDK.RESULT_COMPLETE)
                {
                    if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                    {
                        JSONObject userobject=new JSONObject();
                        try {
                            userobject.put("photo",photoText.getText().toString());
                            userobject.put("password",newpassText1.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpConection.sendPostRequest("", userobject.toString(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                infomation("请求失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String code=null;
                                    JSONObject job= null;
                                    try {
                                        job = new JSONObject(response.body().string());
                                        code=job.getString("state");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(code=="0400")
                                    {
                                        infomation("修改密码成功");
                                        finish();
                                    }
                                    else if(code=="0500")
                                    {
                                        infomation("修改失败");
                                    }
                                    else{
                                        infomation("未知情况错误");
                                    }
                                }
                                else{
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
        simpleToobar.setMainTitle("修改密码");
        simpleToobar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void infomation(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GetNewPassActivity.this,msg,Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GetNewPassActivity.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                else {
                    TimeCount time = new TimeCount(60000, 1000, getVar);
                    SMSSDK.getVerificationCode("86",photoText.getText().toString());
                    time.start();
                }
                break;
            case R.id.ok1:
                String npt=newpassText.getText().toString();
                String npt1=newpassText1.getText().toString();
                String pt=photoText.getText().toString();
                String vr=varifyText.getText().toString();
                if(TextUtils.isEmpty(npt))
                    Toast.makeText(GetNewPassActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                else if(npt.length()<6)
                    Toast.makeText(GetNewPassActivity.this,"新密码过短",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(npt1))
                    Toast.makeText(GetNewPassActivity.this,"未确认新密码",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(pt))
                    Toast.makeText(GetNewPassActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(vr))
                    Toast.makeText(GetNewPassActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                else if(pt.length()<11)
                    Toast.makeText(GetNewPassActivity.this,"手机号错误",Toast.LENGTH_SHORT).show();
                else if(!npt.equals(npt1))
                    Toast.makeText(GetNewPassActivity.this,"密码确认错误",Toast.LENGTH_SHORT).show();
                else
                {
                    //SMSSDK.submitVerificationCode("86",pt,vr);
                    JSONObject userobject=new JSONObject();
                    try {
                        userobject.put("phoneNumber",photoText.getText().toString());
                        userobject.put("password",newpassText1.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpConection.sendPostRequest("http://172.26.93.218:5000/reset_password", userobject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            infomation("请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                String code=null;
                                JSONObject job= null;
                                try {
                                    job = new JSONObject(response.body().string());
                                    code=job.getString("state");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(code.equals("0400"))
                                {
                                    infomation("修改密码成功");
                                    finish();
                                }
                                else if(code.equals("0401"))
                                {
                                    infomation("修改失败");
                                }
                                else{
                                    infomation("未知情况错误");
                                }
                            }
                            else{
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
}

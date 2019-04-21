package com.fzu.facheck.TheStartActivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fzu.facheck.MainActivity;
import com.fzu.facheck.MyWidget.SimpleToobar;
import com.fzu.facheck.R;
import com.fzu.facheck.WorkClass.HttpConection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleToobar simpleToobar;
    EditText username;
    EditText password;
    Button login_in;
    TextView forget_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initUI();
        forget_pass.setOnClickListener(this);
        login_in.setOnClickListener(this);
    }
    private void initUI()
    {
        initStatus();
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login_in=(Button)findViewById(R.id.login_button);
        forget_pass=(TextView)findViewById(R.id.forget_pass);
    }
    private void initStatus(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.parseColor("#00CDCD"));
        simpleToobar=(SimpleToobar)findViewById(R.id.simple_toolbar);
        simpleToobar.setMainTitle("登陆账号");
        simpleToobar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch ((v.getId()))
        {
            case R.id.forget_pass:
                Intent intent=new Intent(LoginActivity.this,GetNewPassActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                String un=username.getText().toString();
                String pw=password.getText().toString();
                if(TextUtils.isEmpty(un)||TextUtils.isEmpty(pw))
                    Toast.makeText(LoginActivity.this,"手机号或密码不能为空",Toast.LENGTH_SHORT).show();
                else if(un.length()<11)
                    Toast.makeText(LoginActivity.this,"手机号错误",Toast.LENGTH_SHORT).show();
                else if(pw.length()<6)
                    Toast.makeText(LoginActivity.this,"密码过短",Toast.LENGTH_SHORT).show();
                else
                {
                    final JSONObject userobject=new JSONObject();
                    try {
                        userobject.put("phoneNumber",un);
                        userobject.put("password",pw);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpConection.sendPostRequest("http://172.26.93.218:5000/login", userobject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) { infomation("请求失败");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                String code=null;
                                try {
                                    JSONObject job=new JSONObject(response.body().string());
                                    code=job.getString("state");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(code.equals("0100")){
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else if(code.equals("0101"))
                                {
                                    infomation("密码错误");
                                }
                                else{infomation("未知情况错误"); }
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
                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

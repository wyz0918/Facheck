package com.fzu.facheck.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UpdatePwFragment extends RxLazyFragment {
    @BindView(R.id.pass)
    EditText oldpassText;
    @BindView(R.id.new_pass)
    EditText newpassText;
    @BindView(R.id.new_pass1)
    EditText newpassText1;
    @Override
    public int getLayoutResId() {
        return R.layout.update_pw_fragment;
    }

    @Override
    public void finishCreateView(Bundle state) {
    }
    @OnClick({R.id.OK})
    void onClick(View view){
        switch (view.getId()){
            case R.id.OK:
                String oldp=oldpassText.getText().toString();
                String newp=newpassText.getText().toString();
                String newp1=newpassText1.getText().toString();
                if(TextUtils.isEmpty(oldp))
                    ToastUtil.showShort(getActivity(),"请输入旧密码");
                else if(TextUtils.isEmpty(newp))
                    ToastUtil.showShort(getActivity(),"请输入新密码");
                else if(TextUtils.isEmpty(newp1))
                    ToastUtil.showShort(getActivity(),"请确认新密码");
                else if(oldp.length()<6||newp.length()<6||newp1.length()<6)
                    ToastUtil.showShort(getActivity(),"密码不得小于6位");
                else if(!newp.equals(newp1))
                    ToastUtil.showShort(getActivity(),"新密码确认错误");
                else{
//                    JSONObject object=new JSONObject();
//                    try {
//                        object.put("",oldp);
//                        object.put("",newp1);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=utf-8"),object.toString());
                    ToastUtil.showShort(getActivity(),"修改成功");
                    getActivity().finish();
                }
                break;
        }
    }
}

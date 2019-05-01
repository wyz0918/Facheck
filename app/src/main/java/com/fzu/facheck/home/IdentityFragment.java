package com.fzu.facheck.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.entiy.logininfo.StateInfo;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.ToastUtil;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IdentityFragment extends RxLazyFragment {
    @BindView(R.id.you_name)
    EditText Textname;
    @BindView(R.id.you_id)
    EditText Textid;
    @Override
    public int getLayoutResId() {
        return R.layout.identify_fragment;
    }

    @Override
    public void finishCreateView(Bundle state) {
    }
    @OnClick({R.id.submit_message})
    void onClick(View view){
        switch (view.getId()){
            case R.id.submit_message:
                String name=Textname.getText().toString();
                String id=Textid.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(id))
                    ToastUtil.showShort(getActivity(),"姓名或学号不能为空");
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("请确认您的信息:");
                    dialog.setMessage("姓名："+name+"\r\n"+"学号/工号："+id);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final JSONObject userobject=new JSONObject();
                            try {
                                userobject.put("name",name);
                                userobject.put("authenticationID",id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=utf-8"),userobject.toString());
                            RetrofitHelper.getLoAPI().getserver("identity_authenticate",requestBody)
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
                                                    ToastUtil.showShort(getActivity(),"服务器出错");
                                            }
                                            else
                                                ToastUtil.showShort(getActivity(),"请求失败");
                                        }
                                        @Override
                                        public void onNext(StateInfo stateInfo) {
                                            if(stateInfo.code.equals("0200")){
                                                ToastUtil.showShort(getActivity(),"认证成功");
                                                getActivity().finish();
                                            }
                                            else if(stateInfo.code.equals("0201"))
                                                ToastUtil.showShort(getActivity(),"认证失败");
                                            else
                                                ToastUtil.showShort(getActivity(),"其他情况错误");
                                        }
                                    });
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }
}

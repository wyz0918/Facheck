package com.fzu.facheck.module.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.entity.RollCall.ClassInfo;
import com.fzu.facheck.entity.RollCall.CreateClassInfo;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.TimeUtil;
import com.fzu.facheck.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateClassFragment extends RxLazyFragment {
    @BindView(R.id.class_name)
    EditText classnameText;
    @BindView(R.id.per_class_time)
    TextView perclasstimeText;
    @BindView(R.id.the_begin_time)
    TextView begintimeText;
    @BindView(R.id.the_end_time)
    TextView endtimeText;
    @BindView(R.id.total_class_time)
    EditText totaltimeText;
    @BindView(R.id.the_work_date)
    TextView worddateText;
    List<String> wordData=new ArrayList<>();
    @Override
    public int getLayoutResId() {
        return R.layout.layout_create_calss;
    }

    @Override
    public void finishCreateView(Bundle state) { }

    @OnClick({R.id.cancel,R.id.commit,R.id.per_class_time
    ,R.id.the_begin_time,R.id.the_end_time,R.id.the_work_date})
    void onClick(View view){
        switch (view.getId()){
            case R.id.per_class_time:
                String[] options1Item={"0.5","1.0","1.5","2.0","2.5","3.0","3.5","4.0","4.5"
                        ,"5.0","5.5","6.0","6.5","7.0","7.5","8.0"};
                OptionsPickerView pvOptions=new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        perclasstimeText.setText(options1Item[options1]);
                    }
                }).setTitleText("每节课课时数")
                        .setCancelText("取消")
                        .setSubmitText("确定")
                        .setOutSideCancelable(false)
                        .setSelectOptions(4)
                        .build();
                pvOptions.setPicker(Arrays.asList(options1Item));
                pvOptions.show();
                break;
            case R.id.the_begin_time:
                selectTime(begintimeText);
                break;
            case R.id.the_end_time:
                selectTime(endtimeText);
                break;
            case R.id.the_work_date:
                showupPopWindow();
                break;
            case R.id.cancel:
                break;
            case R.id.commit:
                sendCreateRequest();
                break;
        }
    }
    private void selectTime(TextView vv){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019,0,1);
        endDate.set(2022,11,31);
        TimePickerView pvtime=new TimePickerBuilder(getActivity(),new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                vv.setText(TimeUtil.formatDate(date));
            }
        }).setType(new boolean[]{true,true,true,false,false,false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleText("请选择时间")
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                .setLabel("年","月","日",
                        null,null,null)
                .isCenterLabel(false)
                .setOutSideCancelable(false)
                .build();
        pvtime.show();
    }
    private void sendCreateRequest(){
        String classname=classnameText.getText().toString();
        String begintime=begintimeText.getText().toString();
        String endtime=endtimeText.getText().toString();
        String perclasstime=perclasstimeText.getText().toString();
        String totaltime=totaltimeText.getText().toString();
        String worddate=worddateText.getText().toString();
        if(TextUtils.isEmpty(classname)||TextUtils.isEmpty(totaltime))
            ToastUtil.showShort(getActivity(),"请填入完整信息");
        else if(begintime.equals("请输入开始时间 >")||endtime.equals("请输入结束时间 >")||
                perclasstime.equals("请输入课时数 >")||worddate.equals("请输入上课时间 >"))
            ToastUtil.showShort(getActivity(),"请填入完整信息");
        else{
            final JSONObject classobject=new JSONObject();
            try {
                classobject.put("className",classname);
                classobject.put("classTime",worddate);
                classobject.put("phoneNumber","13215000002");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=utf-8"),classobject.toString());
            RetrofitHelper.getClassInfo().getclassserver("create_class",requestBody)
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CreateClassInfo>() {
                        @Override
                        public void onCompleted() { }
                        @Override
                        public void onError(Throwable e) { ToastUtil.showShort(getActivity(),"请求失败");}
                        @Override
                        public void onNext(CreateClassInfo classInfo) {
                            if(classInfo.code.equals("0500")){
                                wordData.clear();
                                ToastUtil.showShort(getActivity(),"创建成功 "+classInfo.getClassID());
                            }
                            else if(classInfo.code.equals("0501"))
                                ToastUtil.showShort(getActivity(),"创建失败");
                            else
                                ToastUtil.showShort(getActivity(),"未知情况错误");
                        }
                    });
        }
    }
    private void showupPopWindow(){
        View contentView=LayoutInflater.from(getActivity()).inflate(R.layout.popupwin_layout,null);
        PopupWindow mpopWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mpopWindow.setContentView(contentView);
        TextView tv1=(TextView)contentView.findViewById(R.id.select_one);
        tv1.setText("添加");
        TextView tv2=(TextView)contentView.findViewById(R.id.select_two);
        tv2.setText("清除");
        TextView tv3=(TextView)contentView.findViewById(R.id.cancel_select);
        tv1.setOnClickListener((v)->{
            mpopWindow.dismiss();
            selectWordDate();
        });
        tv2.setOnClickListener((v)->{
            worddateText.setText("请输入上课时间 >");
            wordData.clear();
            mpopWindow.dismiss();
        });
        tv3.setOnClickListener((v)->{
            mpopWindow.dismiss();
        });
        View rootView=LayoutInflater.from(getActivity()).inflate(R.layout.activity_main,null);
        mpopWindow.showAtLocation(rootView,Gravity.BOTTOM,0,0);
    }
    private void selectWordDate(){
        String[] week={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        String[] duringtime={"上午8:20-10:00","上午10:20-12:00","下午14:00-15:40","下午15:50-17:30"
        ,"晚上19:00-20:40"};
        OptionsPickerView pvOptions=new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                wordData.add(week[options1]+" "+duringtime[options2]);
                String s="";
                for(String dt:wordData)
                    s=s+dt+";";
                worddateText.setText(s);
            }
        }).setTitleText("上课时间")
                .setCancelText("取消")
                .setSubmitText("确定")
                .setOutSideCancelable(false)
                .setSelectOptions(4)
                .build();
        pvOptions.setNPicker(Arrays.asList(week),Arrays.asList(duringtime),null);
        pvOptions.show();
    }
}

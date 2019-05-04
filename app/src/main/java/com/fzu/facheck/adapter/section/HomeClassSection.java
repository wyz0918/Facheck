package com.fzu.facheck.adapter.section;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fzu.facheck.R;
import com.fzu.facheck.entity.RollCall.RollCallInfo;
import com.fzu.facheck.module.home.RollCallResultActivity;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.RxTimerUtil;
import com.fzu.facheck.widget.sectioned.StatelessSection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeClassSection extends StatelessSection {
    private List<RollCallInfo.ClassInfoBean.JoinedClassDataBean> jointedData;
    private List<RollCallInfo.ClassInfoBean.ManagedClassDataBean> createdData;
    private String type;
    private static final String JOINTED_DATA = "jointed_data";
    private static final String CREATE_DATA = "created_data";
    private Context mContext;
    private ArrayList<String> minutes = new ArrayList<>();
    private int rollCallTime;
    private OptionsPickerView pvOptions;
    private int mBtn_status = 0;
    private JSONObject jsonObject;

    private AMapLocation mLocation; //当前点的位置
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private String mRecordId;

    String TAG = "Home";



    public HomeClassSection( RollCallInfo.ClassInfoBean data,String type,Context context) {
        super(R.layout.layout_home_class_header, R.layout.layout_home_class);
        this.jointedData = data.getJoinedClassData();
        this.createdData = data.getManagedClassData();
        this.type = type;
        this.mContext = context;
        getOptionData();
        initAmapLocation();
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        switch (type) {
            case JOINTED_DATA:
                final RollCallInfo.ClassInfoBean.JoinedClassDataBean joinedClassDataBean = jointedData.get(position);
                itemViewHolder.mClassName.setText(joinedClassDataBean.getJoinedClassName());
                itemViewHolder.mClassDuration.setText(joinedClassDataBean.getJoinedClassTime());
                if (joinedClassDataBean.isSignable()) {
                    itemViewHolder.mBtn.setText(R.string.not_signed_in);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);
                    itemViewHolder.mBtn.setOnClickListener(v -> {

                        itemViewHolder.mBtn.setText(R.string.signed_in);
                        itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);
                    });

                } else {
                    itemViewHolder.mBtn.setText(R.string.signed_in);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);

                }
                break;
            case CREATE_DATA:
                final RollCallInfo.ClassInfoBean.ManagedClassDataBean managedClassData = createdData.get(position);
                itemViewHolder.mClassName.setText(managedClassData.getManagedClassName());
                itemViewHolder.mClassDuration.setText(managedClassData.getManagedClassTime());
                if (managedClassData.isAbleRollCall()) {
                    mBtn_status = 0;
                    itemViewHolder.mBtn.setText(R.string.roll_call_on);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);


                } else {
                    mBtn_status = 1;
                    itemViewHolder.mBtn.setText(R.string.roll_call_off);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);

                }

                itemViewHolder.mBtn.setOnClickListener(v -> {

                    if (mBtn_status == 0) {
                        initOptionPicker(position,itemViewHolder);
                    }else{
                        Intent intent = new Intent(mContext, RollCallResultActivity.class);

                        Log.i(TAG, "onBindItemViewHolder: "+mRecordId);
//                        intent.putExtra("record_id",mRecordId);
//                        intent.putExtra("class_title",itemViewHolder.mClassName.getText());

                        intent.putExtra("record_id","00000001");
                        intent.putExtra("class_title","高等数学");

                        mContext.startActivity(intent);
                        itemViewHolder.mBtn.setText(R.string.roll_call_on);
                        itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);
                    }

                });
                break;

        }


    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

        switch (type){
            case JOINTED_DATA:
                headerViewHolder.tileClass.setText(R.string.class_I_jointed);
                break;
            case CREATE_DATA:
                headerViewHolder.tileClass.setText(R.string.class_I_created);
                break;

        }
    }


    @Override
    public int getContentItemsTotal() {
        switch (type) {
            case JOINTED_DATA:
                return jointedData.size();
            case CREATE_DATA:
                return createdData.size();
            default:
                return 1;
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.class_name)
        TextView mClassName;
        @BindView(R.id.class_duration)
        TextView mClassDuration;
        @BindView(R.id.item_btn_rollcall)
        TextView mBtn;

        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_class)
        TextView tileClass;


        HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    private void getOptionData(){
        for(int i =1;i<=10;i++){
            minutes.add(i+"");
        }
    }

    private void initOptionPicker(int position,ItemViewHolder itemViewHolder){
        pvOptions = new OptionsPickerBuilder(mContext, (options1, option2, options3, v1) ->{rollCallTime = Integer.valueOf(minutes.get(options1));uploadData(position, itemViewHolder);} )
                .setTitleText("选择点名时间／分钟")
                .setSelectOptions(4)//默认选中项
                .build();
        pvOptions.setPicker(minutes);
        pvOptions.show();
    }

    private RequestBody initRequestbody(int position){

        jsonObject = new JSONObject();

        try {
            if(mLocation!=null){

                jsonObject .put("longitude",mLocation.getLongitude());
                jsonObject .put("latitude",mLocation.getLatitude());
            }else{
                //当前网络状况不好 无法定位 请移到网络状况良好的地区
                jsonObject .put("longitude",119.30);
                jsonObject .put("latitude",26.08);
            }
            jsonObject .put("classId",createdData.get(position).getManagedClassId());
            jsonObject .put("timeInterval",rollCallTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),  jsonObject.toString());
        return requestBody;
    }


    private void uploadData(int position,ItemViewHolder itemViewHolder){
        RetrofitHelper.getRollCallAPI()
                .getStartRollCallById(initRequestbody(position))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(resultBeans -> {

                    if (resultBeans.getCode().equals("0700")) {

                        mRecordId = resultBeans.getRecordId();
                        itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);
                        itemViewHolder.mBtn.setText(R.string.roll_calling);
                        RxTimerUtil.timer(rollCallTime, number -> itemViewHolder.mBtn.setText(R.string.roll_call_off));
                        mBtn_status = 1;

                    } else {


                    }

                });


    }

//    private void initAmapLocation() {
//        // 定位引擎接口
//        ILocationManager mLocationManager;
//        // 配置信息
//        Configuration mConfiguration;
//
//        mLocationManager = OnlineLocator.getInstance();
//        mConfiguration = createConfigBuilder(mContext).build();
//
//        mLocationManager.init("", mConfiguration, mSDKInitHandler);
//    }
//
//    /**
//     * 创建室内定位需要的配置信息
//     * @param context 主context
//     * @return 配置信息的builder
//     */
//    public static Configuration.Builder createConfigBuilder(Context context){
//        Configuration.Builder mConfigBuilder= new Configuration.Builder(context);
//        // 指定是使用wifi定位还是蓝牙定位
//        //mConfigBuilder.setLocationProvider(Configuration.LocationProvider.BLE);
//        mConfigBuilder.setLocationProvider(Configuration.LocationProvider.BLE);
//
//        // ***指定自己在高德网站申请的key***
//        mConfigBuilder.setLBSParam("a0a773675f4bd808453f0c6451eddb6b");
//        return mConfigBuilder;
//    }



    private void initAmapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，设备定位模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(50000);
        mLocationOption.setMockEnable(true);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }


    /**
     * 定位回调每1秒调用一次
     */
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mLocation = amapLocation;

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                }
            }
        }
    };

}

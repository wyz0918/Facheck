package com.fzu.facheck.module.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.adapter.section.HomeClassSection;
import com.fzu.facheck.entity.RollCall.RollCallInfo;
import com.fzu.facheck.module.common.MainActivity;
import com.fzu.facheck.R;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.SnackbarUtil;
import com.fzu.facheck.utils.TimeUtil;
import com.fzu.facheck.widget.CustomEmptyView;
import com.fzu.facheck.widget.sectioned.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import org.json.JSONException;
import org.json.JSONObject;

public class HomePageFragment extends RxLazyFragment {
    private static final String TAG = "HomePage";


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_title)
    TextView mtoolbar_title;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;


    private String date = getTime();


    private boolean mIsRefreshing = false;
    private SectionedRecyclerViewAdapter mSectionedAdapter;
    private RollCallInfo results = new RollCallInfo();



    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }
    
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setHasOptionsMenu(true);
        initToolBar();
        initRefreshLayout();
        initRecyclerView();

    }



    private void initToolBar() {
        mToolbar.setTitle("");
        mtoolbar_title.setText(date);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);

    }

    private String getTime(){
        Date date = new Date();
        String timePart1  = TimeUtil.parseDate(Calendar.getInstance().getTimeInMillis());
        String timePart2 = TimeUtil.getWeek(date);

        return timePart1+"("+timePart2+")";
    }


    @Override
    protected void initRecyclerView() {
        mSectionedAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mSectionedAdapter);
        setRecycleNoScroll();


    }


    @Override
    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            loadData();
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            loadData();
        });
    }

    @Override
    protected void loadData() {

        JSONObject userobject=new JSONObject();
        try {
            userobject.put("phoneNumber","33333333");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), userobject.toString());

        RetrofitHelper.getRollCallAPI()
                .getRollCallInfoById("get_user_class_info",requestBody)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {

                    if(resultBeans.getCode().equals("0900")){
                        results = resultBeans;
                        finishTask();
                    }else{
                        initEmptyView();
                    }

                }, throwable -> initEmptyView());


    }

    @Override
    protected void finishTask() {

        Log.i(TAG, "finishTask: "+results.getClassInfo().getJoinedClassData().size());
        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        hideEmptyView();
        int size = results.getClassInfo().getJoinedClassData().size();



            mSectionedAdapter.addSection(new HomeClassSection(results.getClassInfo(),"jointed_data"));


            mSectionedAdapter.addSection(new HomeClassSection(results.getClassInfo(),"created_data"));


        mSectionedAdapter.notifyDataSetChanged();
    }


    public void initEmptyView() {

        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load);
        mCustomEmptyView.setEmptyText("加载失败!");
        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
    }


    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void clearData() {

        if(results!=null){
            results.getClassInfo().getManagedClassData().clear();
            results.getClassInfo().getJoinedClassData().clear();
        }

        mIsRefreshing = true;
        mSectionedAdapter.removeAllSections();
    }


    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }


}

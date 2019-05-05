package com.fzu.facheck.module.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.adapter.section.HomeClassInfoSection;
import com.fzu.facheck.base.RxLazyFragment;
import com.fzu.facheck.entity.RollCall.ClassInfo;
import com.fzu.facheck.network.RetrofitHelper;
import com.fzu.facheck.utils.SnackbarUtil;
import com.fzu.facheck.widget.CustomEmptyView;
import com.fzu.facheck.widget.sectioned.SectionedRecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassPageFragment extends RxLazyFragment {
    private SectionedRecyclerViewAdapter mSectionedAdapter;
    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    @BindView(R.id.toolbar_title)
    TextView title_text;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    private String classname="数据结构";
    private ClassInfo result=new ClassInfo();
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_class_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initToolbar();
        initRecyclerView();
        loadData();
       // finishTask();
    }

    private void initToolbar(){
        mtoolbar.setNavigationIcon(R.drawable.backicon);
        title_text.setText(classname);
       // mtoolbar.setNavigationOnClickListener(v->finish());
    }
    @Override
    protected void initRecyclerView() {
        mSectionedAdapter=new SectionedRecyclerViewAdapter();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mSectionedAdapter);
    }

    @Override
    protected void loadData() {
        JSONObject userobject=new JSONObject();
        try {
            userobject.put("classId","00000001");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), userobject.toString());
        RetrofitHelper.getClassInfo().getclassInfo("get_all_class_sign_in_info",requestBody)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ClassInfo>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        initEmptyView();
                    }
                    @Override
                    public void onNext(ClassInfo classInfo) {
                        Log.d("MainActivity",classInfo.code);
                        if(classInfo.code.equals("1100")){
                            result=classInfo;
                            finishTask();
                        }else{
                            if(classInfo.getRecordsInfo()==null)
                                classInfo.RecordInstance();
                            result=classInfo;
                            finishTask();
                        }
                    }
                });
    }
    @Override
    public void finishTask() {
        Log.d("MainActivity","finish");
        hideEmptyView();
        mSectionedAdapter.addSection(new HomeClassInfoSection(result,"student",getActivity()));
        mSectionedAdapter.addSection(new HomeClassInfoSection(result,"records",getActivity()));
        mSectionedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void initEmptyView() {

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
}

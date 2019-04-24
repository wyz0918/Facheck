package com.fzu.facheck.home;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.MainActivity;
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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HomePageFragment extends RxLazyFragment {


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
//    private List<RecommendInfo.ResultBean> results = new ArrayList<>();
//    private List<RecommendBannerInfo.DataBean> recommendBanners = new ArrayList<>();


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
        isPrepared = true;
        lazyLoad();

    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        initRecyclerView();
        isPrepared = false;
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
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mSectionedAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_FOOTER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
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
//            clearData();
//            loadData();
        });
    }
//
//    @Override
//    protected void loadData() {
//        RetrofitHelper.getBiliAppAPI()
//                .getRecommendedBannerInfo()
//                .compose(bindToLifecycle())
//                .map(RecommendBannerInfo::getData)
//                .flatMap(new Func1<List<RecommendBannerInfo.DataBean>, Observable<RecommendInfo>>() {
//                    @Override
//                    public Observable<RecommendInfo> call(List<RecommendBannerInfo.DataBean> dataBeans) {
//                        recommendBanners.addAll(dataBeans);
//                        return RetrofitHelper.getBiliAppAPI().getRecommendedInfo();
//                    }
//                })
//                .compose(bindToLifecycle())
//                .map(RecommendInfo::getResult)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(resultBeans -> {
//                    results.addAll(resultBeans);
//                    finishTask();
//                }, throwable -> initEmptyView());
//    }
//
//    @Override
//    protected void finishTask() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mIsRefreshing = false;
//        hideEmptyView();
//        mSectionedAdapter.addSection(new HomeRecommendBannerSection(banners));
//        int size = results.size();
//        for (int i = 0; i < size; i++) {
//            String type = results.get(i).getType();
//            if (!TextUtils.isEmpty(type)) {
//                switch (type) {
//                    case ConstantUtil.TYPE_TOPIC:
//                        //话题
//                        mSectionedAdapter.addSection(new HomeRecommendTopicSection(getActivity(),
//                                results.get(i).getBody().get(0).getCover(),
//                                results.get(i).getBody().get(0).getTitle(),
//                                results.get(i).getBody().get(0).getParam()));
//                        break;
//                    case ConstantUtil.TYPE_ACTIVITY_CENTER:
//                        //活动中心
//                        mSectionedAdapter.addSection(new HomeRecommendActivityCenterSection(
//                                getActivity(),
//                                results.get(i).getBody()));
//                        break;
//                    default:
//                        mSectionedAdapter.addSection(new HomeRecommendedSection(
//                                getActivity(),
//                                results.get(i).getHead().getTitle(),
//                                results.get(i).getType(),
//                                results.get(1).getHead().getCount(),
//                                results.get(i).getBody()));
//                        break;
//                }
//            }
//            String style = results.get(i).getHead().getStyle();
//            if (style.equals(ConstantUtil.STYLE_PIC)) {
//                mSectionedAdapter.addSection(new HomeRecommendPicSection(getActivity(),
//                        results.get(i).getBody().get(0).getCover(),
//                        results.get(i).getBody().get(0).getParam()));
//            }
//        }
//        mSectionedAdapter.notifyDataSetChanged();
//    }
//
//
//    public void initEmptyView() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mCustomEmptyView.setVisibility(View.VISIBLE);
//        mRecyclerView.setVisibility(View.GONE);
//        mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
//        mCustomEmptyView.setEmptyText("加载失败!");
//        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
//    }
//
//
//    public void hideEmptyView() {
//        mCustomEmptyView.setVisibility(View.GONE);
//        mRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    private void clearData() {
//        recommendBanners.clear();
//        results.clear();
//        mIsRefreshing = true;
//        mSectionedAdapter.removeAllSections();
//    }


    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }


}

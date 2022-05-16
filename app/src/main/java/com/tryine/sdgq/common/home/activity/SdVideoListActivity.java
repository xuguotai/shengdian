package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.adapter.SdVideoHomeMenuAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.fragment.CourseListFragment;
import com.tryine.sdgq.common.home.fragment.VideoListFragment;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.mine.adapter.TabAdapter1;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圣典视频列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-25 10:35
 */
public class SdVideoListActivity extends BaseActivity implements SdHomeVideoView {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<HomeMenuBean> tabBeanList = new ArrayList<HomeMenuBean>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;
    SdVideoHomePresenter sdVideoHomePresenter;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter1 tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    int position = 0;

    public static void start(Context context, int position) {
        Intent intent = new Intent();
        intent.setClass(context, SdVideoListActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_sdlist;
    }

    @Override
    protected void init() {
        setWhiteBar();
        position = getIntent().getIntExtra("position", 0);
        sdVideoHomePresenter = new SdVideoHomePresenter(this);
        sdVideoHomePresenter.attachView(this);
        sdVideoHomePresenter.getVideoTypeList();
    }

    private void initTabViews() {
        tabAdapter = new TabAdapter1(this, tabBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_tab.setLayoutManager(lin);
        rv_tab.setAdapter(tabAdapter);


        tabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });

        fragmentManager = getSupportFragmentManager();

        for (int i = 0; i < tabBeanList.size(); i++) {
            fragmentList.add(createVideoListFragments(tabBeanList.get(i).getId()));
        }

        vpView.setAdapter(new MainPagerAdapter(fragmentManager));
        vpView.setCurrentItem(position);
        rv_tab.scrollToPosition(position);
        tabAdapter.setSelectedTabPosition(position);
        vpView.setOffscreenPageLimit(20);
        vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
                tabAdapter.notifyDataSetChanged();
                rv_tab.scrollToPosition(position);
                tabAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @OnClick({R.id.iv_black,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                VideoSearchActivity.start(mContext);
                break;
        }
    }



    private VideoListFragment createVideoListFragments(String id) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {
        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setId("-1");
        homeMenuBean.setName("免费专区");
        HomeMenuBean homeMenuBean1 = new HomeMenuBean();
        homeMenuBean1.setId("-2");
        homeMenuBean1.setName("付费视频");
        tabBeanList.add(homeMenuBean);
        tabBeanList.add(homeMenuBean1);
        this.tabBeanList.addAll(homeMenuBeanList);
        initTabViews();
    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList, int pages) {

    }

    @Override
    public void onGetZhVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoDetailSuccess(VideoModel videoModel, List<VideoModel> videoModelList,List<SheetMusicBean> sheetMusicBeans) {

    }

    @Override
    public void onUnlockVideoSuccess() {

    }

    @Override
    public void onFocusSuccess(String isFocus) {

    }

    @Override
    public void onCollectSuccess(String isCollect) {

    }

    @Override
    public void onBuypiaonscoreSuccess() {

    }

    @Override
    public void onFailed(String message) {

    }

    class MainPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{};

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }


}

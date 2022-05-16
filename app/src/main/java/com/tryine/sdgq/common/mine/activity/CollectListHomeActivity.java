package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.common.mine.adapter.TeacherTabAdapter;
import com.tryine.sdgq.common.mine.fragment.CollectCircleFragment;
import com.tryine.sdgq.common.mine.fragment.CollectGoodFragment;
import com.tryine.sdgq.common.mine.fragment.CollectVideoFragment;
import com.tryine.sdgq.common.mine.fragment.TyCardListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的收藏首页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 11:22
 */
public class CollectListHomeActivity extends BaseActivity {
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter2 teacherTabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CollectListHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_collect;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tabBeanList.add("视频");
        tabBeanList.add("琴友圈");
        tabBeanList.add("商品");

        teacherTabAdapter = new TabAdapter2(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(teacherTabAdapter);


        teacherTabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                teacherTabAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });

        fragmentManager = getSupportFragmentManager();


        CollectVideoFragment collectVideoFragment = new CollectVideoFragment();
        CollectCircleFragment collectCircleFragment = new CollectCircleFragment();
        CollectGoodFragment collectGoodFragment = new CollectGoodFragment();
        fragmentList.add(collectVideoFragment);
        fragmentList.add(collectCircleFragment);
        fragmentList.add(collectGoodFragment);

        vpView.setAdapter(new MainPagerAdapter(fragmentManager));
        vpView.setCurrentItem(0);
        vpView.setOffscreenPageLimit(20);
        vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
                teacherTabAdapter.notifyDataSetChanged();
                rv_tab.scrollToPosition(position);
                teacherTabAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
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

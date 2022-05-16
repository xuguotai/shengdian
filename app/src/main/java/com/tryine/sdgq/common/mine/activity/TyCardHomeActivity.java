package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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
import com.tryine.sdgq.common.home.activity.CampusListActivity;
import com.tryine.sdgq.common.home.fragment.CampusListFragment;
import com.tryine.sdgq.common.mine.adapter.TeacherTabAdapter;
import com.tryine.sdgq.common.mine.fragment.TyCardListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的体验卡首页
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:01
 */
public class TyCardHomeActivity extends BaseActivity {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TeacherTabAdapter teacherTabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TyCardHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tycardhome;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tabBeanList.add("待使用");
        tabBeanList.add("使用中");
        tabBeanList.add("已使用");
        tabBeanList.add("已转赠");

        teacherTabAdapter = new TeacherTabAdapter(this, tabBeanList);
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

        fragmentList.add(createTyCardListFragments(0));
        fragmentList.add(createTyCardListFragments(1));
        fragmentList.add(createTyCardListFragments(3));
        fragmentList.add(createTyCardListFragments(4));

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


    private TyCardListFragment createTyCardListFragments(int status) {
        TyCardListFragment fragment = new TyCardListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
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
    @OnClick({R.id.iv_black,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                TyCardSearchActivity.start(mContext);
                break;
        }
    }


}

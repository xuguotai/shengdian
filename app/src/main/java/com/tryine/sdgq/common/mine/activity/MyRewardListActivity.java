package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.fragment.CampusListFragment;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.common.mine.fragment.CollectCircleFragment;
import com.tryine.sdgq.common.mine.fragment.CollectVideoFragment;
import com.tryine.sdgq.common.mine.fragment.RewardFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的打赏/我的礼物
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 14:29
 */
public class MyRewardListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter2 teacherTabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    String type = "0";//0.我的打赏 1.我的礼物

    public static void start(Context context, String type) {
        Intent intent = new Intent();
        intent.setClass(context, MyRewardListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reward_list;
    }

    @Override
    protected void init() {
        setWhiteBar();

        type = getIntent().getStringExtra("type");

        tv_title.setText("0".equals(type) ? "我的打赏" : "我的礼物");

        tabBeanList.add("SD金豆");
        tabBeanList.add("SD银豆");

        teacherTabAdapter = new TabAdapter2(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(teacherTabAdapter);


        teacherTabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                teacherTabAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });

        fragmentManager = getSupportFragmentManager();

        fragmentList.add(createRewardFragments(type, "0"));
        fragmentList.add(createRewardFragments(type, "1"));


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

    private RewardFragment createRewardFragments(String type, String giftType) {
        RewardFragment fragment = new RewardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("giftType", giftType);
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


}

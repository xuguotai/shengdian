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
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mall.fragment.OrderListFragment;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.common.mine.fragment.MineBargainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 砍价订单首页
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 10:24
 */
public class BargainOrderListHomeActivity extends BaseActivity {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter2 tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BargainOrderListHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tabBeanList.add("全部");
        tabBeanList.add("砍价中");
        tabBeanList.add("待付款");
        tabBeanList.add("待核销");
        tabBeanList.add("已完成");

        //单状态 订单状态 0-全部 1-砍价中 2-待付款 3-待核销 4-已完成
        tabAdapter = new TabAdapter2(this, tabBeanList);
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
            fragmentList.add(createMineBargainFragments(i + ""));
        }

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
                tabAdapter.notifyDataSetChanged();
                rv_tab.scrollToPosition(position);
                tabAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private MineBargainFragment createMineBargainFragments(String status) {
        MineBargainFragment fragment = new MineBargainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick({R.id.iv_black,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                BargainOrderSearchActivity.start(mContext);
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

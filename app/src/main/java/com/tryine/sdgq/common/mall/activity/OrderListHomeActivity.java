package com.tryine.sdgq.common.mall.activity;

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
import com.tryine.sdgq.common.home.activity.CampusListActivity;
import com.tryine.sdgq.common.home.fragment.CampusListFragment;
import com.tryine.sdgq.common.mall.fragment.OrderListFragment;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城订单首页
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 10:24
 */
public class OrderListHomeActivity extends BaseActivity {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter2 tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, OrderListHomeActivity.class);
        intent.putExtra("type", type);
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
        tabBeanList.add("待付款");
        tabBeanList.add("待提货");
        tabBeanList.add("待评价");
        tabBeanList.add("已完成");

        int type = getIntent().getIntExtra("type",0);

        //单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)

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

        fragmentList.add(createOrderListFragments("-1"));
        fragmentList.add(createOrderListFragments("1"));
        fragmentList.add(createOrderListFragments("8"));
        fragmentList.add(createOrderListFragments("4"));
        fragmentList.add(createOrderListFragments("5"));

        vpView.setAdapter(new MainPagerAdapter(fragmentManager));
        vpView.setOffscreenPageLimit(20);
        vpView.setCurrentItem(type);
        tabAdapter.setSelectedTabPosition(type);
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


    private OrderListFragment createOrderListFragments(String status) {
        OrderListFragment fragment = new OrderListFragment();
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
                OrderSearchActivity.start(mContext);
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

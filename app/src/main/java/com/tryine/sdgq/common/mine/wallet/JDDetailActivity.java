package com.tryine.sdgq.common.mine.wallet;

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
import com.tryine.sdgq.common.mine.adapter.JDRechargeAdapter;
import com.tryine.sdgq.common.mine.adapter.TabAdapter;
import com.tryine.sdgq.common.mine.fragment.JDDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 金豆明细
 *
 * @author: zhangshuaijun
 * @time: 2021-11-22 09:31
 */
public class JDDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;


    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    int type = 0;

    public static void start(Context context,int type) {
        Intent intent = new Intent();
        intent.setClass(context, JDDetailActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jd_details;
    }


    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("明细");
        type = getIntent().getIntExtra("type",0);

        tabBeanList.add("SD金豆");
        tabBeanList.add("SD银豆");
        tabAdapter = new TabAdapter(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(tabAdapter);
        tabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });
        tabAdapter.setSelectedTabPosition(type);


        fragmentManager = getSupportFragmentManager();

        for (int i = 0; i < tabBeanList.size(); i++) {
            fragmentList.add(createJDDetailFragments(i + ""));
        }

        vpView.setAdapter(new MainPagerAdapter(fragmentManager));
        vpView.setCurrentItem(type);
        vpView.setOffscreenPageLimit(20);
        vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
                tabAdapter.notifyDataSetChanged();
                tabAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private JDDetailFragment createJDDetailFragments(String id) {
        JDDetailFragment fragment = new JDDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("beanType", id);
        fragment.setArguments(bundle);
        return fragment;
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

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
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.common.mine.adapter.TeacherTabAdapter;
import com.tryine.sdgq.common.mine.fragment.FansListFragment;
import com.tryine.sdgq.common.mine.fragment.FansListFragment1;
import com.tryine.sdgq.common.mine.fragment.TyCardListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的圈子
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:01
 */
public class MineCircleHomeActivity extends BaseActivity {
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

    String userId;
    String selectType;

    public static void start(Context context, String userId,String selectType) {
        Intent intent = new Intent();
        intent.setClass(context, MineCircleHomeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("selectType", selectType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_minecirclehome;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tabBeanList.add("关注");
        tabBeanList.add("粉丝");
        userId = getIntent().getStringExtra("userId");
        selectType = getIntent().getStringExtra("selectType");
        if(selectType.equals("0")){
            tv_title.setText("我的圈子");
        }else{
            tv_title.setText("Ta的圈子");
        }

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

        FansListFragment fragment = new FansListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "0");
        bundle.putString("userId", userId);
        bundle.putString("selectType", selectType);
        fragment.setArguments(bundle);

        FansListFragment1 fragment1 = new FansListFragment1();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type", "1");
        bundle1.putString("userId", userId);
        bundle1.putString("selectType", selectType);
        fragment1.setArguments(bundle1);

        fragmentList.add(fragment);
        fragmentList.add(fragment1);

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


    private FansListFragment createFansListFragments(String type) {
        FansListFragment fragment = new FansListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("userId", userId);
        bundle.putString("selectType", selectType);
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

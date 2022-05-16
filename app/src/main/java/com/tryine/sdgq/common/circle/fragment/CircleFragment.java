package com.tryine.sdgq.common.circle.fragment;

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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.CircleSearchHomeActivity;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.mine.adapter.TabAdapter;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴友圈
 *
 * @author: zhangshuaijun
 * @time: 2021-11-17 10:30
 */
public class CircleFragment extends BaseFragment {

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter tabAdapter;
    List<String> tabBeanList = new ArrayList<String>();

    ArrayList<Fragment> mNewsFragmentList = new ArrayList<>();
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        initViews();
    }

    private void initViews() {
        tabBeanList.add("发现");
        tabBeanList.add("关注");
        tabBeanList.add("话题");
        tabAdapter = new TabAdapter(getContext(), tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(tabAdapter);
        tabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabAdapter.setSelectedTabPosition(position);
                viewPager.setCurrentItem(position);
            }
        });


        fragmentManager = getChildFragmentManager();

        FindFragment findFragment = new FindFragment();
        Bundle bundle = new Bundle();
        bundle.putString("moduleType", "0");
        findFragment.setArguments(bundle);
        FindFragment findFragment1 = new FindFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("moduleType", "1");
        findFragment1.setArguments(bundle1);
        TopicFragment findFragment2 = new TopicFragment();
        mNewsFragmentList.add(findFragment);
        mNewsFragmentList.add(findFragment1);
        mNewsFragmentList.add(findFragment2);

        viewPager.setAdapter(new MainPagerAdapter(fragmentManager));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(20);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @OnClick({R.id.iv_search, R.id.iv_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                CircleSearchHomeActivity.start(getContext());
                break;
            case R.id.iv_personal:
                PersonalHomePageActivity.start(getContext(), SPUtils.getString(Parameter.USER_ID));
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    class MainPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{};

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mNewsFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return mNewsFragmentList == null ? 0 : mNewsFragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setWhiteBar();
        }

    }


}

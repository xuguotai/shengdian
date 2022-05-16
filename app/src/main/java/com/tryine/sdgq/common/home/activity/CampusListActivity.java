package com.tryine.sdgq.common.home.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.home.fragment.CampusListFragment;
import com.tryine.sdgq.common.home.fragment.VideoListFragment;
import com.tryine.sdgq.common.mine.adapter.TabAdapter1;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.util.NavUtil;
import com.tryine.sdgq.util.PermissionsUtils;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 校区展示列表
 * @author: zhangshuaijun
 * @time: 2021-11-25 10:35
 */
public class CampusListActivity extends BaseActivity {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabAdapter2 tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    //获取定位
    private String[] permissions_location = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CampusListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_campus_list;
    }

    @Override
    protected void init() {
        setWhiteBar();

        PermissionsUtils.getInstance().chekPermissions(this, permissions_location, permissionsResult);

        tabBeanList.add("全部");
        tabBeanList.add("北京");
        tabBeanList.add("长沙");
        tabBeanList.add("武汉");

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
            fragmentList.add(createCampusListFragments(i + "",tabBeanList.get(i)));
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

    @OnClick({R.id.iv_black,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                CampusSearchActivity.start(mContext);
                break;
        }
    }


    private CampusListFragment createCampusListFragments(String id,String city) {
        CampusListFragment fragment = new CampusListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeId", id);
        bundle.putString("city", city);
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


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e("wy", "run: " + "用户拒绝授权");
            } else {
                //获取权限成功提示，可以不要
                Log.e("wy", "run: " + "用户已授权");
//                initLocation();
                NavUtil.gaode(CampusListActivity.this);
            }
        }
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //权限通过，可以做其他事情
            NavUtil.gaode(CampusListActivity.this);
        }

        @Override
        public void forbitPermissons() {
            ToastUtil.toastLongMessage("权限不通过");
        }
    };


}

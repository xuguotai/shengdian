package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.CourseListAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.fragment.BuyCourseHomeFragment;
import com.tryine.sdgq.common.home.fragment.CourseListFragment;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.common.live.activity.LiveTypeListActivity;
import com.tryine.sdgq.common.live.fragment.LiveListFragment;
import com.tryine.sdgq.common.live.presenter.LiveHomePresenter;
import com.tryine.sdgq.common.mine.adapter.TabAdapter1;
import com.tryine.sdgq.common.mine.fragment.JDDetailFragment;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;
import com.tryine.sdgq.view.dialog.VideoTypeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买课程首页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class BuyCourseHomeActivity extends BaseActivity implements CourseView {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<HomeMenuBean> tabBeanList = new ArrayList<HomeMenuBean>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;
    TabAdapter1 tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    int position = 0;

    List<CampusBean> campusBeanLists = new ArrayList<>();
    CampusBean selectCampusBean;//选中的校区

    CoursePresenter coursePresenter;

    String receiptId;

    public static void start(Context context, String receiptId) {
        Intent intent = new Intent();
        intent.setClass(context, BuyCourseHomeActivity.class);
        intent.putExtra("receiptId", receiptId);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BuyCourseHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coursehome_buy;
    }

    @Override
    protected void init() {
        receiptId = getIntent().getStringExtra("receiptId");
        setWhiteBar();

        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getFicationList();

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
            fragmentList.add(createFragments(tabBeanList.get(i).getId(),selectCampusBean.getId()));
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


    @OnClick({R.id.iv_black, R.id.tv_qhxq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_qhxq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(this, campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_qhxq.setText(homeMenuBean.getName());
                            for (int i = 0; i < fragmentList.size(); i++) {
                                BuyCourseHomeFragment b = (BuyCourseHomeFragment) fragmentList.get(i);
                                b.refres(selectCampusBean.getId());
                            }
                        }
                    });
                }
                break;
        }
    }

    private BuyCourseHomeFragment createFragments(String id, String receiptId) {
        BuyCourseHomeFragment fragment = new BuyCourseHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeId", id);
        bundle.putString("receiptId", receiptId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
        if (TextUtils.isEmpty(receiptId)) {

            if(TextUtils.isEmpty(SPUtils.getString("receiptId"))){
                //默认取第一个校区
                if (null != campusBeanLists && campusBeanLists.size() > 0) {
                    selectCampusBean = campusBeanLists.get(0);
                    tv_qhxq.setText(selectCampusBean.getName());
                }
            }else{
                for (CampusBean campusBean : campusBeanList) {
                    if (campusBean.getId().equals(SPUtils.getString("receiptId"))) {
                        this.selectCampusBean = campusBean;
                        tv_qhxq.setText(selectCampusBean.getName());
                    }
                }
            }
        } else {
            for (CampusBean campusBean : campusBeanList) {
                if (campusBean.getId().equals(receiptId)) {
                    this.selectCampusBean = campusBean;
                    tv_qhxq.setText(selectCampusBean.getName());
                }
            }
        }
        coursePresenter.getCourescatslist();
    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }

    @Override
    public void onGetcancelledSuccess(int count, int positions) {

    }

    @Override
    public void onAddCampusSuccess() {

    }

    @Override
    public void onAddCourseDataSuccess() {

    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {
        this.tabBeanList.addAll(homeMenuBeanList);
        initTabViews();
    }

    @Override
    public void onselectsuspendedSuccess(int selectsuspended, int positions) {

    }

    @Override
    public void onSuspendedSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
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

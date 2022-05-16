package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tryine.sdgq.Application;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.PersonalHomePageLabelAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.chat.ChatActivity;
import com.tryine.sdgq.common.circle.fragment.PersonalPageFragment;
import com.tryine.sdgq.common.circle.fragment.PersonalPageTopicFragment;
import com.tryine.sdgq.common.circle.fragment.PersonalPageVideoFragment;
import com.tryine.sdgq.common.circle.presenter.PersonalPresenter;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.activity.MineCircleHomeActivity;
import com.tryine.sdgq.common.mine.adapter.TeacherTabAdapter;
import com.tryine.sdgq.common.mine.fragment.CollectVideoFragment;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.GradeTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人主页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 16:29
 */
public class PersonalHomePageActivity extends BaseActivity implements PersonalView {
    @BindView(R.id.tv_jb)
    TextView iv_jb;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_userName)
    TextView tv_userName;
    @BindView(R.id.tv_dj)
    GradeTextView tv_dj;
    @BindView(R.id.tv_focusCount)
    TextView tv_focusCount;
    @BindView(R.id.ll_gz)
    LinearLayout ll_gz;
    @BindView(R.id.tv_gz)
    TextView tv_gz;
    @BindView(R.id.tv_sx)
    TextView tv_sx;
    @BindView(R.id.tv_userExplain)
    TextView tv_userExplain;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;

    @BindView(R.id.rv_tabbtn)
    public RecyclerView rv_tabbtn;
    PersonalHomePageLabelAdapter personalHomePageLabelAdapter;


    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> tabBeanList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TeacherTabAdapter teacherTabAdapter;

    PersonalPresenter personalPresenter;

    @BindView(R.id.vp_view)
    ViewPager vpView;
    String userId;

    PersonalBean personalBean;

    public static void start(Context context, String userId) {
        Intent intent = new Intent();
        intent.setClass(context, PersonalHomePageActivity.class);
        intent.putExtra("userId", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_homepage;
    }

    @Override
    protected void init() {
        setWhiteBar();
        userId = getIntent().getStringExtra("userId");

        personalPresenter = new PersonalPresenter(this);
        personalPresenter.attachView(this);

        if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
            personalPresenter.getUserHomeInfo("0", userId);
        } else {
            personalPresenter.getUserHomeInfo("1", userId);
        }

        tabBeanList.add("主页");
        tabBeanList.add("视频");
        tabBeanList.add("话题");

        teacherTabAdapter = new TeacherTabAdapter(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(teacherTabAdapter);


        teacherTabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                teacherTabAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });

        fragmentManager = getSupportFragmentManager();

        PersonalPageFragment personalPageFragment = new PersonalPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("selectType", "0");
        personalPageFragment.setArguments(bundle);

        PersonalPageVideoFragment personalPageVideoFragment = new PersonalPageVideoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("userId", userId);
        bundle1.putString("selectType", "0");
        personalPageVideoFragment.setArguments(bundle1);

        PersonalPageTopicFragment personalPageTopicFragment = new PersonalPageTopicFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("userId", userId);
        bundle2.putString("selectType", "0");
        personalPageTopicFragment.setArguments(bundle2);

        fragmentList.add(personalPageFragment);
        fragmentList.add(personalPageVideoFragment);
        fragmentList.add(personalPageTopicFragment);


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


    private void initUserInfo(PersonalBean personalBean) {

        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, personalBean.getAvatar(), iv_head);
        tv_userName.setText(personalBean.getUserName());
        if(!TextUtils.isEmpty(personalBean.getLevel())){
            tv_dj.setData(Integer.parseInt(personalBean.getLevel()));
        }else{
            tv_dj.setData(0);
        }
        tv_focusCount.setText("关注 " + personalBean.getFocusCount() + "  粉丝 " + personalBean.getFansCount());

        if (!TextUtils.isEmpty(personalBean.getUserExplain())) {
            tv_userExplain.setText(personalBean.getUserExplain());
        } else {
            tv_userExplain.setText("这个人很懒，什么也没留下。");
        }

        if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
            iv_setting.setVisibility(View.VISIBLE);

        } else { //他人
            ll_gz.setVisibility(View.VISIBLE);
            iv_jb.setVisibility(View.VISIBLE);
            if ("0".equals(personalBean.getIsReceive())) {
                tv_sx.setVisibility(View.GONE);
            } else {
                tv_sx.setVisibility(View.VISIBLE);
            }

            if ("0".equals(personalBean.getIsFocus())) {
                tv_gz.setText("+ 关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yy);
            } else {
                tv_gz.setText("已关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
            }

            if ("1".equals(personalBean.getIsShowLabel())) { //显示标签
                rv_tabbtn.setVisibility(View.VISIBLE);
            } else {
                rv_tabbtn.setVisibility(View.GONE);
            }

        }

        personalHomePageLabelAdapter = new PersonalHomePageLabelAdapter(this, personalBean.getLabelList());
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager1.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        rv_tabbtn.setLayoutManager(flexboxLayoutManager1);
        rv_tabbtn.setAdapter(personalHomePageLabelAdapter);


    }

    @OnClick({R.id.iv_black, R.id.iv_setting, R.id.tv_gz, R.id.tv_focusCount, R.id.tv_sx, R.id.tv_jb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_focusCount:
                if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
                    MineCircleHomeActivity.start(mContext, userId, "0");
                } else {
                    MineCircleHomeActivity.start(mContext, userId, "1");
                }

                break;
            case R.id.iv_setting:
                PersonalSettingActivity.start(mContext, personalBean);
                break;
            case R.id.tv_gz:
                if (null != personalBean) {
                    personalPresenter.setFocus(personalBean.getUserId(), personalBean.getIsFocus());

                }
                break;
            case R.id.tv_sx:
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(personalBean.getUserId());
                String chatName = personalBean.getUserId();
                if (!TextUtils.isEmpty(personalBean.getUserName())) {
                    chatName = personalBean.getUserName();
                } else if (!TextUtils.isEmpty(personalBean.getUserName())) {
                    chatName = personalBean.getUserName();
                }
                chatInfo.setChatName(chatName);
                chatInfo.setHeadImgage(personalBean.getAvatar());
                Intent intenta = new Intent(this, ChatActivity.class);
                intenta.putExtra("chatInfo", chatInfo);
                intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Application.getApplication().startActivity(intenta);
                break;
            case R.id.tv_jb:
                if (null != personalBean) {
                    ReportActivity.start(mContext, personalBean.getUserId());
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
            personalPresenter.getUserHomeInfo("0", userId);
        } else {
            personalPresenter.getUserHomeInfo("1", userId);
        }
    }

    @Override
    public void onGetPersonaBeanSuccess(PersonalBean personalBean) {
        if (null != personalBean) {
            this.personalBean = personalBean;
            initUserInfo(personalBean);
        }

    }

    @Override
    public void onGetLabelListSuccess(List<LabelBean> labelBeanList) {

    }

    @Override
    public void onUpdateUserInfoSuccess() {

    }

    @Override
    public void onUpdateLabelSuccess() {

    }

    @Override
    public void onFocusSuccess() {
        if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
            personalPresenter.getUserHomeInfo("0", userId);
        } else {
            personalPresenter.getUserHomeInfo("1", userId);
        }
    }

    @Override
    public void onDeletePyqSuccess() {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetTopicBeanListSuccess(List<TopicBean> topicBeanList, int pages) {

    }

    @Override
    public void onGetVideoBeanListSuccess(List<VideoModel> videoModels, int pages) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {

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

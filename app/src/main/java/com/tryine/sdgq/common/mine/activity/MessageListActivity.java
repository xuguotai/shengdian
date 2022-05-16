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
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.adapter.TabAdapter2;
import com.tryine.sdgq.common.mine.adapter.TabMessageAdapter;
import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.common.mine.fragment.CollectCircleFragment;
import com.tryine.sdgq.common.mine.fragment.CollectVideoFragment;
import com.tryine.sdgq.common.mine.fragment.MessageFragment;
import com.tryine.sdgq.common.mine.fragment.MineFragment;
import com.tryine.sdgq.common.mine.fragment.PrivateLetterFragment;
import com.tryine.sdgq.common.mine.presenter.MessagePresenter;
import com.tryine.sdgq.common.mine.view.MessageView;
import com.tryine.sdgq.event.IMMessageCountEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的消息
 *
 * @author: zhangshuaijun
 * @time: 2022-01-24 10:32
 */
public class MessageListActivity extends BaseActivity implements MessageView {
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    List<HomeMenuBean> tabBeanList = new ArrayList<>();
    private FragmentManager fragmentManager;
    int selectedTabPosition = 0;

    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TabMessageAdapter tabMessageAdapter;

    @BindView(R.id.tv_msgCount)
    TextView tv_msgCount;
    @BindView(R.id.vp_view)
    ViewPager vpView;

    MessagePresenter messagePresenter;

    public int IMmessageCount;//私信消息总数
    int sysSum = 0;
    int thumbSum = 0;
    int shareSum = 0;
    int commentsSum = 0;
    int exceptionalSum = 0;

    MessageFragment messageFragment,messageFragment1;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_message;
    }

    @Override
    protected void init() {
        IMmessageCount = ConversationManagerKit.getInstance().getUnreadTotal();
        showChatMessageCount();
        initViews();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(MessageListActivity.this);
        }
        messagePresenter = new MessagePresenter(mContext);
        messagePresenter.attachView(this);
        messagePresenter.getMessagenumber();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        messagePresenter.getMessagenumber();
    }

    private void showChatMessageCount(){
        tv_msgCount.setText(IMmessageCount + "条未读消息");

    }

    private void initViews() {

        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setName("系统消息");
        homeMenuBean.setCount(0);
        tabBeanList.add(homeMenuBean);

        HomeMenuBean homeMenuBean1 = new HomeMenuBean();
        homeMenuBean1.setName("好友消息");
        homeMenuBean1.setCount(IMmessageCount);
        tabBeanList.add(homeMenuBean1);

        HomeMenuBean homeMenuBean2 = new HomeMenuBean();
        homeMenuBean2.setName("赞享评赏");
        homeMenuBean2.setCount(0);
        tabBeanList.add(homeMenuBean2);

        tabMessageAdapter = new TabMessageAdapter(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(tabMessageAdapter);


        tabMessageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabMessageAdapter.setSelectedTabPosition(position);
                vpView.setCurrentItem(position);
            }
        });

        fragmentManager = getSupportFragmentManager();


        messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resourcesType", 0);
        messageFragment.setArguments(bundle);

        PrivateLetterFragment privateLetterFragment = new PrivateLetterFragment();

        messageFragment1 = new MessageFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("resourcesType", 1);
        messageFragment1.setArguments(bundle1);
        fragmentList.add(messageFragment);
        fragmentList.add(privateLetterFragment);
        fragmentList.add(messageFragment1);

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
                tabMessageAdapter.notifyDataSetChanged();
                rv_tab.scrollToPosition(position);
                tabMessageAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @OnClick({R.id.iv_black, R.id.tv_yjyd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_yjyd:
                V2TIMManager.getMessageManager().markC2CMessageAsRead(null, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
                messagePresenter.messageread();
                break;
        }
    }

    @Override
    public void onGetMessageBeanListSuccess(List<MessageBean> messageBeanList, int pages) {

    }

    @Override
    public void onGetMessagenumberSuccess(int sysSum, int thumbSum, int shareSum, int commentsSum, int exceptionalSum) {
        tabBeanList.get(0).setCount(sysSum);

        this.sysSum = sysSum;
        this.thumbSum = thumbSum;
        this.shareSum = shareSum;
        this.commentsSum = commentsSum;
        this.exceptionalSum = exceptionalSum;
        tabBeanList.get(2).setCount(thumbSum + shareSum + commentsSum + exceptionalSum);
        tv_msgCount.setText(sysSum + thumbSum + shareSum + commentsSum + exceptionalSum + IMmessageCount + "条未读消息");
        tabMessageAdapter.notifyDataSetChanged();

        messageFragment.refresh();
        messageFragment1.refresh();
    }

    @Override
    public void onAllReadSuccess() {
        messagePresenter.getMessagenumber();
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

    /**
     * 私信消息总数量监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(IMMessageCountEvent event) {
        IMmessageCount = event.count;
        tabBeanList.get(1).setCount(IMmessageCount);

        int messageCount = sysSum + thumbSum + shareSum + commentsSum + exceptionalSum + IMmessageCount;
        if (messageCount > 100) {
            tv_msgCount.setText("99+条未读消息");
        } else {
            tv_msgCount.setText(sysSum + thumbSum + shareSum + commentsSum + exceptionalSum + IMmessageCount + "条未读消息");
        }
        tabMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MessageListActivity.this);
        //清空粘性事件的缓存
        EventBus.getDefault().removeAllStickyEvents();
    }

}

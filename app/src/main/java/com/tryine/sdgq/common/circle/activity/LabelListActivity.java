package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.LabelListAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.PersonalPresenter;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.activity.CampusDetailActivity;
import com.tryine.sdgq.common.home.activity.NoticeListActivity;
import com.tryine.sdgq.common.home.adapter.NoticeListAdapter;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.AddLabelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑标签
 *
 * @author: zhangshuaijun
 * @time: 2021-12-02 15:47
 */
public class LabelListActivity extends BaseActivity implements PersonalView {

    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.iv_bianji)
    ImageView iv_bianji;
    @BindView(R.id.tv_bianji)
    TextView tv_bianji;

    List<LabelBean> labelBeanLists = new ArrayList<>();
    LabelListAdapter labelListAdapter;


    PersonalPresenter personalPresenter;

    int type = 0;//

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LabelListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_label_list;
    }

    @Override
    protected void init() {
        setWhiteBar();

        personalPresenter = new PersonalPresenter(this);
        personalPresenter.attachView(this);
        personalPresenter.getLabelList();

        initViews();
    }


    protected void initViews() {
        setWhiteBar();

        labelListAdapter = new LabelListAdapter(this, labelBeanLists);
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager1.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        rc_data.setLayoutManager(flexboxLayoutManager1);
        rc_data.setAdapter(labelListAdapter);
        labelListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                personalPresenter.deleteLabel(labelBeanLists.get(position).getId());
            }
        });

    }


    @OnClick({R.id.iv_black, R.id.iv_bianji, R.id.tv_bianji, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_bianji:
                if (tv_bianji.getVisibility() == View.GONE) {
                    iv_bianji.setVisibility(View.GONE);
                    tv_bianji.setVisibility(View.VISIBLE);
                    tv_submit.setVisibility(View.VISIBLE);
                    type = 1;
                    labelListAdapter.setType(type);
                }


                break;
            case R.id.tv_bianji:
                if (iv_bianji.getVisibility() == View.GONE) {
                    iv_bianji.setVisibility(View.VISIBLE);
                    tv_bianji.setVisibility(View.GONE);
                    tv_submit.setVisibility(View.GONE);
                    type = 0;
                    labelListAdapter.setType(type);
                }

                break;
            case R.id.tv_submit:
                if (null != labelBeanLists && labelBeanLists.size() >= 5) {
                    ToastUtil.toastLongMessage("最多可以添加5个标签");
                    return;
                }

                AddLabelDialog addLabelDialog = new AddLabelDialog(mContext, R.style.dialog_center);
                addLabelDialog.show();
                addLabelDialog.setmOnTextSendListener(new AddLabelDialog.OnTextSendListener() {
                    @Override
                    public void onMoney(String money) {
                        personalPresenter.addLabel(money);
                    }

                    @Override
                    public void dismiss() {

                    }
                });

                break;
        }
    }

    @Override
    public void onGetPersonaBeanSuccess(PersonalBean personalBean) {

    }

    @Override
    public void onGetLabelListSuccess(List<LabelBean> labelBeanList) {
        this.labelBeanLists.clear();
        this.labelBeanLists.addAll(labelBeanList);
        labelListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onUpdateUserInfoSuccess() {
    }

    @Override
    public void onUpdateLabelSuccess() {
        personalPresenter.getLabelList();
    }

    @Override
    public void onFocusSuccess() {

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
}

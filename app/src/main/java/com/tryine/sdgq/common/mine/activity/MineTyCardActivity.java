package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.activity.AllCourseActivity;
import com.tryine.sdgq.common.home.activity.CampusDetailActivity;
import com.tryine.sdgq.common.mine.adapter.TyCardListAdapter;
import com.tryine.sdgq.common.mine.adapter.TyCardUseRecordListAdapter;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.common.mine.presenter.TyCardPresenter;
import com.tryine.sdgq.common.mine.view.TyCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的体验卡
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 10:19
 */
public class MineTyCardActivity extends BaseActivity implements TyCardView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;
    @BindView(R.id.tv_couresName)
    TextView tv_couresName;
    @BindView(R.id.tv_experienceClass)
    TextView tv_experienceClass;
    @BindView(R.id.tv_experienceLayer)
    TextView tv_experienceLayer;
    @BindView(R.id.tv_dete)
    TextView tv_dete;
    @BindView(R.id.tv_experienceClass1)
    TextView tv_experienceClass1;
    @BindView(R.id.ll_wsy)
    LinearLayout ll_wsy;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.ll_syjl)
    LinearLayout ll_syjl;
    @BindView(R.id.ll_zzhy)
    LinearLayout ll_zzhy;

    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_zzDate)
    TextView tv_zzDate;

    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    List<ExperienceBean> experienceBeanLists = new ArrayList<>();
    TyCardUseRecordListAdapter tyCardUseRecordListAdapter;

    CardBean cardBean;

    TyCardPresenter tyCardPresenter;


    public static void start(Context context, CardBean cardBean) {
        Intent intent = new Intent();
        intent.setClass(context, MineTyCardActivity.class);
        intent.putExtra("cardBean", cardBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tycard_mine;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("我的体验卡");
        tyCardPresenter = new TyCardPresenter(this);
        tyCardPresenter.attachView(this);

        cardBean = (CardBean) getIntent().getSerializableExtra("cardBean");

        tyCardPresenter.getexperiencelist(cardBean.getId());

        //状态 0:未开始 1:使用中2：暂停使用3：已使用 4、转赠好友
        if (cardBean.getStatus().equals("0")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_dsybg);
            ll_wsy.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.VISIBLE);
        } else if (cardBean.getStatus().equals("1")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_syzbg);
            ll_wsy.setVisibility(View.VISIBLE);
            ll_syjl.setVisibility(View.VISIBLE);
        } else if (cardBean.getStatus().equals("3")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_ysybg);
            ll_wsy.setVisibility(View.VISIBLE);
            ll_syjl.setVisibility(View.VISIBLE);
        } else if (cardBean.getStatus().equals("4")) {
            ll_zzhy.setVisibility(View.VISIBLE);
            ll_bg.setBackgroundResource(R.mipmap.ic_card_yzzbg);
        }

        tv_couresName.setText("包含课程：" + cardBean.getCouresName());
        tv_experienceClass.setText("课 时：" + cardBean.getExperienceClass());
        tv_experienceLayer.setText(cardBean.getExperienceLayer());
        tv_experienceClass1.setText(cardBean.getExperienceClass());
        tv_dete.setText("体验期限：" + cardBean.getStartTime() + " 至 " + cardBean.getEndTime());

        tv_mobile.setText(cardBean.getMobile());
        tv_zzDate.setText(cardBean.getUpdateTime());


        tyCardUseRecordListAdapter = new TyCardUseRecordListAdapter(this, experienceBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(tyCardUseRecordListAdapter);
        tyCardUseRecordListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

    }

    @OnClick({R.id.iv_black, R.id.tv_zzhy, R.id.tv_ljsy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_zzhy:
                TyCardZZActivity.start(mContext, cardBean.getId());
                break;
            case R.id.tv_ljsy:
                AllCourseActivity.start(mContext, cardBean.getId());
                break;

        }
    }

    @Override
    public void onGetCardBeanListSuccess(List<CardBean> cardBeanList, int pages) {

    }

    @Override
    public void onGetExperienceBeanSuccess(List<ExperienceBean> experienceBeanList, int pages) {
        experienceBeanLists.clear();
        experienceBeanLists.addAll(experienceBeanList);
        tyCardUseRecordListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetCardBeanListSuccess() {

    }

    @Override
    public void onForwardingSuccess() {

    }

    @Override
    public void onFailed(String message) {

    }
}

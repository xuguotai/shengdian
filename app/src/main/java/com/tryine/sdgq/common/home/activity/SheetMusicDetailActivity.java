package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.SheetMusicVideoAdapter;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SheetMusicPresenter;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.adapter.MallXlGoodsAdapter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴谱详情
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 16:40
 */
public class SheetMusicDetailActivity extends BaseActivity implements SheetMusicView {

    @BindView(R.id.iv_cover)
    RoundImageView iv_cover;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_silverBean)
    TextView tv_silverBean;
    @BindView(R.id.iv_content)
    ImageView iv_content;
    @BindView(R.id.tv_pageNum)
    TextView tv_pageNum;
    @BindView(R.id.tv_scoreDesc)
    TextView tv_scoreDesc;
    @BindView(R.id.ll_video)
    LinearLayout ll_video;


    @BindView(R.id.rv_videos)
    RecyclerView rv_videos;
    List<VideoModel> videoModelList = new ArrayList<>();
    SheetMusicVideoAdapter sheetMusicVideoAdapter;

    SheetMusicBean sheetMusicBean;

    String id;

    SheetMusicPresenter sheetMusicPresenter;

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, SheetMusicDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sheetmusic_detail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        id = getIntent().getStringExtra("id");

        sheetMusicPresenter = new SheetMusicPresenter(this);
        sheetMusicPresenter.attachView(this);
        sheetMusicPresenter.getpiaonscoredetail(id);
    }

    private void initViews() {
        GlideEngine.createGlideEngine().loadImage(mContext, sheetMusicBean.getCoverUrl(),iv_cover);
        tv_name.setText(sheetMusicBean.getName());
        tv_time.setText("发布时间：" + sheetMusicBean.getCreateTime());
        tv_silverBean.setText(sheetMusicBean.getSilverBean());

        String[] imgUrl = sheetMusicBean.getImgUrl().split(",");
        GlideEngine.createGlideEngine().loadImage(mContext, imgUrl[0], iv_content);
        tv_pageNum.setText("共 " + imgUrl.length + " 页");
        tv_scoreDesc.setText(sheetMusicBean.getScoreDesc());

        if (null != videoModelList && videoModelList.size() > 0) {
            sheetMusicVideoAdapter = new SheetMusicVideoAdapter(this, videoModelList);
            LinearLayoutManager lin = new LinearLayoutManager(this);
            lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
            rv_videos.setLayoutManager(lin);
            rv_videos.setAdapter(sheetMusicVideoAdapter);
            sheetMusicVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    SdVideoDetailActivity.start(mContext, videoModelList.get(position).getId());
                }
            });
            ll_video.setVisibility(View.VISIBLE);
        }else{
            ll_video.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_black, R.id.iv_qp, R.id.tv_gdsp,R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_qp:
                if (null == sheetMusicBean) {
                    sheetMusicPresenter.getpiaonscoredetail(id);
                    return;
                }

//                if (!sheetMusicBean.getIsUnlock().equals("1")) {
//                    PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
//                            "需支付" + sheetMusicBean.getSilverBean() + "银豆解锁", "确认", "取消");
//                    promptDialog.show();
//                    promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
//                        @Override
//                        public void insure() {
//                            sheetMusicPresenter.buypiaonscore(sheetMusicBean.getId());
//                        }
//
//                        @Override
//                        public void cancel() {
//
//                        }
//                    });
//                } else {
                    SheetMusicPageActivity.start(mContext, sheetMusicBean.getImgUrl());
//                }
                break;
            case R.id.tv_gdsp:
                SdSdHomeVideoHomeActivity.start(mContext);
                break;
            case R.id.iv_share:
                if (null != sheetMusicBean) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, sheetMusicBean.getId(), "1");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(SheetMusicDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;
        }
    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetChildMenuSuccess(List<HomeMenuBean> homeMenuBeanList, int pages) {

    }

    @Override
    public void onBuypiaonscoreSuccess() {
        ToastUtil.toastLongMessage("解锁成功");
        sheetMusicPresenter.getpiaonscoredetail(id);
    }

    @Override
    public void onGetpiaonscoredetailSuccess(SheetMusicBean sheetMusicBean, List<VideoModel> videoModelLists) {
        this.sheetMusicBean = sheetMusicBean;
        this.videoModelList.clear();
        this.videoModelList.addAll(videoModelLists);
        initViews();
    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

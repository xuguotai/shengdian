package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.just.agentweb.AgentWebView;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.activity.BargainHomeActivity;
import com.tryine.sdgq.common.home.activity.NoticeListActivity;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.GoodsCommentBean;
import com.tryine.sdgq.common.mall.presenter.GoodsDetailPresenter;
import com.tryine.sdgq.common.mall.view.GoodsDetailView;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.dialog.CampusDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 11:17
 */
public class GoodsDetailActivity extends BaseActivity implements GoodsDetailView {

    @BindView(R.id.banner)
    BannerViewPager bannerView;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_collect)
    ImageView iv_collect;
    @BindView(R.id.iv_price)
    ImageView iv_price;
    @BindView(R.id.tv_discountPrice)
    TextView tv_discountPrice;
    @BindView(R.id.tv_salesVolume)
    TextView tv_salesVolume;
    @BindView(R.id.wv_detail_content)
    AgentWebView wv_detail_content;

    GoodsBean goodsBean;

    GoodsDetailPresenter goodsDetailPresenter;
    String id;

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        id = getIntent().getStringExtra("id");
        goodsDetailPresenter = new GoodsDetailPresenter(this);
        goodsDetailPresenter.attachView(this);
        goodsDetailPresenter.getGoodsDetail(id);

    }


    private void initGoodsDatail(GoodsBean goodsBean) {
        if (null != goodsBean) {
            if (!TextUtils.isEmpty(goodsBean.getImgUrl())) {
                List<BannerBean> bannerBeanList = new ArrayList<>();
                String[] imgUrl = goodsBean.getImgUrl().split("!#!");
                for (int i = 0; i < imgUrl.length; i++) {
                    BannerBean bannerBean = new BannerBean();
                    bannerBean.setImgUrl(imgUrl[i]);
                    bannerBeanList.add(bannerBean);
                }
                initBanner(bannerBeanList);
            }

            tv_title.setText(goodsBean.getName());

            if (goodsBean.getBeanType() == 0) {
                iv_price.setBackgroundResource(R.mipmap.ic_jdz);
                tv_discountPrice.setText(goodsBean.getDiscountPrice() + " 金豆");
            } else {
                iv_price.setBackgroundResource(R.mipmap.ic_ydz);
                tv_discountPrice.setText(goodsBean.getDiscountPrice() + " 银豆");
            }

            tv_salesVolume.setText("销量 " + goodsBean.getSalesVolume());

            iv_collect.setBackgroundResource(goodsBean.getIscollection().equals("0") ? R.mipmap.ic_comment_sc : R.mipmap.ic_comment_sc_pre);

            setHtmlData(goodsBean.getDetail());
        }

    }

    private void initBanner(List<BannerBean> bannerBeanList) {
        bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(bannerBeanList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                .finishConfig()//这句必须加
                .addPoint(bannerBeanList.size())//添加指示器
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {

                    }
                });
    }


    @OnClick({R.id.iv_black, R.id.ll_addCar, R.id.tv_buy, R.id.iv_share, R.id.iv_collect,R.id.tv_ckpj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_addCar:
                if (null != goodsBean) {
                    if(goodsBean.getIsInput() == 1){
                        ToastUtil.toastLongMessage("此商品无法加入购物车，可以直接购买");
                        return;
                    }

                    if(goodsBean.getStock() == 0){
                        ToastUtil.toastLongMessage("库存不足");
                        return;
                    }
                    goodsDetailPresenter.addCar(goodsBean.getId());
                }
                break;
            case R.id.tv_buy:
                List<GoodsBean> goodsBeanLists = new ArrayList<>();
                goodsBean.setQuantity(1);
                goodsBean.setGoodsName(goodsBean.getName());
                goodsBean.setGoodsFaceImage(goodsBean.getImgUrl());
                goodsBean.setPrice(goodsBean.getDiscountPrice());
                goodsBean.setGoodsId(goodsBean.getId());
                goodsBean.setChecked(true);
                goodsBeanLists.add(goodsBean);
                MallConfirmOrderActivity.start(mContext, goodsBeanLists,goodsBean.getIsInput());
                break;
            case R.id.iv_share:
                if (null != goodsBean) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, goodsBean.getId(), "3");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(GoodsDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;
            case R.id.iv_collect:
                if (null != goodsBean) {
                    goodsDetailPresenter.setCollect(id, goodsBean.getIscollection());
                }
                break;
            case R.id.tv_ckpj:
                if (null != goodsBean) {
                    GoodsCommentListActivity.start(mContext, goodsBean.getId());
                }
                break;
        }
    }


    @Override
    public void onGetGoodsDetailSuccess(GoodsBean goodsBean) {
        this.goodsBean = goodsBean;
        initGoodsDatail(goodsBean);
    }

    @Override
    public void onGetGoodsCommentListSuccess(List<GoodsCommentBean> goodsCommentBeans, int pages) {

    }

    @Override
    public void onaddCarSuccess() {
        ToastUtil.toastLongMessage("添加购物车成功");
    }

    @Override
    public void onGiveSuccess() {
        goodsDetailPresenter.getGoodsDetail(id);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    private void setHtmlData(String remark) {
        if (TextUtils.isEmpty(remark)) return;
        String htmlData = "" +
                "<html><style>img{width:100%;height:auto}</style><body>" + remark + "</body></html>";
        final WebSettings webSettings = wv_detail_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);
        wv_detail_content.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }

}

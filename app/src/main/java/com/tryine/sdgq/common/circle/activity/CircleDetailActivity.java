package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.just.agentweb.AgentWebView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.CircleCommentAdapter;
import com.tryine.sdgq.common.circle.adapter.CircleCommentChildAdapter;
import com.tryine.sdgq.common.circle.adapter.ShowImageAdapter;
import com.tryine.sdgq.common.circle.bean.CircleCommentBean;
import com.tryine.sdgq.common.circle.bean.CircleDetailBean;
import com.tryine.sdgq.common.circle.presenter.CircleDetailPresenter;
import com.tryine.sdgq.common.circle.view.CircleDetailView;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.FullScreenImg.AssImgPreviewActivity;
import com.tryine.sdgq.view.GradeTextView;
import com.tryine.sdgq.view.dialog.CommentDialog;
import com.tryine.sdgq.view.dialog.DeleteCommentDialog;
import com.tryine.sdgq.view.dialog.GiftDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴友圈详情
 *
 * @author: zhangshuaijun
 * @time: 2021-12-02 10:26
 */
public class CircleDetailActivity extends BaseActivity implements CircleDetailView {
    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_dj)
    GradeTextView tv_dj;
    @BindView(R.id.tv_gz)
    TextView tv_gz;
    @BindView(R.id.iv_vip)
    ImageView iv_vip;

    @BindView(R.id.wv_detail_content)
    AgentWebView wv_detail_content;

    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.rl_play)
    RelativeLayout rl_play;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_commentCount)
    TextView tv_commentCount;
    @BindView(R.id.tv_rewardCount)
    TextView tv_rewardCount;
    @BindView(R.id.tv_collectCount)
    TextView tv_collectCount;
    @BindView(R.id.tv_giveCount)
    TextView tv_giveCount;


    List<CircleCommentBean> commentLists = new ArrayList<>();
    @BindView(R.id.rc_comment)
    public RecyclerView rc_comment;
//    CircleCommentAdapter circleCommentAdapter;

    CommonAdapter circleCommentAdapter;

    @BindView(R.id.rc_data)
    public RecyclerView rv_image;

    CircleDetailPresenter circleDetailPresenter;
    String id;
    int pageNum = 1;

    CircleDetailBean circleDetailBean;

    int replycomment = 0; //0正常评论 1回复评论

    int selectedPosition = 0;


    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, CircleDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        id = getIntent().getStringExtra("id");
        circleDetailPresenter = new CircleDetailPresenter(this);
        circleDetailPresenter.attachView(this);
        circleDetailPresenter.getCircleDetail(id);
        circleDetailPresenter.getCirclecommentlist(id, pageNum);


        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        circleDetailPresenter.getCircleDetail(id);
    }

    protected void initViews() {

        circleCommentAdapter = new CommonAdapter(this, R.layout.item_circle_comment, commentLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                CircleCommentBean data = (CircleCommentBean) o;
                holder.setText(R.id.tv_name, data.getUserName());
                holder.setText(R.id.tv_content, data.getContent());
                holder.setText(R.id.tv_time, data.getCreateTimeStr());
                CircleImageView iv_head = holder.getView(R.id.iv_head);
                GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), iv_head);
                TextView tv_zkpl = holder.getView(R.id.tv_zkpl);
                TextView tv_zkpl1 = holder.getView(R.id.tv_zkpl1);
                tv_zkpl.setVisibility(View.GONE);
                tv_zkpl1.setVisibility(View.GONE);

                if (data.getIsTwoCommon().equals("1") && data.isExpand()) {
                    tv_zkpl.setVisibility(View.VISIBLE);
                    tv_zkpl.setText("展开" + data.getTwoCouont() + "条回复");
                } else {
                    tv_zkpl.setVisibility(View.GONE);
                    if (data.getPageNum() < data.getPages()) {
                        tv_zkpl1.setVisibility(View.VISIBLE);
                    } else {
                        tv_zkpl1.setVisibility(View.GONE);
                    }
                }
                RecyclerView rc_comment = holder.getView(R.id.rc_comment);
                CircleCommentChildAdapter circleCommentAdapter = new CircleCommentChildAdapter(mContext, data.getCommentVoList());
                LinearLayoutManager lin = new LinearLayoutManager(mContext);
                lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
                rc_comment.setLayoutManager(lin);
                rc_comment.setAdapter(circleCommentAdapter);
                rc_comment.setVisibility(View.VISIBLE);
                circleCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        CommentDialog commentDialog = new CommentDialog(mContext, R.style.dialog_center, 1, data.getCommentVoList().get(position).getUserName());
                        commentDialog.show();
                        commentDialog.setmOnTextSendListener(new CommentDialog.OnTextSendListener() {
                            @Override
                            public void onContent(String content) {
                            }

                            @Override
                            public void onReplycomment(String content) {
                                circleDetailPresenter.replycomment(data.getCommentVoList().get(position).getId(), content, "1");
                            }

                            @Override
                            public void dismiss() {
                            }
                        });
                    }
                });

                circleCommentAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                        if (data.getCommentVoList().get(position).getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
                            DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog(mContext);
                            deleteCommentDialog.show();
                            deleteCommentDialog.setConfirmListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    circleDetailPresenter.deletecomment(data.getCommentVoList().get(position).getId());
                                }
                            });
                        }
                        return false;
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (commentLists.get(position).getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
                            DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog(mContext);
                            deleteCommentDialog.show();
                            deleteCommentDialog.setConfirmListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    circleDetailPresenter.deletecomment(commentLists.get(position).getId());

                                }
                            });
                        }

                        return false;
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentDialog commentDialog = new CommentDialog(mContext, R.style.dialog_center, 1, commentLists.get(position).getUserName());
                        commentDialog.show();
                        commentDialog.setmOnTextSendListener(new CommentDialog.OnTextSendListener() {
                            @Override
                            public void onContent(String content) {
                            }

                            @Override
                            public void onReplycomment(String content) {
                                circleDetailPresenter.replycomment(commentLists.get(position).getId(), content, "0");
                            }

                            @Override
                            public void dismiss() {
                            }
                        });
                    }
                });


                iv_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersonalHomePageActivity.start(mContext, commentLists.get(position).getUserId());
                    }
                });

                tv_zkpl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        circleDetailPresenter.getSelecttwocommentlist(commentLists.get(position).getId(), 1, position);
                    }
                });

                tv_zkpl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pageNum = commentLists.get(position).getPageNum() + 1;
                        circleDetailPresenter.getSelecttwocommentlist(commentLists.get(position).getId(), pageNum, position);
                    }
                });

            }
        };

        LinearLayoutManager lin1 = new LinearLayoutManager(this);
        lin1.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_comment.setLayoutManager(lin1);
        rc_comment.setAdapter(circleCommentAdapter);

    }


    protected void initDataViews() {
        if (null != circleDetailBean) {
            setHtmlData(circleDetailBean.getContent());
        }

        GlideEngine.createGlideEngine().loadUserHeadImage(this, circleDetailBean.getAvatar(), (ImageView) iv_head);
        tv_name.setText(circleDetailBean.getUserName());
        if (!TextUtils.isEmpty(circleDetailBean.getLevel())) {
            tv_dj.setData(Integer.parseInt(circleDetailBean.getLevel()));
        } else {
            tv_dj.setData(0);
        }

        if (!circleDetailBean.getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
            if ("0".equals(circleDetailBean.getIsFocus())) {
                tv_gz.setText("+ 关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yy);
            } else {
                tv_gz.setText("已关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
            }
        } else {
            tv_gz.setVisibility(View.GONE);
        }

        iv_vip.setVisibility(circleDetailBean.getIsVip() == 0 ? View.GONE : View.VISIBLE);

        tv_commentCount.setText("共 " + circleDetailBean.getCommentCount() + " 条评论");
        tv_rewardCount.setText(circleDetailBean.getRewardCount());
        tv_collectCount.setText(circleDetailBean.getCollectCount());
        tv_giveCount.setText(circleDetailBean.getGiveCount());
        tv_date.setText("发布时间: " + circleDetailBean.getCreateTime());

        setdrawableTop(tv_collectCount, circleDetailBean.getIsCollect(), R.mipmap.ic_comment_sc, R.mipmap.ic_comment_sc_pre);
        setdrawableTop(tv_giveCount, circleDetailBean.getIsGive(), R.mipmap.ic_comment_dz, R.mipmap.ic_comment_dz_pre);


        if (!TextUtils.isEmpty(circleDetailBean.getContentUrl())) {
            if (circleDetailBean.getContentType().equals("0")) {

                String[] imgStr = circleDetailBean.getContentUrl().split(",");
                List<String> imgList = Arrays.asList(imgStr);
                int spanCount = 3;
                if (imgList.size() >= 3) {
                    rv_image.setVisibility(View.VISIBLE);
                    iv_image.setVisibility(View.GONE);
                    rl_play.setVisibility(View.GONE);
                    spanCount = 3;
                }
                if (imgList.size() == 2) {
                    rv_image.setVisibility(View.VISIBLE);
                    iv_image.setVisibility(View.GONE);
                    rl_play.setVisibility(View.GONE);
                    spanCount = 2;
                } else if (imgList.size() == 1) {
                    rv_image.setVisibility(View.GONE);
                    iv_image.setVisibility(View.VISIBLE);
                    rl_play.setVisibility(View.GONE);

                    Glide.with(mContext).load(circleDetailBean.getContentUrl()).apply(new RequestOptions())
                            .into(iv_image);

//                    GlideEngine.createGlideEngine().loadImage(circleDetailBean.getContentUrl(), iv_image);
                }

                ShowImageAdapter showImageAdapter = new ShowImageAdapter(this, imgList, spanCount, 30, 1);
                rv_image.setLayoutManager(new GridLayoutManager(this, spanCount));
                rv_image.setAdapter(showImageAdapter);
                showImageAdapter.setOnLookListener(new ShowImageAdapter.OnLookListener() {
                    @Override
                    public void look(int index) {
                        Intent intent = new Intent(mContext, AssImgPreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("goodImages", new ArrayList<>(imgList));
                        bundle.putInt("currentIndex", index);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    }
                });

            } else {
                rv_image.setVisibility(View.GONE);
                rl_play.setVisibility(View.VISIBLE);
                GlideEngine.createGlideEngine().loadImage(mContext, circleDetailBean.getCoverUrl(), iv_cover);
            }

        }

    }


    /**
     * 是否 0-否 1-是
     */
    private void setdrawableTop(TextView t, String type, int id, int id1) {
        Drawable drawable;
        if ("0".equals(type)) {
            drawable = getResources().getDrawable(
                    id);
            t.setTextColor(0xffd3d3d3);
        } else {
            drawable = getResources().getDrawable(
                    id1);
            t.setTextColor(0xffA98576);
        }
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        t.setCompoundDrawables(null, drawable, null, null);
    }


    @OnClick({R.id.iv_black, R.id.rl_play, R.id.tv_fy, R.id.tv_giveCount, R.id.tv_collectCount, R.id.iv_image, R.id.iv_head, R.id.tv_gz, R.id.tv_rewardCount, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.rl_play:
                if (null != circleDetailBean) {
                    VideoPlayFullScreenActivity.start(mContext, circleDetailBean.getContentUrl());
                }
                break;
            case R.id.tv_fy:
                CommentDialog commentDialog = new CommentDialog(mContext, R.style.dialog_center, 0, "");
                commentDialog.show();
                commentDialog.setmOnTextSendListener(new CommentDialog.OnTextSendListener() {
                    @Override
                    public void onContent(String content) {
                        circleDetailPresenter.setComment(id, content);
                    }

                    @Override
                    public void onReplycomment(String content) {

                    }

                    @Override
                    public void dismiss() {

                    }
                });
                break;
            case R.id.tv_rewardCount: //礼物
                if (null != circleDetailBean) {
                    GiftDialog giftDialog = new GiftDialog(mContext, circleDetailBean.getId());
                    giftDialog.show();
                }
                break;
            case R.id.tv_collectCount: //收藏
                if (null != circleDetailBean) {
                    circleDetailPresenter.setCollect(id, circleDetailBean.getIsCollect());
                }
                break;
            case R.id.tv_giveCount: //点赞
                if (null != circleDetailBean) {
                    circleDetailPresenter.setGive(id, circleDetailBean.getIsGive());
                }
                break;
            case R.id.iv_image: //查看大图
                if (null != circleDetailBean) {
                    List<String> imgList = new ArrayList<>();
                    imgList.add(circleDetailBean.getContentUrl());
                    Intent intent = new Intent(mContext, AssImgPreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("goodImages", new ArrayList<>(imgList));
                    bundle.putInt("currentIndex", 0);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.iv_head:
                if (null != circleDetailBean) {
                    PersonalHomePageActivity.start(mContext, circleDetailBean.getUserId());
                }
                break;
            case R.id.tv_gz:
                if (null != circleDetailBean) {
                    circleDetailPresenter.setFocus(circleDetailBean.getUserId(), circleDetailBean.getIsFocus());

                }
                break;
            case R.id.iv_share:
                if (null != circleDetailBean) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, circleDetailBean.getId(), "2");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(CircleDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;

        }
    }


    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                circleDetailPresenter.getCirclecommentlist(id, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                circleDetailPresenter.getCirclecommentlist(id, pageNum);
            }
        });
    }

    @Override
    public void onGetCircleDetailSuccess(CircleDetailBean circleDetailBean) {
        this.circleDetailBean = circleDetailBean;
        initDataViews();

    }

    @Override
    public void onGetCircleCommentBeanListSuccess(List<CircleCommentBean> circleCommentBeanLists, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            commentLists.clear();
        }

        commentLists.addAll(circleCommentBeanLists);
        if (commentLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_comment.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_comment.setVisibility(View.VISIBLE);
        }
        circleCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCommentSuccess() {
        circleDetailPresenter.getCircleDetail(id);
        pageNum = 1;
        circleDetailPresenter.getCirclecommentlist(id, pageNum);
    }

    @Override
    public void onHfCommentSuccess() {

    }

    @Override
    public void onGiveSuccess() {
        circleDetailPresenter.getCircleDetail(id);
    }

    @Override
    public void onReplycommentSuccess() {
        circleDetailPresenter.getCircleDetail(id);
        pageNum = 1;
        circleDetailPresenter.getCirclecommentlist(id, pageNum);
    }

    @Override
    public void onGetCircleCommentBeanChildListSuccess(List<CircleCommentBean> circleCommentBeanLists, int pageNum, int pages, int selectedPosition) {
        commentLists.get(selectedPosition).setExpand(false);
        commentLists.get(selectedPosition).setPageNum(pageNum);
        commentLists.get(selectedPosition).setPages(pages);

        if (null == commentLists.get(selectedPosition).getCommentVoList()) {
            commentLists.get(selectedPosition).setCommentVoList(circleCommentBeanLists);
        } else {
            commentLists.get(selectedPosition).getCommentVoList().addAll(circleCommentBeanLists);
        }
        circleCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusSuccess() {
        circleDetailPresenter.getCircleDetail(id);
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

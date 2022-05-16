package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIImageLoader;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mine.activity.TaskHomeActivity;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.mine.wallet.JDRechargeActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 礼物
 */
public class GiftDialog extends Dialog {

    Context mContext;

    TextView tv_jzd;
    TextView tv_yzd;
    RecyclerView rc_data;

    List<GiftBean> giftBeanLists = new ArrayList<>();

    int selectedTabPosition = 0;

    LiveInPresenter liveInPresenter;

    String contentId;


    public GiftDialog(Context context,String contentId) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = context;
        this.contentId = contentId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.dialog_tuigift_panel, null);
        ButterKnife.bind(this, mMenuView);
        setContentView(mMenuView);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        liveInPresenter = new LiveInPresenter(mContext);
        liveInPresenter.attachView(new LiveInView() {
            @Override
            public void onCloseRoomSuccess() {

            }

            @Override
            public void onSendPresentSuccess(TUIGiftModel giftModel) {
                ToastUtil.toastLongMessage("赠送成功");
                liveInPresenter.getUserbean();
            }

            @Override
            public void onGiftBeanListSuccess(List<GiftBean> giftBeanList) {
                giftBeanLists = giftBeanList;
                setData();
            }

            @Override
            public void onGetUserbeanSuccess(int goldenBean, int silverBean) {
                tv_jzd.setText(goldenBean + " SD金豆");
                tv_yzd.setText(silverBean + " SD银豆");
            }

            @Override
            public void onGetliveroomdetailSuccess(String pushAddr) {

            }

            @Override
            public void onGetUsertrtcurlSuccess(String pushUrl, String playUrl) {

            }

            @Override
            public void onGetUserdetailSuccess(UserBean userBean) {

            }

            @Override
            public void onGetliveroomdetailSuccess(String userId, String teacherName, String teacherHead, String mPlayURL ,String s) {

            }

            @Override
            public void onFailed(String message) {
                ToastUtil.toastLongMessage(message);
            }
        });
        liveInPresenter.getiGiftList(0);
        liveInPresenter.getUserbean();

        tv_jzd = findViewById(R.id.tv_jzd);
        tv_yzd = findViewById(R.id.tv_yzd);
        rc_data = findViewById(R.id.rc_data);

        tv_jzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JDRechargeActivity.start(mContext);
            }
        });
        tv_yzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskHomeActivity.start(mContext);
            }
        });
    }

    private  void setData() {

        CommonAdapter goodsAdapter = new CommonAdapter(mContext, R.layout.tuigift_panel_recycle_item, giftBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                GiftBean giftBean = (GiftBean) o;
                GlideEngine.createGlideEngine().loadImage(mContext, giftBean.getGiftImageUrl()
                        , holder.getView(R.id.iv_gift_icon));
                GlideEngine.createGlideEngine().loadImage(mContext, giftBean.getGiftImageUrl()
                        , holder.getView(R.id.iv_gift_icon1));

                holder.setText(R.id.tv_gift_name, giftBean.getTitle());
                if ("0".equals(giftBean.getPrice())) {
                    holder.setText(R.id.tv_price, "免费");
                    holder.setText(R.id.tv_price1, "免费");
                } else {
                    if (giftBean.getSdType().equals("0")) {//类型 0:金豆礼物 1:银豆礼物
                        holder.setText(R.id.tv_price, giftBean.getPrice() + "SD金豆");
                        holder.setText(R.id.tv_price1, giftBean.getPrice() + "SD金豆");
                    } else {
                        holder.setText(R.id.tv_price, giftBean.getPrice() + "SD银豆");
                        holder.setText(R.id.tv_price1, giftBean.getPrice() + "SD银豆");
                    }
                }

                LinearLayout ll_gift_root = holder.getView(R.id.ll_gift_root);
                RelativeLayout ll_gift_root1 = holder.getView(R.id.ll_gift_root1);

                if (selectedTabPosition == position) {
                    ll_gift_root.setVisibility(View.GONE);
                    ll_gift_root1.setVisibility(View.VISIBLE);
                } else {
                    ll_gift_root.setVisibility(View.VISIBLE);
                    ll_gift_root1.setVisibility(View.GONE);
                }

                ll_gift_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedTabPosition = position;
                        notifyDataSetChanged();
                    }
                });

                ll_gift_root1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedTabPosition = position;
                        notifyDataSetChanged();
                    }
                });

                TextView tv_send = holder.getView(R.id.tv_send);
                tv_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        liveInPresenter.sendPresent(giftBeanLists.get(position).getGiftId(), contentId);
                    }
                });


            }
        };
        rc_data.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rc_data.setAdapter(goodsAdapter);
    }


}

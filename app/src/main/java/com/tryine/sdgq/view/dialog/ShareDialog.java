package com.tryine.sdgq.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.presenter.SharePresenter;
import com.tryine.sdgq.common.mine.view.ShareView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 分享弹窗
 *
 * @author：qujingfeng
 * @time：2020.11.10 15:05
 */
public class ShareDialog extends Dialog implements ShareView, View.OnClickListener {
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.ll_jb)
    LinearLayout ll_jb;

    Activity mContext;
    int shareType = Parameter.SHARE_TYPE_NORMAL;
    //分享类型 0:视频 1:琴谱 2:琴友圈 3:商品 4:直播 5:邀请好友 6-投票活动
    String modelType = "10";
    String id = "";
    SharePresenter sharePresenter;

    public ShareDialog(Context context, int type, String id, String modelType) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext =(Activity) context;
        shareType = type;
        this.id = id;
        this.modelType = modelType;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.dialog_share, null);
        ButterKnife.bind(this, mMenuView);
        setContentView(mMenuView);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initWidth();

        if (Parameter.SHARE_TYPE_REPORT == shareType) {
            //举报拉黑
            ll_jb.setVisibility(View.VISIBLE);
        } else {
            //正常分享
            ll_title.setVisibility(View.VISIBLE);
        }
        sharePresenter = new SharePresenter(mContext);
        sharePresenter.attachView(this);

    }

    private void initWidth() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_jb.getLayoutParams();
        lp.width = (dm.widthPixels - (int) (20 * dm.density + 0.5f)) / 4;

        ll_jb.setLayoutParams(lp);
    }

    private OnShareListener onShareListener;

    public void setShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    @Override
    public void onSuccess(int type, ShareInfoBean bean) {
        ((BaseActivity) mContext).progressDialog.dismiss();
        bean.setDataType("1");
        if (null != onShareListener) {
            onShareListener.insure(type, bean);
        }
        dismiss();
    }

    @Override
    public void onFailed(String message) {
        ((BaseActivity) mContext).progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);
    }

    public interface OnShareListener {
        void insure(int shareType, ShareInfoBean bean);
    }


    @OnClick({R.id.tv_cancel, R.id.ll_wx, R.id.ll_pyq, R.id.ll_qq})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.ll_wx:
                getShareInfo(Parameter.SHARE_WX);
                break;
            case R.id.ll_pyq:
                getShareInfo(Parameter.SHARE_PYQ);
                break;
            case R.id.ll_qq:
                getShareInfo(Parameter.SHARE_QQ);
                break;
        }
    }

    private void getShareInfo(int type) {
        if ("-1".equals(modelType)) {
            if (null != onShareListener) {
                onShareListener.insure(type, null);
            }
            dismiss();
        } else {
            ((BaseActivity) mContext).progressDialog.show();
            sharePresenter.getShareInfo(id, modelType, type);
        }

    }
}

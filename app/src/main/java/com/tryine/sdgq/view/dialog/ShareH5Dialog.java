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
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
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
public class ShareH5Dialog extends Dialog implements ShareView, View.OnClickListener {
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.ll_jb)
    LinearLayout ll_jb;

    Activity mContext;
    String url;
    String title;
    String content;

    public ShareH5Dialog(Context context, String url, String title, String content) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = (Activity) context;
        this.url = url;
        this.title = title;
        this.content = content;
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

    private void getShareInfo(int shareType) {
        ShareInfoBean bean = new ShareInfoBean();
        bean.setWebpageUrl(url);
        bean.setTitle(title);
        bean.setContent(content);
        ShareUtils.share(mContext, shareType, bean);
    }
}

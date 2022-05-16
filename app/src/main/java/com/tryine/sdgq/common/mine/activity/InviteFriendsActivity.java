package com.tryine.sdgq.common.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.UMENGShareUtils;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.dialog.ShareDialog;
import com.xuexiang.xqrcode.XQRCode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邀请好友
 *
 * @author: zhangshuaijun
 * @time: 2022-01-14 13:17
 */
public class InviteFriendsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_yqm)
    TextView tv_yqm;
    @BindView(R.id.iv_qrCode)
    ImageView iv_qrCode;

    @BindView(R.id.ll_hb)
    LinearLayout ll_hb;

    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InviteFriendsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitefriends;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("我的邀请码");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, userBean.getAvatar(), iv_head);
        tv_name.setText(userBean.getUserName());
        tv_yqm.setText(userBean.getInviteCode());
        Bitmap bitmap = XQRCode.createQRCodeWithLogo(Constant.shareUrl + "#/regist?inviteCode=" + userBean.getInviteCode(), 150, 150, null);
        iv_qrCode.setImageBitmap(bitmap);

        checkREADExternalStoragePermission(this);

    }

    @OnClick({R.id.iv_black, R.id.ll_bc, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_bc:
                //保存图片
                UMENGShareUtils.saveImageToGallery(this, UMENGShareUtils.createBitmapFromView(ll_hb));
                break;
            case R.id.ll_share:
                ShareDialog shareDialog = new ShareDialog(mContext, 1, "0", "5");
                shareDialog.show();
                shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                    @Override
                    public void insure(int shareType, ShareInfoBean bean) {
                        ShareUtils.share(InviteFriendsActivity.this, shareType, bean);
                    }
                });
                break;
        }
    }

    public static boolean checkREADExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //检查没有打开照相机或文件读写权限就弹框请求打开这三个权限
                activity.requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return false;
            }
            return true;
        }
        return true;
    }
}

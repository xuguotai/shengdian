package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.presenter.PersonalPresenter;
import com.tryine.sdgq.common.mine.view.PersonalView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.PictureTools;
import com.tryine.sdgq.util.ToastUtil;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证
 * @author: zhangshuaijun
 * @time: 2022-04-20 14:30
 */
public class PersonalSmrzActivity extends BaseActivity implements PersonalView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_img)
    TextView tv_img;

    PersonalPresenter personalPresenter;

    String avatarUrl;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PersonalSmrzActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_smrz;
    }

    @Override
    protected void init() {
        tv_title.setText("实名认证");
        setWhiteBar();
        personalPresenter = new PersonalPresenter(this);
        personalPresenter.attachView(this);

        tv_img.setText("上传成功");
    }


    @OnClick({R.id.iv_black, R.id.rl_img,R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.rl_img:
                PictureTools.gallery(this);
                break;
            case R.id.tv_submit:
                send();
                break;

        }
    }

    private void send() {
        if ("".equals(getTextStr(et_name))) {
            ToastUtil.toastLongMessage("请输入您的姓名");
            return;
        }
        if ("".equals(getTextStr(et_code))) {
            ToastUtil.toastLongMessage("请输入身份证号码");
            return;
        }
        if (TextUtils.isEmpty(avatarUrl)) {
            ToastUtil.toastLongMessage("请选择人脸照片");
            return;
        }

        progressDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("idCard", getTextStr(et_code));
            object.put("trueName", getTextStr(et_name));
            object.put("faceUrl", avatarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        personalPresenter.updateName(object);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    // 结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia mLocalMedia = selectList.get(0);
                    String path = "";
                    if (mLocalMedia.isCut()) {
                        path = mLocalMedia.getCutPath();
                    } else {
                        path = mLocalMedia.getRealPath();
                    }
                    personalPresenter.uploadFile(path);
                    break;


            }
        }
    }


    @Override
    public void onUpdateAvatarSuccess(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onCampusNameSuccess(String campusName) {

    }

    @Override
    public void onUpdateSuccess() {
       ToastUtil.toastLongMessage("修改成功");
       finish();
    }

    @Override
    public void onFailed(String message) {
      ToastUtil.toastLongMessage(message);
    }
}

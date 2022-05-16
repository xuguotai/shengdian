package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.presenter.PersonalPresenter;
import com.tryine.sdgq.common.mine.view.PersonalView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.PictureTools;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.dialog.VideoTypeDialog;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 15:41
 */
public class PersonalActivity extends BaseActivity implements PersonalView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_name1)
    TextView tv_name1;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_campusName)
    TextView tv_campusName;

    PersonalPresenter personalPresenter;

    UserBean userBean;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PersonalActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("个人信息");
        personalPresenter = new PersonalPresenter(this);
        personalPresenter.attachView(this);
        personalPresenter.userdetail();
        personalPresenter.usercampusid();
        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        personalPresenter.userdetail();
    }

    private void initViews() {
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, userBean.getAvatar(), iv_head);
        tv_name.setText(userBean.getUserName());
        tv_phone.setText(userBean.getMobile());

        if(!TextUtils.isEmpty(userBean.getTrueStatus()) ){
            if(!userBean.getTrueStatus().equals("1")){
                PersonalSmrzActivity.start(mContext);
            }else{
                tv_name1.setText("去实名认证");
            }
        }else{
            tv_name1.setText("去实名认证");
        }

        tv_sex.setText(userBean.getSex().equals("0") ? "男" : "女");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal;
    }


    @OnClick({R.id.iv_black, R.id.rl_head, R.id.rl_nikename, R.id.rl_sex,R.id.rl_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.rl_head:
                //修改头像
                PictureTools.gallery(this);
                break;
            case R.id.rl_sex:
                startActivityForResult(new Intent(this, UpdateSexActivity.class), 2);
                break;
            case R.id.rl_nikename:
                startActivityForResult(new Intent(this, UpdateUserNameActivity.class), 1);
                break;
            case R.id.rl_name:
                if(!TextUtils.isEmpty(userBean.getTrueStatus()) ){
                    if(!userBean.getTrueStatus().equals("1")){
                        PersonalSmrzActivity.start(mContext);
                    }
                }else{
                    PersonalSmrzActivity.start(mContext);
                }
                break;
        }
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
                case 1://修改昵称
                    String userName = data.getStringExtra("nickName");
                    tv_name.setText(userName);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("userName", userName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    personalPresenter.updateName(object);
                    break;
                case 2://修改昵称
                    String sex = data.getStringExtra("sex");
                    tv_sex.setText(sex.equals("0") ? "男" : "女");
                    JSONObject object1 = new JSONObject();
                    try {
                        object1.put("sex", sex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    personalPresenter.updateName(object1);
                    break;
            }
        }
    }


    @Override
    public void onUpdateAvatarSuccess(String avatarUrl) {
        personalPresenter.updateAvatar(avatarUrl);
    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(userBean));
        initViews();
    }

    @Override
    public void onCampusNameSuccess(String campusName) {
        tv_campusName.setText(campusName);
    }

    @Override
    public void onUpdateSuccess() {
        personalPresenter.userdetail();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

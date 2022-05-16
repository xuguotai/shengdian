package com.tryine.sdgq.common.circle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.PictureTools;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.ugcupload.TXUGCPublish;
import com.tryine.sdgq.util.ugcupload.TXUGCPublishTypeDef;
import com.tryine.sdgq.view.TasksCompletedView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-04-12 09:41
 */
public class AddHdActivity extends BaseActivity implements TXUGCPublishTypeDef.ITXVideoPublishListener, CircleView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_title)
    EditText et_title;

    @BindView(R.id.ll_upload)
    LinearLayout ll_upload;
    @BindView(R.id.progress)
    TasksCompletedView progress;
    @BindView(R.id.ll_cover)
    LinearLayout ll_cover;
    @BindView(R.id.iv_coverUrl)
    ImageView iv_coverUrl;

    @BindView(R.id.rc_data)
    RecyclerView rv_image;
    List<ImageUploadBean> imageList = new ArrayList<>();
    private CommonAdapter uploadAdapter;

    public static final int REQUESTCODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
    private String mSignature;

    private TXUGCPublish mVideoPublish = null;

    int resourceType = 1;//0首次全部可以选 1图片 2视频

    private int index = 10;//默认九张图片
    CirclePresenter circlePresenter;
    String mVideoPath;
    String coverUrl; //视频封面

    String activityId;

    TXUGCPublishTypeDef.TXPublishResult result;

    public static void start(Activity activity,String activityId) {
        Intent intent = new Intent();
        intent.putExtra("activityId",activityId);
        intent.setClass(activity, AddHdActivity.class);
        activity.startActivityForResult(intent,2);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_hd_add;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("发布");
        verifyStoragePermissions();
        activityId = getIntent().getStringExtra("activityId");

        circlePresenter = new CirclePresenter(this);
        circlePresenter.attachView(this);
        circlePresenter.getSignature();

        initViews();
    }


    private void initViews() {
        ImageUploadBean uploadBean = new ImageUploadBean();
        uploadBean.setResourceId(R.mipmap.ic_add_img);
        imageList.add(uploadBean);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = (dm.widthPixels - (int) (20 * dm.density + 0.5f)) / 3;

        uploadAdapter = new CommonAdapter(this, R.layout.item_image_upload, imageList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                ImageUploadBean item = (ImageUploadBean) o;
                RelativeLayout ll_item = holder.getView(R.id.ll_item);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_item.getLayoutParams();
                lp.width = width;
                lp.height = width;
                ll_item.setLayoutParams(lp);

                if (0 != item.getResourceId()) {
                    holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                    holder.getView(R.id.image).setBackgroundResource(item.getResourceId());
                } else {
                    if (!"".equals(item.getLocalUrl())) {
                        holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                        GlideEngine.createGlideEngine().loadImage(item.getLocalUrl(), (ImageView) holder.getView(R.id.image));
                    }
                }

                holder.setOnClickListener(R.id.image, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageList.get(position).getResourceId() == R.mipmap.ic_add_img) {
                            if (imageList.size() == 1) {
                                resourceType = 0;
                            }

                            PictureSelector.create(AddHdActivity.this)
                                    .openGallery(resourceType)//PictureMimeType.ofAll()
                                    .isWeChatStyle(true)// 是否开启微信图片选择风格
                                    .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                    .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                                    .isCamera(true)
                                    .maxVideoSelectNum(1)
                                    .isPreviewImage(true)// 是否可预览图片
                                    .isPreviewVideo(true)// 是否可预览视频
                                    .maxSelectNum(index - imageList.size())// 最大图片选择数量
                                    .forResult(PictureConfig.REQUEST_CAMERA);
                        }
                    }
                });

                holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageList.remove(position);
                        rv_image.removeAllViews();
                        notifyDataSetChanged();
                    }
                });

                if (resourceType == 1) {
                    if (holder.getAdapterPosition() > 8) {
                        ll_item.setVisibility(View.GONE);
                    } else {
                        ll_item.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (holder.getAdapterPosition() > 0) {
                        ll_item.setVisibility(View.GONE);
                    }
                }

            }
        };
        rv_image.setLayoutManager(new GridLayoutManager(this, 3));
        rv_image.setAdapter(uploadAdapter);

    }




    @OnClick({R.id.iv_black, R.id.ll_submit, R.id.tv_cancel, R.id.iv_coverUrl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                setResult(RESULT_OK, new Intent().putExtra("isAdd", "0"));
                finish();
                break;
            case R.id.tv_cancel: //取消
                this.result = null;
                pauseUpload();
                break;
            case R.id.iv_coverUrl: //封面
                PictureTools.gallery(this, 910);
                break;
            case R.id.ll_submit:
                submit();
                break;
        }
    }

    int needNum = 0;

    private void submit() {
        needNum = 1;
        if ("".equals(getTextStr(et_title))) {
            ToastUtil.toastLongMessage("请输入标题");
            return;
        }

        if (null != imageList && imageList.size() == 1 && null == result) {
            ToastUtil.toastLongMessage("请添加图片或者视频");
            return;
        }

        if (resourceType == 2 && TextUtils.isEmpty(coverUrl)) {
            ToastUtil.toastLongMessage("请添加视频封面");
            return;
        }

        progressDialog.show();
        if (resourceType == 1) {
            try {
                //批量上传图片
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (int i = 0; i < imageList.size(); i++) {
                            ImageUploadBean imageBean = imageList.get(i);
                            if ("".equals(replaceNull(imageBean.getUrl())) &&
                                    imageBean.getResourceId() != R.mipmap.ic_add_img) {
                                needNum++;

                                int position = i;

                                if (resourceType == 1) {

                                    try {
                                        //上传图片
                                        circlePresenter.uploadFile(imageBean.getLocalUrl(), position);
                                        Thread.sleep(2000);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }

                                }
                            }
                        }

                    }
                }.start();
                if (needNum == 0) {
                    addFoot();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            addFoot();
        }

    }


    /**
     * 发布
     */
    private void addFoot() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", et_title.getText().toString());
            obj.put("activityId", activityId);
            obj.put("latLong", SPUtils.getConfigString(Parameter.LOCATION, ""));
            String addrImg = "";
            if (resourceType == 1) { //图片
                for (ImageUploadBean imageBean : imageList) {
                    if (imageBean.getResourceId() != R.mipmap.ic_add_img) {
                        //去掉默认上传图片
                        addrImg += imageBean.getUrl() + ",";
                    }
                }
                if (!"".equals(addrImg)) {
                    addrImg = addrImg.substring(0, addrImg.length() - 1);
                }
                obj.put("contentType", "0");//0-图片 1-视频
                obj.put("contentUrl", addrImg);
            } else {  //视频
                obj.put("contentType", "1");
                obj.put("fileId", result.videoId);//视频文件id
                obj.put("contentUrl", result.videoURL);
                obj.put("coverUrl", coverUrl);
            }
            circlePresenter.partakeactivity(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始上传视频
     */
    private void beginUpload() {
        if (TextUtils.isEmpty(mSignature)) {
            ToastUtil.toastLongMessage("上传视频签名失效");
            return;
        }
        progress.setProgress(0);
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(this.getApplicationContext(), "independence_android");
            mVideoPublish.setListener(this);
        }

        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        param.signature = mSignature;
        param.videoPath = mVideoPath;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
        }

    }


    /**
     * 暂停上传
     */
    private void pauseUpload() {
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
            rv_image.setVisibility(View.VISIBLE);
            ll_upload.setVisibility(View.GONE);
            ll_cover.setVisibility(View.GONE);
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
                    for (LocalMedia mLocalMedia : selectList) {
                        if (imageList.size() > 10) {
                            return;
                        }
                        String path = "";
                        if (mLocalMedia.isCut()) {
                            path = mLocalMedia.getCutPath();
                        } else {
                            path = mLocalMedia.getRealPath();
                        }
                        if (mLocalMedia.getMimeType().contains("video")) {
                            resourceType = 2;
                            mVideoPath = path;
                            beginUpload();
                            rv_image.setVisibility(View.GONE);
                            ll_upload.setVisibility(View.VISIBLE);
                            ll_cover.setVisibility(View.VISIBLE);
                        } else {
                            resourceType = 1;
                            coverUrl = "";
                            //封装数据
                            ImageUploadBean bean = new ImageUploadBean();
                            bean.setLocalUrl(path);
                            imageList.add(0, bean);
                            rv_image.removeAllViews();
                            uploadAdapter.notifyDataSetChanged();
                        }

                    }
                    break;
                case 910:
                    // 结果回调
                    List<LocalMedia> selectList1 = PictureSelector.obtainMultipleResult(data);
                    LocalMedia mLocalMedia = selectList1.get(0);
                    String path = "";
                    if (mLocalMedia.isCut()) {
                        path = mLocalMedia.getCutPath();
                    } else {
                        path = mLocalMedia.getRealPath();
                    }
                    circlePresenter.uploadFile(path, 100);
                    GlideEngine.createGlideEngine().loadImage(mContext, path, iv_coverUrl);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onAddCircleSuccess() {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage("发布成功");
        setResult(RESULT_OK, new Intent().putExtra("isAdd", "1"));
        finish();
    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {
        if (type == 100) {
            this.coverUrl = fileUrl;
            ToastUtil.toastLongMessage("视频封面上传成功");
            return;
        }
        //更新数据
        try {
            if (!TextUtils.isEmpty(fileUrl)) {
                imageList.get(type).setUrl(fileUrl);
            }
            boolean isUpload = true;
            for (int i = 0; i < imageList.size(); i++) {
                ImageUploadBean imageBean = imageList.get(i);
                if (!"".equals(replaceNull(imageBean.getLocalUrl())) &&
                        "".equals(replaceNull(imageBean.getUrl()))) {
                    isUpload = false;
                }
            }
            if (isUpload) {
                //图片上传完成
                addFoot();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onGetTopicListSuccess(List<HomeMenuBean> homeMenuBeanList) {
    }

    @Override
    public void onGetHdListSuccess(List<HomeMenuBean> homeMenuBeanList) {
    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {
        this.mSignature = signature;

    }

    @Override
    public void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages) {

    }

    @Override
    public void onFocusSuccess(String isFocus) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {

    }


    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);

    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        progress.setProgress((int) (100 * uploadBytes / totalBytes));
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        if (result.retCode == 0) {
            this.result = result;
        }
    }


    public boolean verifyStoragePermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

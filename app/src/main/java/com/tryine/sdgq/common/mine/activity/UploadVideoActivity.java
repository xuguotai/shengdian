package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mine.adapter.UploadVideoSheetMusicAdapter;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.common.mine.presenter.UploadVideoVideoPresenter;
import com.tryine.sdgq.common.mine.view.UploadVideoView;
import com.tryine.sdgq.util.FileUtils;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.PictureTools;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.ugcupload.TXUGCPublish;
import com.tryine.sdgq.util.ugcupload.TXUGCPublishTypeDef;
import com.tryine.sdgq.view.TasksCompletedView;
import com.tryine.sdgq.view.dialog.GlSheetMusicDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.VideoTypeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上传视频
 *
 * @author: zhangshuaijun
 * @time: 2021-12-02 09:26
 */
public class UploadVideoActivity extends BaseActivity implements TXUGCPublishTypeDef.ITXVideoPublishListener, UploadVideoView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.radioGroupId)
    RadioGroup radioGroupId;
    @BindView(R.id.radioGroupIdJd)
    RadioGroup radioGroupIdJd;

    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.ll_glTitle)
    LinearLayout ll_glTitle;
    @BindView(R.id.ll_goldenBean)
    LinearLayout ll_goldenBean;


    @BindView(R.id.tv_videoTitle)
    TextView tv_videoTitle;
    @BindView(R.id.tv_videotype)
    TextView tv_videotype;
    @BindView(R.id.tv_videoHj)
    TextView tv_videoHj;
    @BindView(R.id.et_goldenBean)
    EditText et_goldenBean;
    @BindView(R.id.iv_coverUrl)
    ImageView iv_coverUrl;


    @BindView(R.id.iv_addVideo)
    ImageView iv_addVideo;
    @BindView(R.id.ll_upload)
    LinearLayout ll_upload;
    @BindView(R.id.progress)
    TasksCompletedView progress;

    public static final int REQUESTCODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
    private String mSignature;

    private TXUGCPublish mVideoPublish = null;

    String mVideoPath;

    int videoType = 0;//视频种类 0-单个 1-合集 2-关联合集
    int beanType = 0;//解锁方式 0金豆 1银豆
    String coverUrl; //封面地址
    int videoTime;//视频时长
    String videoWidth;//视频宽度
    String videoHeight;//视频高度

    TXUGCPublishTypeDef.TXPublishResult result;

    /**
     * 视频类型
     */
    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    UploadVideoVideoPresenter uploadVideoVideoPresenter;
    HomeMenuBean selectHomeMenuBean;
    /**
     * 视频合集
     */
    List<HomeMenuBean> videoHjList = new ArrayList<>();
    HomeMenuBean videoHjBean;
    /**
     * 关联琴谱
     */
    List<SheetMusicBean> sheetMusicBeanList = new ArrayList<>();
    List<SheetMusicBean> selectedSheetMusicBeanList = new ArrayList<>(); //选中的琴谱
    @BindView(R.id.rc_sheetMusicData)
    RecyclerView rc_sheetMusicData;
    UploadVideoSheetMusicAdapter uploadVideoSheetMusicAdapter;
    int selectedSelectPosition = 0;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UploadVideoActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_uploadvideo;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("上传视频");
        initViews();
        verifyStoragePermissions();

        uploadVideoVideoPresenter = new UploadVideoVideoPresenter(this);
        uploadVideoVideoPresenter.attachView(this);
        uploadVideoVideoPresenter.getSignature();
        uploadVideoVideoPresenter.getVideoTypeList();
        uploadVideoVideoPresenter.getSelectpiaonlist();
        uploadVideoVideoPresenter.getVideoHjList();

    }


    private void initViews() {

        radioGroupId.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.buttonId:
                        videoType = 0;
                        ll_title.setVisibility(View.VISIBLE);
                        ll_goldenBean.setVisibility(View.VISIBLE);
                        ll_glTitle.setVisibility(View.GONE);
                        break;
                    case R.id.buttonId1:
                        videoType = 1;
                        ll_title.setVisibility(View.VISIBLE);
                        ll_goldenBean.setVisibility(View.VISIBLE);
                        ll_glTitle.setVisibility(View.GONE);
                        break;
                    case R.id.buttonId2:
                        videoType = 2;
                        ll_title.setVisibility(View.GONE);
                        ll_goldenBean.setVisibility(View.GONE);
                        ll_glTitle.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupIdJd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.buttonId3:
                        beanType = 0;
                        et_goldenBean.setHint("请输入金豆数量(数量设置0表示免费)");
                        break;
                    case R.id.buttonId4:
                        beanType = 1;
                        et_goldenBean.setHint("请输入银豆数量(数量设置0表示免费)");
                        break;

                }
            }
        });


        uploadVideoSheetMusicAdapter = new UploadVideoSheetMusicAdapter(this, selectedSheetMusicBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_sheetMusicData.setLayoutManager(lin);
        rc_sheetMusicData.setAdapter(uploadVideoSheetMusicAdapter);
        uploadVideoSheetMusicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_close) {
                    selectedSheetMusicBeanList.remove(position);
                    uploadVideoSheetMusicAdapter.notifyDataSetChanged();
                } else if (view.getId() == R.id.iv_sheetmusic) {
                    selectedSelectPosition = position;
                    GlSheetMusicDialog glSheetMusicDialog = new GlSheetMusicDialog(UploadVideoActivity.this, sheetMusicBeanList);
                    glSheetMusicDialog.show();
                    glSheetMusicDialog.setOnItemClickListener(new GlSheetMusicDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(SheetMusicBean sheetMusicBean) {
                            selectedSheetMusicBeanList.get(selectedSelectPosition).setName(sheetMusicBean.getName());
                            selectedSheetMusicBeanList.get(selectedSelectPosition).setId(sheetMusicBean.getId());
                            uploadVideoSheetMusicAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }


    @OnClick({R.id.iv_black, R.id.iv_addVideo, R.id.tv_cancel, R.id.iv_videoType, R.id.tv_addgl, R.id.ll_btn_upload,
            R.id.iv_coverUrl, R.id.iv_videohj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                showPromptDialog();
                break;
            case R.id.iv_addVideo://选择视频
                if (verifyStoragePermissions()) ;
                chooseFile();
                break;
            case R.id.tv_cancel: //取消
                this.result = null;
                pauseUpload();
                break;
            case R.id.iv_videoType:
                if (null != homeMenuBeanList) {
                    VideoTypeDialog videoTypeDialog = new VideoTypeDialog(this, homeMenuBeanList);
                    videoTypeDialog.show();
                    videoTypeDialog.setOnItemClickListener(new VideoTypeDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(HomeMenuBean homeMenuBean) {
                            selectHomeMenuBean = homeMenuBean;
                            tv_videotype.setText(homeMenuBean.getName());
                        }
                    });
                }
                break;
            case R.id.tv_addgl://新增关联
                SheetMusicBean sheetMusicBean = new SheetMusicBean();
                selectedSheetMusicBeanList.add(sheetMusicBean);
                uploadVideoSheetMusicAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_btn_upload: //发布视频
                upload();
                break;
            case R.id.iv_coverUrl: //封面
                PictureTools.gallery(this);
                break;
            case R.id.iv_videohj: //关联合集
                if (null != videoHjList) {
                    VideoTypeDialog videoTypeDialog = new VideoTypeDialog(this, videoHjList);
                    videoTypeDialog.show();
                    videoTypeDialog.setOnItemClickListener(new VideoTypeDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(HomeMenuBean homeMenuBean) {
                            videoHjBean = homeMenuBean;
                            tv_videoHj.setText(homeMenuBean.getTitle());
                        }
                    });
                }
                break;

        }
    }


    private void upload() {

        if (null == selectHomeMenuBean) {
            ToastUtil.toastLongMessage("请选择视频类型");
            return;
        }
        if ("".equals(getSheetMusicIds())) {
            ToastUtil.toastLongMessage("请至少关联一个琴谱");
            return;
        }
        if ("".equals(coverUrl)) {
            ToastUtil.toastLongMessage("请输入选择视频封面");
            return;
        }
        if ("".equals(coverUrl)) {
            ToastUtil.toastLongMessage("请上传视频封面");
            return;
        }
        if (null == result) {
            ToastUtil.toastLongMessage("请上传视频");
            return;
        }
        if (videoType == 0 || videoType == 1) {
            if ("".equals(getTextStr(tv_videoTitle))) {
                ToastUtil.toastLongMessage("请输入视频标题");
                return;
            }
            if ("".equals(getTextStr(et_goldenBean))) {
                if (beanType == 0) {
                    ToastUtil.toastLongMessage("请输入金豆数量(数量设置0表示免费)");
                } else {
                    ToastUtil.toastLongMessage("请输入银豆数量(数量设置0表示免费)");
                }
                return;
            }
        } else if (videoType == 2) {
            if (null == videoHjBean) {
                ToastUtil.toastLongMessage("请关联合集");
                return;
            }
        }


        uploadVideoVideoPresenter.uploadVideo(videoType
                , getTextStr(tv_videoTitle)
                , selectHomeMenuBean.getId()
                , getSheetMusicIds()
                , et_goldenBean.getText().toString()
                , null != videoHjBean ? videoHjBean.getId() : ""
                , result.videoId, result.videoURL, coverUrl, videoTime, videoWidth, videoHeight
                , beanType);

    }


    private String getSheetMusicIds() {
        String ids = "";
        for (SheetMusicBean sheetMusicBean : selectedSheetMusicBeanList) {
            if (null != sheetMusicBean.getId() && !"".equals(sheetMusicBean.getId())) {
                ids = ids + sheetMusicBean.getId() + ",";
            }
        }
        return ids;
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


    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent wrapIntent = Intent.createChooser(intent, null);
        startActivityForResult(wrapIntent, REQUESTCODE);
    }

    /**
     * 开始上传视频
     */
    private void beginUpload() {
        if (TextUtils.isEmpty(mSignature)) {
            uploadVideoVideoPresenter.getSignature();
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
            iv_addVideo.setVisibility(View.VISIBLE);
            ll_upload.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                // Get the path
                String path = null;
                path = FileUtils.getRealPathFromUri(this, uri);
                mVideoPath = path;
                getLocalVideoDuration(mVideoPath);
                if (videoTime > 60) {
                    ToastUtil.toastLongMessage("视频长度不能超过1分钟");
                    return;
                }
                beginUpload();
                iv_addVideo.setVisibility(View.GONE);
                ll_upload.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_OK) {
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
                    uploadVideoVideoPresenter.uploadFile(path);
                    GlideEngine.createGlideEngine().loadImage(mContext, path, iv_coverUrl);
                    break;

            }

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

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {
        this.homeMenuBeanList = homeMenuBeanList;
    }

    @Override
    public void onGetVideoHjSuccess(List<HomeMenuBean> homeMenuBeanList) {
        this.videoHjList = homeMenuBeanList;
    }

    @Override
    public void onGetSheetMusicBeanSuccess(List<SheetMusicBean> sheetMusicBeanList) {
        this.sheetMusicBeanList = sheetMusicBeanList;
    }

    @Override
    public void onUploadVideoSuccess() {
        ToastUtil.toastLongMessage("视频发布成功，请等待审核");
        finish();
    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {
        this.coverUrl = fileUrl;
        ToastUtil.toastLongMessage("视频封面上传成功");
    }

    @Override
    public void onGetsignatureSuccess(String signature) {
        this.mSignature = signature;

    }

    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);
    }


    /**
     * get Local video duration
     *
     * @return
     */
    public void getLocalVideoDuration(String videoPath) {
        //除以 1000 返回是秒
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            videoTime = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
            //宽
            videoWidth = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            //高
            videoHeight = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showPromptDialog();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showPromptDialog() {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                "是否退出当前页面", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                finish();
            }

            @Override
            public void cancel() {

            }
        });
    }
}

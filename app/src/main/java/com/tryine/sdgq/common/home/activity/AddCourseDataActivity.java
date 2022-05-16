package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.FileUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.ugcupload.TXUGCPublish;
import com.tryine.sdgq.util.ugcupload.TXUGCPublishTypeDef;
import com.tryine.sdgq.view.TasksCompletedView;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课堂资料
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class AddCourseDataActivity extends BaseActivity implements TXUGCPublishTypeDef.ITXVideoPublishListener, CourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_classContent)
    EditText et_classContent;
    @BindView(R.id.et_problemContent)
    EditText et_problemContent;
    @BindView(R.id.et_homeworkContent)
    EditText et_homeworkContent;
    @BindView(R.id.et_nextContent)
    EditText et_nextContent;

    @BindView(R.id.tv_fileName)
    TextView tv_fileName;
    @BindView(R.id.iv_add_pptx)
    ImageView iv_add_pptx;

    @BindView(R.id.iv_addVideo)
    ImageView iv_addVideo;
    @BindView(R.id.ll_upload)
    LinearLayout ll_upload;
    @BindView(R.id.progress)
    TasksCompletedView progress;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    String filePath; //本地地址
    String fileUrl; //外部地址
    String mVideoPath;
    private TXUGCPublish mVideoPublish = null;

    CoursePresenter coursePresenter;

    // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
    private String mSignature;

    TXUGCPublishTypeDef.TXPublishResult result;

    String id;

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, AddCourseDataActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_data;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("课堂资料");
        id = getIntent().getStringExtra("id");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getSignature();

    }

    @OnClick({R.id.iv_black, R.id.iv_add_pptx, R.id.tv_fileName, R.id.iv_addVideo, R.id.ll_btn_upload, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_add_pptx:
            case R.id.tv_fileName:
                String[] mimeTypes = {MimeType.DOC, MimeType.DOCX, MimeType.PDF, MimeType.PPT, MimeType.PPTX, MimeType.XLS, MimeType.XLSX};
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                Intent wrapIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapIntent, 1);
                break;
            case R.id.iv_addVideo://选择视频
                if (verifyStoragePermissions()) ;
                chooseFile();
                break;
            case R.id.ll_btn_upload:
                upload();
                break;
            case R.id.tv_cancel: //取消
                this.result = null;
                pauseUpload();
                break;
        }
    }

    int needNum = 0;

    private void upload() {

        if ("".equals(getTextStr(et_classContent))) {
            ToastUtil.toastLongMessage("请输入课堂内容");
            return;
        }
        if ("".equals(getTextStr(et_problemContent))) {
            ToastUtil.toastLongMessage("请输入课程问题");
            return;
        }
        if ("".equals(getTextStr(et_homeworkContent))) {
            ToastUtil.toastLongMessage("请输入作业内容");
            return;
        }
        if ("".equals(getTextStr(et_nextContent))) {
            ToastUtil.toastLongMessage("请输入下节课内容");
            return;
        }
        progressDialog.show();
        try {
            //批量上传图片
            if (!TextUtils.isEmpty(filePath)) {
                coursePresenter.uploadFile(filePath);
                needNum = 1;
            }
            if (needNum == 0) {
                addFoot();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    /**
     * 发布
     */
    private void addFoot() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("couresId", id);//id
            obj.put("classContent", getTextStr(et_classContent));//课程内容
            obj.put("problemContent", getTextStr(et_problemContent));//问题
            obj.put("homeworkContent", getTextStr(et_homeworkContent));//作业
            obj.put("nextContent", getTextStr(et_nextContent));//下节课
            obj.put("attachmentUrl", fileUrl + "");//附件
            if (null != result) {
                obj.put("videoUrl", result.videoURL);//视频
            }

            coursePresenter.addCourseData(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }

    @Override
    public void onGetcancelledSuccess(int count, int positions) {

    }

    @Override
    public void onAddCampusSuccess() {

    }

    @Override
    public void onAddCourseDataSuccess() {
        ToastUtil.toastLongMessage("添加成功");
        finish();
    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {
        this.mSignature = signature;
    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {
        this.fileUrl = fileUrl;
        if (!TextUtils.isEmpty(fileUrl)) {
            //图片上传完成
            addFoot();
        }

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onselectsuspendedSuccess(int selectsuspended, int positions) {

    }

    @Override
    public void onSuspendedSuccess() {

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

    //存放需要过滤的类型的类
    public class MimeType {
        public static final String DOC = "application/msword";
        public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        public static final String XLS = "application/vnd.ms-excel";
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        public static final String PPT = "application/vnd.ms-powerpoint";
        public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        public static final String PDF = "application/pdf";
    }

    //接收返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                // Get the path
                String path = null;
                path = FileUtils.getRealPath(this, uri);
                filePath = path;
                if (!TextUtils.isEmpty(filePath)) {
                    tv_fileName.setText(filePath);
                    tv_fileName.setVisibility(View.VISIBLE);
                    iv_add_pptx.setVisibility(View.GONE);
                }

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                // Get the path
                String path = null;
                path = FileUtils.getRealPathFromUri(this, uri);
                mVideoPath = path;
                beginUpload();
                iv_addVideo.setVisibility(View.GONE);
                ll_upload.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 开始上传视频
     */
    private void beginUpload() {
        if (TextUtils.isEmpty(mSignature)) {
//            uploadVideoVideoPresenter.getSignature();
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

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent wrapIntent = Intent.createChooser(intent, null);
        startActivityForResult(wrapIntent, 2);
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

}

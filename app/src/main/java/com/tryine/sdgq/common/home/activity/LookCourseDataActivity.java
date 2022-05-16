package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerVideoId;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.VideoPlayFullScreenActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.FileUtils;
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
 * 查看课堂资料
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class LookCourseDataActivity extends BaseActivity implements CourseView {
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
    @BindView(R.id.tv_videoTitle)
    TextView tv_videoTitle;
    @BindView(R.id.ll_file)
    LinearLayout ll_file;
    @BindView(R.id.ll_video)
    LinearLayout ll_video;


    CoursePresenter coursePresenter;
    String courseId;

    String videoUrl;


    public static void start(Context context, String courseId) {
        Intent intent = new Intent();
        intent.setClass(context, LookCourseDataActivity.class);
        intent.putExtra("courseId", courseId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lookcourse_data;
    }

    @Override
    protected void init() {
        setWhiteBar();
        initSuperVodGlobalSetting();
        tv_title.setText("课堂资料");
        courseId = getIntent().getStringExtra("courseId");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.detailinfo(courseId);

        tv_videoTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_videoTitle.getPaint().setAntiAlias(true);

    }

    @OnClick({R.id.iv_black, R.id.tv_fileName, R.id.ll_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_fileName:
                break;
            case R.id.ll_video:
                VideoPlayFullScreenActivity.start(mContext, videoUrl);
                break;

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
    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {
    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {
        et_classContent.setText(classContent);
        et_problemContent.setText(problemContent);
        et_homeworkContent.setText(homeworkContent);
        et_nextContent.setText(nextContent);

        if (!TextUtils.isEmpty(attachmentUrl) && !attachmentUrl.equals("null")) {
            ll_file.setVisibility(View.VISIBLE);
            tv_fileName.setText(attachmentUrl);
        } else {
            ll_file.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(videoUrl)) {
            this.videoUrl = videoUrl;
            ll_video.setVisibility(View.VISIBLE);
        } else {
            ll_video.setVisibility(View.GONE);
        }


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
        ToastUtil.toastLongMessage(message);
    }


//    public boolean verifyStoragePermissions() {
//        try {
//            //检测是否有写的权限
//            int permission = ActivityCompat.checkSelfPermission(this,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // 没有写的权限，去申请写的权限，会弹出对话框
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

    /**
     * 初始化超级播放器全局配置
     */
    private void initSuperVodGlobalSetting() {
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 0;
        rect.height = 0;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
    }


}

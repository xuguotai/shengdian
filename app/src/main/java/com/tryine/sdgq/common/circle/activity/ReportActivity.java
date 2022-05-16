package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.ReportTabAdapter;
import com.tryine.sdgq.common.circle.bean.ReportTypeBean;
import com.tryine.sdgq.common.circle.presenter.ReportPresenter;
import com.tryine.sdgq.common.circle.view.ReportView;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 举报
 */
public class ReportActivity extends BaseActivity implements ReportView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.rv_image)
    RecyclerView rv_image;
    @BindView(R.id.rv_tabbtn)
    RecyclerView rv_tabbtn;

    private int index = 4;//3张图片

    List<ReportTypeBean> reportTypeBeans = new ArrayList<>();
    List<ImageUploadBean> imageList = new ArrayList<>(); //当前选择的所有图片
    private CommonAdapter uploadAdapter;
    private ReportTabAdapter reportTabAdapter;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    ReportPresenter reportPresenter;

    int selectedTabPosition = 0;

    String resourcesId;


    public static void start(Context context, String resourcesId) {
        Intent intent = new Intent();
        intent.setClass(context, ReportActivity.class);
        intent.putExtra("resourcesId", resourcesId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("举报");
        resourcesId = getIntent().getStringExtra("resourcesId");
        verifyStoragePermissions();

        reportPresenter = new ReportPresenter(this);
        reportPresenter.attachView(this);
        reportPresenter.getReportType();

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_size.setText(s.length() + "/300");
            }
        });

        initViews();
    }

    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void initViews() {
        reportTabAdapter = new ReportTabAdapter(this, reportTypeBeans);
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager1.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        rv_tabbtn.setLayoutManager(flexboxLayoutManager1);
        rv_tabbtn.setAdapter(reportTabAdapter);
        reportTabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                selectedTabPosition = position;
                reportTabAdapter.setSelectedTabPosition(position);
            }
        });


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
                            PictureSelector.create(ReportActivity.this)
                                    .openGallery(PictureMimeType.ofImage())//PictureMimeType.ofAll()
                                    .isWeChatStyle(true)// 是否开启微信图片选择风格
                                    .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                    .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                                    .isCamera(false)
                                    .maxVideoSelectNum(1)
                                    .isPreviewImage(false)// 是否可预览图片
                                    .isPreviewVideo(false)// 是否可预览视频
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

                if (holder.getAdapterPosition() > 2) {
                    ll_item.setVisibility(View.GONE);
                } else {
                    ll_item.setVisibility(View.VISIBLE);
                }


            }
        };
        rv_image.setLayoutManager(new GridLayoutManager(this, 3));
        rv_image.setAdapter(uploadAdapter);

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
                        //封装数据
                        ImageUploadBean bean = new ImageUploadBean();
                        bean.setLocalUrl(path);
                        imageList.add(0, bean);
                        rv_image.removeAllViews();
                        uploadAdapter.notifyDataSetChanged();

                    }
                    break;

            }
        }
    }

    int needNum = 0;

    private void submit() {
        if ("".equals(getTextStr(et_content))) {
            ToastUtil.toastLongMessage("请输入内容");
            return;
        }

        if ("".equals(getTextStr(et_phone))) {
            ToastUtil.toastLongMessage("请输入联系方式");
            return;
        }

        progressDialog.show();
        try {

            for (int i = 0; i < imageList.size(); i++) {
                ImageUploadBean imageBean = imageList.get(i);
                if ("".equals(replaceNull(imageBean.getUrl())) &&
                        imageBean.getResourceId() != R.mipmap.ic_add_img) {
                    needNum++;
                    int position = i;
                    try {
                        //上传图片
                        reportPresenter.uploadFile(imageBean.getLocalUrl(), position);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
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
            obj.put("typeId", reportTypeBeans.get(selectedTabPosition).getId());// 举报类型id
            obj.put("content", et_content.getText().toString());// 举报内容
            obj.put("resourcesId", resourcesId);// 举报资源id
            obj.put("resourcesType", 0);//    举报类型(0:用户 2:视频 3:琴友圈 4:直播)
            obj.put("phone", et_phone.getText().toString());//    联系电话
            String addrImg = "";
            for (ImageUploadBean imageBean : imageList) {
                if (imageBean.getResourceId() != R.mipmap.ic_add_img) {
                    //去掉默认上传图片
                    addrImg += imageBean.getUrl() + ",";
                }
            }
            if (!"".equals(addrImg)) {
                addrImg = addrImg.substring(0, addrImg.length() - 1);
            }
            obj.put("imgUrl", addrImg);// 举报图片
            reportPresenter.report(obj);

        } catch (Exception e) {
            e.printStackTrace();
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
    public void onGetReportTypeListSuccess(List<ReportTypeBean> reportTypeBeans) {
        this.reportTypeBeans.clear();
        this.reportTypeBeans.addAll(reportTypeBeans);
        reportTabAdapter.notifyDataSetChanged();

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {
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
    public void onReportSuccess() {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage("提交成功");
        finish();
    }

    @Override
    public void onUserComplainSuccess() {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

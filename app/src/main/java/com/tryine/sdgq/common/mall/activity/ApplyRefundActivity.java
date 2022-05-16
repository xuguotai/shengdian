package com.tryine.sdgq.common.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.AddCircleActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.common.mall.presenter.OrderPresenter;
import com.tryine.sdgq.common.mall.view.OrderView;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.ReasonDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请退款
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 14:31
 */
public class ApplyRefundActivity extends BaseActivity implements OrderView {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.tv_reason)
    TextView tv_reason;
    @BindView(R.id.tv_totalPayPrice)
    TextView tv_totalPayPrice;
    @BindView(R.id.et_returnReason)
    EditText et_returnReason;
    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.rc_data)
    RecyclerView rv_image;

    private int index = 4;//默认3张图片

    OrderPresenter orderPresenter;

    List<ImageUploadBean> imageList = new ArrayList<>();
    private CommonAdapter uploadAdapter;

    OrderListBean orderListBean;
    String tkPrice;

    public static void start(Activity context, OrderListBean orderListBean, String tkPrice) {
        Intent intent = new Intent();
        intent.setClass(context, ApplyRefundActivity.class);
        intent.putExtra("orderListBean", orderListBean);
        intent.putExtra("tkPrice", tkPrice);
        context.startActivityForResult(intent, 2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_applyrefund;
    }

    @Override
    protected void init() {
        setWhiteBar();
        orderListBean = (OrderListBean) getIntent().getSerializableExtra("orderListBean");
        tkPrice = getIntent().getStringExtra("tkPrice");
        tv_title.setText("申请退款");
        orderPresenter = new OrderPresenter(mContext);
        orderPresenter.attachView(this);

        initViews();
    }


    private void initViews() {
        UserBean userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        tv_totalPayPrice.setText(tkPrice);
        et_mobile.setText(userBean.getMobile());

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
                            PictureSelector.create(ApplyRefundActivity.this)
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

    @OnClick({R.id.iv_black, R.id.ll_reason, R.id.ll_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_reason:
                ReasonDialog reasonDialog = new ReasonDialog(this);
                reasonDialog.show();
                reasonDialog.setOnItemClickListener(new ReasonDialog.OnItemClickListener() {
                    @Override
                    public void resultReason(String reason) {
                        tv_reason.setText(reason);
                    }
                });
                break;
            case R.id.ll_submit:
                submit();
                break;
        }
    }

    int needNum = 0;

    private void submit() {
        if ("".equals(getTextStr(tv_reason))) {
            ToastUtil.toastLongMessage("请选择退款原因");
            return;
        }

        if ("".equals(getTextStr(et_returnReason))) {
            ToastUtil.toastLongMessage("请输入退款说明");
            return;
        }


        progressDialog.show();
        try {
            //批量上传图片
            for (int i = 0; i < imageList.size(); i++) {
                ImageUploadBean imageBean = imageList.get(i);
                if ("".equals(replaceNull(imageBean.getUrl())) &&
                        imageBean.getResourceId() != R.mipmap.ic_add_img) {
                    needNum++;

                    int position = i;

                    try {
                        //上传图片
                        orderPresenter.uploadFile(imageBean.getLocalUrl(), position);
                        Thread.sleep(1500);
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
                        ImageUploadBean bean = new ImageUploadBean();
                        bean.setLocalUrl(path);
                        imageList.add(0, bean);
                        rv_image.removeAllViews();
                        uploadAdapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * 提交
     */
    private void addFoot() {
        for (int i = 0; i < orderListBean.getPiratesOrderDetailListVoList().size(); i++) {
            OrderGoodsBean orderGoodsBean = orderListBean.getPiratesOrderDetailListVoList().get(i);
            if(!orderGoodsBean.getStatus().equals("7")){
                JSONObject obj = new JSONObject();
                try {
                    obj.put("orderNo", orderGoodsBean.getDetailOrderNo());
                    obj.put("mobile", et_mobile.getText().toString());
                    obj.put("returnRemark", et_returnReason.getText().toString());
                    obj.put("returnReason", tv_reason.getText().toString());
                    obj.put("applyRefundPrice", orderGoodsBean.getUnitPrice());
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
                    obj.put("picList", addrImg);
                    orderPresenter.refund(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    @Override
    public void onGetOrderBeanListSuccess(List<OrderListBean> orderListBeanList, int pages) {

    }

    @Override
    public void onGetOrderBeanSuccess(OrderListBean orderListBean) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onRefundSuccess() {
        ToastUtil.toastLongMessage("申请成功，请等待审核!");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCommentSuccess(int i) {

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
    public void onCancelorderSuccess() {
    }

    @Override
    public void onDetermineSuccess() {
    }

    @Override
    public void onPayOrderSuccess() {
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}

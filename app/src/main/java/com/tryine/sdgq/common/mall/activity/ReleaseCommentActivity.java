package com.tryine.sdgq.common.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.adapter.GoodsImageUploadAdapter;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.common.mall.presenter.OrderPresenter;
import com.tryine.sdgq.common.mall.view.OrderView;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.PictureTools;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.StarBarView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-19 14:38
 */
public class ReleaseCommentActivity extends BaseActivity implements OrderView {

    @BindView(R.id.ll_content)
    LinearLayout ll_content;

    private int index = 0;//点击的index
    Map<Integer, List<ImageUploadBean>> imageMap = new HashMap<>();
    Map<Integer, GoodsImageUploadAdapter> adapterMap = new HashMap<>();


    int uploadNum = 0;//已上传数
    int needNum = 0;//已上传数量


    OrderPresenter orderPresenter;

    OrderListBean orderListBean;

    public static void start(Activity context, OrderListBean orderListBean) {
        Intent intent = new Intent();
        intent.setClass(context, ReleaseCommentActivity.class);
        intent.putExtra("orderListBean", orderListBean);
        context.startActivityForResult(intent, 1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_release_comment;
    }

    @Override
    protected void init() {
        setWhiteBar();
        orderListBean = (OrderListBean) getIntent().getSerializableExtra("orderListBean");
        orderPresenter = new OrderPresenter(mContext);
        orderPresenter.attachView(this);
        initViews();
    }

    private void initViews() {
        for (int i = 0; i < orderListBean.getPiratesOrderDetailListVoList().size(); i++) {
            OrderGoodsBean bean = orderListBean.getPiratesOrderDetailListVoList().get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.item_release_comment, null);
            view.setTag(i);
            ImageView iv_image = view.findViewById(R.id.iv_good_image);
            if (!TextUtils.isEmpty(bean.getGoodsImgUrl())) {
                GlideEngine.createGlideEngine().loadImage(this, bean.getGoodsImgUrl().split(",")[0], iv_image);
            } else {
                GlideEngine.createGlideEngine().loadImage(this, bean.getGoodsImgUrl(), iv_image);
            }
            TextView tv_name = view.findViewById(R.id.tv_goodName);
            tv_name.setText(bean.getGoodsName());
            StarBarView starBar = view.findViewById(R.id.starBar);
            RecyclerView rv_image = view.findViewById(R.id.rv_image);
            List<ImageUploadBean> imageList = new ArrayList<>();
            ImageUploadBean uploadBean = new ImageUploadBean();
            uploadBean.setResourceId(R.mipmap.ic_add_img);
            imageList.add(uploadBean);
            //评分点击事件
            starBar.setMarkType(1);
            starBar.setOnStarChangeListener(new StarBarView.OnStarChangeListener() {
                @Override
                public void onStarChange(float mark) {
                    if (mark == 0) {
                        starBar.setStarMark(1);
                    }

                }
            });

            GoodsImageUploadAdapter uploadAdapter = new GoodsImageUploadAdapter(this, imageList, 3, 10);
            rv_image.setAdapter(uploadAdapter);
            rv_image.setLayoutManager(new GridLayoutManager(this, 3));
            uploadAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View holder, int posiont) {
                    index = Integer.parseInt(view.getTag().toString());
                    switch (holder.getId()) {
                        case R.id.image:
                            if (imageList.get(posiont).getResourceId() == R.mipmap.ic_add_img) {
                                PictureTools.galleryNum(ReleaseCommentActivity.this, 7 - imageList.size());
                            }
                            break;
                        case R.id.iv_delete:
                            imageList.remove(posiont);
                            rv_image.removeAllViews();
                            uploadAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            });
            imageMap.put(i, imageList);
            adapterMap.put(i, uploadAdapter);

            ll_content.addView(view);
        }

    }

    @OnClick({R.id.iv_close, R.id.tv_release})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_release:
                for (int i = 0; i < ll_content.getChildCount(); i++) {
                    EditText et_content = ll_content.getChildAt(i).findViewById(R.id.et_content);
                    if ("".equals(et_content.getText().toString())) {
                        ToastUtil.toastShortMessage("请输入评价内容");
                        return;
                    }
                }
                try {
                    //批量上传图片
                    needNum = 1;
                    uploadNum = 0;
                    progressDialog.show();
                    //批量上传图片
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            for (int i = 0; i < orderListBean.getPiratesOrderDetailListVoList().size(); i++) {
                                for (int j = 0; j < imageMap.get(i).size(); j++) {
                                    ImageUploadBean imageBean = imageMap.get(i).get(j);
                                    if ("".equals(replaceNull(imageBean.getUrl())) &&
                                            imageBean.getResourceId() != R.mipmap.ic_add_img) {
                                        needNum++;
                                        try {
                                            //上传图片
                                            uploadImage(imageBean.getLocalUrl(), i, j);
                                            Thread.sleep(1500);
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                    }
                                }
                            }
                        }
                    }.start();
                    if (0 == needNum) {
                        progressDialog.dismiss();
                        releaseComment();
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
                break;

        }
    }


    private void releaseComment() {
        progressDialog.dismiss();
        for (int i = 0; i < ll_content.getChildCount(); i++) {
            EditText et_content = ll_content.getChildAt(i).findViewById(R.id.et_content);
            if ("".equals(et_content.getText().toString())) {
                ToastUtil.toastLongMessage("请输入评价内容");
                return;
            }
        }
        progressDialog.show();
        try {
            JSONObject sendObj = new JSONObject();
            for (int i = 0; i < orderListBean.getPiratesOrderDetailListVoList().size(); i++) {
                String addrImg = "";
                for (ImageUploadBean imageBean : imageMap.get(i)) {
                    if (imageBean.getResourceId() != R.mipmap.ic_add_img) {
                        //去掉默认上传图片
                        addrImg += imageBean.getUrl() + ",";
                    }
                }
                if (!"".equals(addrImg)) {
                    addrImg = addrImg.substring(0, addrImg.length() - 1);
                }
                OrderGoodsBean bean = orderListBean.getPiratesOrderDetailListVoList().get(i);
                EditText et_content = ll_content.getChildAt(i).findViewById(R.id.et_content);
                StarBarView starBar = ll_content.getChildAt(i).findViewById(R.id.starBar);
                sendObj.put("commentLevel", starBar.getStarMark());//  评论星级(1-5级)
                sendObj.put("goodsId", bean.getGoodsId());//商品ID
                sendObj.put("content", et_content.getText().toString());
                sendObj.put("contentImgUrl", bean.getGoodsImgUrl());
                sendObj.put("orderNo", bean.getDetailOrderNo());
                sendObj.put("imgUrl", addrImg);
                sendObj.put("commentLevel", starBar.getStarMark());

                orderPresenter.comment(sendObj, i);
            }

        } catch (Exception e) {
            ToastUtil.toastShortMessage("请求参数异常");
            progressDialog.dismiss();
            e.printStackTrace();
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
                        if (imageMap.get(index).size() > 6) {
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
                        imageMap.get(index).add(0, bean);
                        adapterMap.get(index).notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public void uploadImage(String file, int mapIndex, int listIndex) {
        String suffix = file.substring(file.lastIndexOf(".") + 1);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), new File(file));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "temp." + suffix, fileBody)
                .addFormDataPart("type", "1")
                .build();
        Request request = new Request.Builder()
                .url(Constant.uploadFile)
                .addHeader("Authorization", SPUtils.getToken())
                .addHeader("Terminaltype", "Android")
                .addHeader("platform", "app")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60000, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    JSONObject json = new JSONObject(str);
                    JSONObject result = new JSONObject(json.optString("result"));
                    //{"code":"200","message":"操作成功!","result":{"url":"https://obs.chalykj.com/appImage/20210827/07b9787ba60e6b58d94a0a5bfba874ad.jpg?x-image-process=image/quality,q_80"}}
                    String coverUrl = result.optString("url");
                    onUploadSuccess(coverUrl, mapIndex, listIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void onUploadSuccess(String url, int mapIndex, int listIndex) {
        //更新数据
        try {
            imageMap.get(mapIndex).get(listIndex).setUrl(url);
            boolean isUpload = true;
            for (int i = 0; i < orderListBean.getPiratesOrderDetailListVoList().size(); i++) {
                for (int j = 0; j < imageMap.get(i).size(); j++) {
                    ImageUploadBean imageBean = imageMap.get(i).get(j);
                    if (!"".equals(replaceNull(imageBean.getLocalUrl())) &&
                            "".equals(replaceNull(imageBean.getUrl()))) {
                        isUpload = false;
                    }
                }
            }
            if (isUpload) {
                //图片上传完成
                releaseComment();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onCancelorderSuccess() {

    }

    @Override
    public void onDetermineSuccess() {

    }

    @Override
    public void onPayOrderSuccess() {

    }

    @Override
    public void onRefundSuccess() {

    }

    @Override
    public void onCommentSuccess(int i) {
        if (i == orderListBean.getPiratesOrderDetailListVoList().size() - 1) {
            ToastUtil.toastLongMessage("评价成功");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

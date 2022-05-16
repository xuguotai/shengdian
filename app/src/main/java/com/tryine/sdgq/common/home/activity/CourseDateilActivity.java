package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.GlideEngine;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买课程详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class CourseDateilActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_layer)
    TextView tv_layer;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_remake)
    TextView tv_remake;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    CourseBean courseBean;


    public static void start(Context context, CourseBean courseBean) {
        Intent intent = new Intent();
        intent.setClass(context, CourseDateilActivity.class);
        intent.putExtra("courseBean", courseBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_coursedateil;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("课程详情");

        courseBean = (CourseBean) getIntent().getSerializableExtra("courseBean");

        GlideEngine.createGlideEngine().loadImage(mContext, courseBean.getImgUrl()
                , iv_cover);
        tv_name.setText(courseBean.getName());
        tv_layer.setText("课程期限：" + courseBean.getLayer()+"天");
        tv_price.setText(courseBean.getPrice());
        tv_remake.setText(Html.fromHtml(courseBean.getDetail()));

    }

    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                if (null != courseBean) {
                    SignUpActivity.start(mContext, courseBean);
                }

                break;
        }
    }


}

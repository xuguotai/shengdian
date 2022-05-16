package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.AnnouncementBean;
import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.util.GlideEngine;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-21 13:42
 */
public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_date)
    TextView tv_date;

    MessageBean messageBean;

    public static void start(Context context, MessageBean messageBean) {
        Intent intent = new Intent();
        intent.setClass(context, MessageDetailActivity.class);
        intent.putExtra("messageBean", messageBean);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_msg;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("消息详情");
        messageBean = (MessageBean) getIntent().getSerializableExtra("messageBean");
        tv_content.setText(messageBean.getContent());
        tv_date.setText(messageBean.getCreateDate());


    }


    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }


}

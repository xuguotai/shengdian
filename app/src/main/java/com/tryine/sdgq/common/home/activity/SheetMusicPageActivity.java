package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.SheetMusicPageAdapter;
import com.tryine.sdgq.common.home.adapter.SheetMusicVideoAdapter;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.FullScreenImg.AssImgPreviewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴谱全屏
 *
 * @author: zhangshuaijun
 * @time: 2021-12-09 15:33
 */
public class SheetMusicPageActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    SheetMusicPageAdapter sheetMusicPageAdapter;


    public static void start(Context context, String imgUrl) {
        Intent intent = new Intent();
        intent.setClass(context, SheetMusicPageActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sheetmusic_page;
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }


    @Override
    protected void init() {
        setWhiteBar();
        String[] imgUrl = getIntent().getStringExtra("imgUrl").split(",");
        List resultList = Arrays.asList(imgUrl);

        tv_title.setText("第1页/共" + resultList.size() + "页");

        sheetMusicPageAdapter = new SheetMusicPageAdapter(this, resultList);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_data.setLayoutManager(lin);
        rv_data.setAdapter(sheetMusicPageAdapter);
        sheetMusicPageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, AssImgPreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("goodImages", new ArrayList<>(resultList));
                bundle.putInt("currentIndex", position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


        rv_data.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (recyclerView != null && recyclerView.getChildCount() > 0) {
                    try {
                        int currentPosition = ((RecyclerView.LayoutParams) recyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                        tv_title.setText("第" + (currentPosition + 1) + "页/共" + resultList.size() + "页");
                    } catch (Exception e) {
                    }
                } if (isSlideToBottom(recyclerView)) {
                    tv_title.setText("第" + (resultList.size()) + "页/共" + resultList.size() + "页");
                }

            }
        });



    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

}

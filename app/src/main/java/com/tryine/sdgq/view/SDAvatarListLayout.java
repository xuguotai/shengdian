package com.tryine.sdgq.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;


import com.blankj.utilcode.util.SizeUtils;
import com.tryine.sdgq.R;
import com.tryine.sdgq.util.GlideEngine;

import java.util.ArrayList;
import java.util.List;

public class SDAvatarListLayout extends HorizontalScrollView {
    private Context context;
    /**
     * 存放创建的最大的ImageView集合
     */
    private List<CircleImageView> imageViewList;
    /**
     * 默认图片大小
     */
    private int imageSize = Math.round(SizeUtils.dp2px(50));
    /**
     * 默认图片数量
     */
    private int imageMaxCount = 6;
    /**
     * 默认图片偏移百分比 0～1
     */
    private float imageOffset = 0.3f;

    public SDAvatarListLayout(Context context) {
        this(context, null);
    }

    public SDAvatarListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SDAvatarListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.SDAvatarListLayout);
        imageMaxCount = ta.getInt(R.styleable.SDAvatarListLayout_image_max_count, imageMaxCount);
        imageSize = (int) ta.getDimension(R.styleable.SDAvatarListLayout_image_size, imageSize);
        imageOffset = ta.getFloat(R.styleable.SDAvatarListLayout_image_offset, imageOffset);
        imageOffset = imageOffset > 1 ? 1 : imageOffset;
        init();
        ta.recycle();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        int offset = imageSize - (int) (imageSize * imageOffset);
        imageViewList = new ArrayList<>(imageMaxCount);
        for (int i = 0; i < imageMaxCount; i++) {
            CircleImageView imageView = new CircleImageView(context);
            imageView.setId(imageView.hashCode() + i);
            imageView.setBorderColor(Color.WHITE);
            imageView.setBorderWidth(Math.round(SizeUtils.dp2px(1)));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageSize, imageSize);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins((imageMaxCount - i - 1) * offset, 0, 0, 0);
            relativeLayout.addView(imageView, params);
            imageViewList.add(imageView);
        }
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.addView(relativeLayout);
    }

    public void setAvatarListListener(ShowAvatarListener listener) {
        hideAllImageView();
        listener.showImageView(imageViewList);
    }


    public void setAvatarListListener(List<String> drawableList) {
        if (drawableList == null) {
            return;
        }
        hideAllImageView();
        int i = imageMaxCount - 1;
        for (String url : drawableList) {
            GlideEngine.createGlideEngine().loadImage(getContext(), url, imageViewList.get(i));
            imageViewList.get(i).setVisibility(VISIBLE);
            if (i == 0) {
                break;
            }
            --i;
        }
    }

    private void hideAllImageView() {
        for (CircleImageView head : imageViewList) {
            head.setVisibility(View.GONE);
        }
    }

    public interface ShowAvatarListener {
        void showImageView(List<CircleImageView> imageViewList);
    }
}

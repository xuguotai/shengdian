package com.tryine.sdgq.view.banner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

public class BannerVideoPagerAdapter extends PagerAdapter {
    private List<VideoModel> mList;
    private Activity mContext;
    private int defaultImg = R.mipmap.icon_indicator;//默认图片
    private int mRoundCorners = -1;
    RequestOptions options = new RequestOptions()
            .error(null)    //加载错误之后的错误图
            .fitCenter()
            .skipMemoryCache(true)  //跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);  //只缓存最终的图片

    /**
     * 默认
     *
     * @param defaultImg
     */
    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }


    /**
     * 设置圆角
     *
     * @param mRoundCorners
     */
    public void setmRoundCorners(int mRoundCorners) {
        this.mRoundCorners = mRoundCorners;
    }

    /**
     * 点击回调
     */
    public static interface OnClickImagesListener {
        void onImagesClick(int position);
    }

    private OnClickImagesListener mImagesListener;

    public void setOnClickImagesListener(OnClickImagesListener listener) {
        mImagesListener = listener;

    }

    public BannerVideoPagerAdapter(List<VideoModel> list, Activity context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 500000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int index = position % mList.size();
        View view = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.banner_video_img_layout, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        LoadImage(mList.get(index).getCoverUrl(), imageView);
        tv_time.setText(mList.get(index).getVideoTimeStr());
        //OnClick
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesListener.onImagesClick(index);
            }
        });

        container.addView(view);
        return view;
    }

    /**
     * 加载图片
     */
    public void LoadImage(String url, ImageView imageview) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .skipMemoryCache(true)  //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);  //只缓存最终的图片
        if (mRoundCorners == -1) {
            GlideEngine.createGlideEngine().loadImage(mContext, url, imageview);
        } else {
            GlideEngine.createGlideEngine().loadRoundImage(mContext, url, mRoundCorners, imageview);
        }
    }

    /**
     * dp转px
     * 16dp - 48px
     * 17dp - 51px
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    public void setBar(int color) {
        ImmersionBar.with(mContext)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(color)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarColor(R.color.white)
                .init();
    }

}
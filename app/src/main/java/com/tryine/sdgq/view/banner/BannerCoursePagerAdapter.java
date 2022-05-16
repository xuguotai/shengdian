package com.tryine.sdgq.view.banner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

public class BannerCoursePagerAdapter extends PagerAdapter {
    private List<CourseBean> mList;
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

    public BannerCoursePagerAdapter(List<CourseBean> list, Activity context) {
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
        view = LayoutInflater.from(mContext).inflate(R.layout.banner_course_layout, container, false);
        LinearLayout ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        LoadImage(mList.get(index).getImgUrl(), imageView);
        // 0->暂停使用 1->未激活 2、使用中 3已失效
        if(mList.get(index).getStatus().equals("0")){
            tv_name.setText(mList.get(index).getCouresName()+"(暂停使用)");
        }else if(mList.get(index).getStatus().equals("1")){
            tv_name.setText(mList.get(index).getCouresName()+"(未激活)");
        }else if(mList.get(index).getStatus().equals("2")){
            tv_name.setText(mList.get(index).getCouresName()+"(使用中)");
        }else if(mList.get(index).getStatus().equals("3")){
            tv_name.setText(mList.get(index).getCouresName()+"(已失效)");
        }

        tv_time.setText("课程期限：" + mList.get(index).getEndTime()+"到期");
        tv_price.setText(mList.get(index).getPaidPrice() + "");
        //OnClick
        ll_root.setOnClickListener(new View.OnClickListener() {
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
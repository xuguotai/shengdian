package com.tryine.sdgq.view.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;

import java.util.List;


public class BannerVideoViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private View mLayout;//布局
    private Activity mContext;//上下文
    private ViewPager mViewPager;//viewpager
    private BannerVideoPagerAdapter mPagerAdapter;//adapter
    private LinearLayout mLineIndicator;//指示器集合容器
    private ImageView[] mImageView;//小圆点imageview对象
    private List<VideoModel> mList;//url数组
    private int currentIndex = 0;//当前实际page
    private int startCurrentIndex = 50;//当前page
    private long secondTime = 0, firstTime = 0;

    //private Timer mTimer=null;//定时器
    //private MyTimerTask mTimerTask=null;

    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;
    private int mRollTime = 5000;

    private int resId_piont_press = R.mipmap.icon_indicator_press;
    private int resId_piont = R.mipmap.icon_indicator;
    private boolean isPoint = false;//开启指示器

    public static interface OnClickBannerListener {
        void onBannerClick(int position);
    }

    private BannerVideoViewPager.OnClickBannerListener mBannerListener;

    public BannerVideoViewPager addBannerListener(BannerVideoViewPager.OnClickBannerListener listener) {
        mBannerListener = listener;
        return this;
    }

    //轮播切换事件
    public static interface OnPageSelectedListener {
        void onPageSelected(int position);
    }

    private BannerVideoViewPager.OnPageSelectedListener mBannerPageSelectedListener;

    public BannerVideoViewPager addOnPageSelected(BannerVideoViewPager.OnPageSelectedListener listener) {
        mBannerPageSelectedListener = listener;
        return this;
    }

    //ui更新
//    Handler mHandler=mHandler= new Handler(){
//        public void handleMessage(Message msg) {
//            int index =  mViewPager.getCurrentItem()+1;//下一个页
//            mViewPager.setCurrentItem(index);//设置此次要显示的pager
//            currentIndex=index%mList.size();
//            setImageBackground(currentIndex);
//        }
//    };
    public BannerVideoViewPager(Context context) {
        super(context);
    }

    public BannerVideoViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BannerVideoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;

    }

    /**
     * 初始化viewpager
     *
     * @param list      url集合
     * @param isGallery 是否使用3D画廊效果
     */
    public BannerVideoViewPager initBanner(List<VideoModel> list, boolean isGallery) {
        mList = list;
        //引入布局
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.banner_view_layout, null);
        mViewPager = (ViewPager) mLayout.findViewById(R.id.viewPager);//关闭
        mLineIndicator = (LinearLayout) mLayout.findViewById(R.id.lineIndicator);
        //初始化位置
        //currentIndex=startCurrentIndex%mList.size();

        mPagerAdapter = new BannerVideoPagerAdapter(mList, mContext);
        mPagerAdapter.setOnClickImagesListener(new BannerVideoPagerAdapter.OnClickImagesListener() {
            @Override
            public void onImagesClick(int position) {
                if (mBannerListener != null) {
                    mBannerListener.onBannerClick(position);
                }
            }
        });

        mViewPager.setAdapter(mPagerAdapter);
        if (isGallery) {
            mViewPager.setPageTransformer(true, new ZoomPageTransformer());
        }

        mViewPager.setCurrentItem(currentIndex);
        mViewPager.setOffscreenPageLimit(2);//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
        mViewPager.addOnPageChangeListener(this);
        return this;
    }


    /**
     * 初始化viewpager
     *
     * @param list      url集合
     * @param isGallery 是否使用3D画廊效果
     * @param alpha     滑动透明度变化
     */
    public BannerVideoViewPager initBanner(List<VideoModel> list, boolean isGallery, float alpha) {
        mList = list;

        //引入布局
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.banner_view_layout, null);
        mViewPager = (ViewPager) mLayout.findViewById(R.id.viewPager);//关闭
        mLineIndicator = (LinearLayout) mLayout.findViewById(R.id.lineIndicator);
        //初始化位置
        //currentIndex=startCurrentIndex%mList.size();

        mPagerAdapter = new BannerVideoPagerAdapter(mList, mContext);
        mPagerAdapter.setOnClickImagesListener(new BannerVideoPagerAdapter.OnClickImagesListener() {
            @Override
            public void onImagesClick(int position) {
                if (mBannerListener != null) {
                    mBannerListener.onBannerClick(position);
                }
            }
        });

        mViewPager.setAdapter(mPagerAdapter);
        if (isGallery) {
            mViewPager.setPageTransformer(true, new ZoomPageTransformer(alpha));
        }

        mViewPager.setCurrentItem(startCurrentIndex);
        mViewPager.setOffscreenPageLimit(2);//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
        mViewPager.addOnPageChangeListener(this);
        return this;
    }

    /**
     * 添加默认图片,当加载失败后显示
     *
     * @param resId_img
     * @return
     */
    public BannerVideoViewPager addDefaultImg(int resId_img) {
        mPagerAdapter.setDefaultImg(resId_img);
        return this;
    }

    /**
     * 添加圆角
     *
     * @param corners
     * @return
     */
    public BannerVideoViewPager addRoundCorners(int corners) {
        mPagerAdapter.setmRoundCorners(corners);
        return this;
    }

    /**
     * @param columnMargin 两个Page之间的距离
     * @param rowMargin    page的外边距
     *                     注意当添加了3D画廊效果时,columnMargin尽量设小。应该本是已经进行了x、y的缩放
     */
    public BannerVideoViewPager addPageMargin(int columnMargin, int rowMargin) {

        mViewPager.setPageMargin(dip2px(columnMargin));
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layout.setMargins(dip2px(rowMargin), 0, dip2px(rowMargin), 0);
        mViewPager.setLayoutParams(layout);
        return this;
    }

    /**
     * 添加小圆点
     *
     * @param distance 间距
     */
    public BannerVideoViewPager addPoint(int distance) {
        isPoint = true;
        mImageView = new ImageView[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            ImageView imageView = new ImageView(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dip2px(distance) / 2, 0, dip2px(distance) / 2, 0);
            imageView.setLayoutParams(params);
            if (i == currentIndex) {
                imageView.setImageResource(resId_piont_press);
            } else {
                imageView.setImageResource(resId_piont);
            }
            mImageView[i] = imageView;
            mLineIndicator.addView(imageView);
        }

        return this;
    }

    /**
     * 添加小圆点
     *
     * @param distance    间距
     * @param piont_press 替换选中图标
     * @param piont       替换未选中图片
     */
    public BannerVideoViewPager addPoint(int distance, int piont_press, int piont) {
        isPoint = true;
        resId_piont_press = piont_press;
        resId_piont = piont;
        mImageView = new ImageView[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            ImageView imageView = new ImageView(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dip2px(distance) / 2, 0, dip2px(distance) / 2, 0);
            imageView.setLayoutParams(params);
            if (i == currentIndex) {
                imageView.setImageResource(resId_piont_press);
            } else {
                imageView.setImageResource(resId_piont);
            }
            mImageView[i] = imageView;
            mLineIndicator.addView(imageView);
        }

        return this;
    }

    /**
     * 添加小圆点底部间距
     *
     * @param paddBottom
     */
    public BannerVideoViewPager addPointBottom(int paddBottom) {
        mLineIndicator.setPadding(0, 0, 0, dip2px(paddBottom));
        return this;
    }

    /**
     * 配置完成,将布局添加到父容器
     */
    public BannerVideoViewPager finishConfig() {
        this.addView(mLayout);
        return this;
    }

    //开始轮播
    public BannerVideoViewPager addStartTimer(int time) {
        mRollTime = time;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mAutoRollRunnable == null) {
            mAutoRollRunnable = new AutoRollRunnable();
        }

        mAutoRollRunnable.start();
        return this;
    }

    // 停止轮播
    public void stopTimer() {
        if (mAutoRollRunnable != null) {
            mAutoRollRunnable.stop();
        }
    }

    private class AutoRollRunnable implements Runnable {
        //是否在轮播的标志
        boolean isRunning = false;

        @Override
        public void run() {
            if (isRunning) {
                int index = mViewPager.getCurrentItem() + 1;//下一个页
                mViewPager.setCurrentItem(index);//设置此次要显示的pager
                currentIndex = index % mList.size();
                setImageBackground(currentIndex);
                mHandler.postDelayed(this, 1000 * mRollTime);
            }
        }

        public void start() {
            if (!isRunning) {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 1000 * mRollTime);
            }
        }

        public void stop() {
            if (isRunning) {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }
    }
//    /**
//     * 开启定时器
//     * @param time
//     */
//    public BannerViewPager addStartTimer(int time) {
//        if (mTimer == null) {
//            mTimer = new Timer();
//        }
//        if(mTimerTask==null){
//            mTimerTask=new MyTimerTask();
//        }
//        mTimer.schedule(mTimerTask, 3000, 1000*time);
//        return this;
//    }
//    /**
//     * 停止定时器
//     */
//    public void stopTimer(){
//        if(mTimer!=null){
//            mTimer.cancel();
//            mTimer = null;
//        }
//        if(mTimerTask!=null){
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
//    }
//    class MyTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            mHandler.sendEmptyMessage(1001);//在此线程中，不能操作ui主线程
//        }
//    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //---------------viewpager滑动事件-----------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 滑动时同步改变底部小圆点
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        currentIndex = position % mList.size();
        setImageBackground(currentIndex);
        if (mBannerPageSelectedListener != null) {
            mBannerPageSelectedListener.onPageSelected(currentIndex);
        }
    }

    /**
     * 改变指示器
     *
     * @param selectItemsIndex
     */
    private void setImageBackground(int selectItemsIndex) {
        if (isPoint) {
            for (int i = 0; i < mImageView.length; i++) {
                if (i == selectItemsIndex) {
                    mImageView[i].setImageResource(resId_piont_press);

                } else {
                    mImageView[i].setImageResource(resId_piont);
                }
            }
        }
    }

}


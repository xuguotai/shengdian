package com.tryine.sdgq.view.FullScreenImg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.tryine.sdgq.R;

import java.util.ArrayList;


/**
 * @author assionhonty
 * Created on 2018/9/19 9:41.
 * Email：assionhonty@126.com
 * Function:图片预览页面
 */
public class AssImgPreviewActivity extends AppCompatActivity {

    private ArrayList<String> goodImages;
    private int currentIndex;

    ImageView iv_close;
    private TextView tvNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ass_img_preview_activity);


        Intent intent = getIntent();
        goodImages = intent.getStringArrayListExtra("goodImages");

        currentIndex = intent.getIntExtra("currentIndex", 0);

        AssViewPager vp = findViewById(R.id.vp);
        tvNum = findViewById(R.id.tv_num);
        iv_close = findViewById(R.id.iv_close);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(4);
        vp.setCurrentItem(currentIndex);
        tvNum.setText((currentIndex + 1) + " / " + goodImages.size());
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vp.addOnPageChangeListener(new AssViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvNum.setText((position + 1) + " / " + goodImages.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter implements OnPhotoTapListener {
        private Context mContext;

        MyPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return goodImages.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.ass_img_preview_item, null);
            final ProgressBar pb = view.findViewById(R.id.pb);
            final PhotoView imageView = view.findViewById(R.id.pv);

            Glide.with(mContext).load( goodImages.get(position)).into(imageView);

            imageView.setOnPhotoTapListener(this);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
        //先获取大图的缓存图片
        System.out.println("info==" + imageInfo.toString());
        Bitmap cacheImage = AssNineGridView.getImageLoader().getCacheImage(imageInfo.getBigImageUrl());
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null) {
            cacheImage = AssNineGridView.getImageLoader().getCacheImage(imageInfo.getThumbnailUrl());
        }
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}

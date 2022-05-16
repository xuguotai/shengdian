package com.tryine.sdgq.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tryine.sdgq.R;


/**
 * 作者：qujingfeng
 * 创建时间：2020.04.16 16:04
 */

public class StarBarView extends View {
    private int starDistance = 0; //星星间距
    private int starCount = 5;  //星星个数
    private int starSize;     //星星高度大小，星星一般正方形，宽度等于高度
    private float starMark = 5.0F;   //评分星星
    private Bitmap starFillBitmap; //亮星星
    private Drawable starEmptyDrawable; //暗星星
    private OnStarChangeListener onStarChangeListener;//监听星星变化接口
    private Paint paint;         //绘制星星画笔
    private boolean isEnable = true;
    private int markType=2;//1 整分 2 半分 3 小数
    public StarBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StarBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        setClickable(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        this.starDistance = (int) mTypedArray.getDimension(R.styleable.RatingBar_starDistance, 0);
        this.starSize = (int) mTypedArray.getDimension(R.styleable.RatingBar_starSize, 20);
        this.starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        this.starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        this.starFillBitmap =  drawableToBitmap(mTypedArray.getDrawable(R.styleable.RatingBar_starFill));
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(starFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }


    /**
     * 设置分制
     * @param markType
     */
    public void setMarkType(int markType){
        this.markType = markType;
    }

    /**
     * 设置显示的星星的分数
     *
     * @param mark
     */
    public void setStarMark(float mark){
        if (markType==1) {
            //整分
            starMark = (int) Math.ceil(mark);
        }else {
            starMark = Math.round(mark * 10) * 1.0f / 10;
            if (markType==2) {
                //半分制 小数值大于0.7 进位取整数 小于0.2 舍去 其他取0.5
                int s = (int)starMark;
                float b = Math.round((starMark-s) * 10) * 1.0f / 10;
                if(b>=0.7){
                    starMark = Math.round((s+1) * 10) * 1.0f / 10;;
                }else{
                    if(s<starCount){
                        if(b>=0.2){
                            b = 0.5f+s;
                        }else{
                            b = s;
                        }
                    }else{
                        b = starCount;
                    }

                    starMark = Math.round(b * 10) * 1.0f / 10;;
                }
            }
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(starMark);  //调用监听接口
        }
        invalidate();
    }

    /**
     * 获取显示星星的数目
     *
     * @return starMark
     */
    public float getStarMark(){
        return starMark;
    }


    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 定义是否可以点击
     */
    public void setEnable(boolean enable){
        isEnable = enable;
    }

    /**
     * 设置监听
     * @param onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener){
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize * starCount + starDistance * (starCount - 1), starSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (starFillBitmap == null || starEmptyDrawable == null) {
            return;
        }
        for (int i = 0;i < starCount;i++) {
            starEmptyDrawable.setBounds((starDistance + starSize) * i, 0, (starDistance + starSize) * i + starSize, starSize);
            starEmptyDrawable.draw(canvas);
        }
        if (starMark > 1) {
            canvas.drawRect(0, 0, starSize, starSize, paint);
            if(starMark-(int)(starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            }else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starDistance + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((starMark - (int) (starMark))*10)*1.0f/10), starSize, paint);
            }
        }else {
            canvas.drawRect(0, 0, starSize * starMark, starSize, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if(isEnable){
           int x = (int) event.getX();
           if (x < 0) x = 0;
           if (x > getMeasuredWidth()) x = getMeasuredWidth();
           switch(event.getAction()){
               case MotionEvent.ACTION_DOWN: {
                   setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/starCount));
                   break;
               }
               case MotionEvent.ACTION_MOVE: {
                   setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/starCount));
                   break;
               }
               case MotionEvent.ACTION_UP: {
                   break;
               }
           }
           invalidate();
       }

        return super.onTouchEvent(event);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null)return null;
        Bitmap bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starSize, starSize);
        drawable.draw(canvas);
        return bitmap;
    }

}

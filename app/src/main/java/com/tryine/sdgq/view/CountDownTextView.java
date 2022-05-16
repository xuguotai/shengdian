package com.tryine.sdgq.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CountDownTextView extends AppCompatTextView {
    private CountDownTimer mCountDownTimer;
    private long mCurrentTime = 0;
    private boolean isPause = false;
    private long mMaxTime = 60000;
    public long millisUntilFinished ;
    //当前倒计时时间
    private CurrentTimeListener mCurrentTimeListener;
    //倒计时完成之后的文案
    private String mFinishText = "完成";

    public String getmFinishText() {
        return mFinishText;
    }

    public boolean ishour = false; //是否显示小时
    public void setmFinishText(String mFinishText) {
        this.mFinishText = mFinishText;
    }

    public void setishour(boolean ishour) {
        this.ishour = ishour;
    }


    public CountDownTextView(Context context) {
        this(context, null, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCurrentTimeListener( CurrentTimeListener tCurrentTimeListener){
        this.mCurrentTimeListener = tCurrentTimeListener;
    }

    public void setMaxTime(long time) {
        mMaxTime = time + 0;
        cancelCountDown();
        setText(getCountTimeByLong(mMaxTime));
        initCountDownTimer(mMaxTime);
    }

    public long getmillisUntilFinished(){
        return millisUntilFinished;
    }

    private void init() {

    }

    public void initCountDownTimer(long millisInFuture) {
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished1) {
                mCurrentTime = millisUntilFinished;
                if (mCurrentTimeListener != null){
                    mCurrentTimeListener.currentTime(mCurrentTime);
                }
                millisUntilFinished = millisUntilFinished1;
                setText(getCountTimeByLong(millisUntilFinished));
                isPause = false;
            }

            public void onFinish() {
//                setText(mFinishText);
                if(null != onItemRefreshListener){
                    onItemRefreshListener.onItemRefresh();
                }
            }
        };
    }


    public void startCountDown() {
        if (mCountDownTimer != null && mMaxTime > 1000) {
            isPause = false;
            mCountDownTimer.start();
        }
    }

    public void cancelCountDown() {
        if (mCountDownTimer != null) {
            isPause = false;
            mCountDownTimer.cancel();
        }
    }

    public void resumeCountDown() {
        if (mCurrentTime != 0 && isPause) {
            initCountDownTimer(mCurrentTime);
            mCountDownTimer.start();
            isPause = false;
        }
    }

    public void pauseCountDown() {
        if (!isPause) {
            isPause = true;
            mCountDownTimer.cancel();
        }
    }

    public long getmCurrentTime() {
        return mCurrentTime;
    }

    public interface CurrentTimeListener{
        public void currentTime(long curTime);
    }

    public String getCountTimeByLong(long finishTime) {
        int totalTime = (int) Math.round((double) finishTime / 1000);
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }

        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if(ishour) {
            if (hour < 10) {
                sb.append("0").append(hour).append(":");
            } else {
                sb.append(hour).append(":");
            }
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second).append("");
        } else {
            sb.append(second).append("");
        }
        return sb.toString();

    }


    public OnItemRefreshListener onItemRefreshListener;
    //倒计时为0通知刷新
    public interface OnItemRefreshListener{
        void onItemRefresh();

    }
    public void setOnItemRefreshListener(OnItemRefreshListener onItemRefreshListener){
        this.onItemRefreshListener = onItemRefreshListener;
    }

}

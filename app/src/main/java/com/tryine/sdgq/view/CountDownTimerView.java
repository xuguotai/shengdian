package com.tryine.sdgq.view;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CountDownTimerView extends CountDownTimer {
    private TextView mTextView;
    private String color = "#6F6F6F";

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerView(TextView tv, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = tv;
    }

    public CountDownTimerView(TextView tv, String color, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = tv;
        this.color = color;
    }

    public void setText(String str) {
        mTextView.setText(str);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //设置不可点击
        mTextView.setClickable(false);
        //设置倒计时时间
        mTextView.setTextColor(Color.parseColor(color));
        mTextView.setText("已发送(" + millisUntilFinished / 1000 + "s)");
        mTextView.setPressed(true);

    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取");
        mTextView.setTextColor(Color.parseColor(color));
        mTextView.setClickable(true);//重新获得点击
        mTextView.setPressed(false);
    }
}

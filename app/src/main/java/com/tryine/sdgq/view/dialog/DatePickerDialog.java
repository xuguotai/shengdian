package com.tryine.sdgq.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.tryine.sdgq.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 只提供年和月的选择
 *
 * @author ZuoHailong
 * @date 2019/3/19.
 */
public class DatePickerDialog extends Dialog {

    private TextView tv_close, tv_confirm;
    private static final int DEFAULT_MAX_YEAR = 2050;
    private static final int DEFAULT_MIN_YEAR = 1900;

    private WheelView wheelViewYear, wheelViewMonth;

    private int minYear; // min year
    private int maxYear; // max year
    private boolean cancelable, loopYear, loopMonth;
    private OnDatePickedListener listener;
    private int selectedYear, selectedMonth;
    private static Calendar calendar;

    private static Activity mContext;

    private DatePickerDialog() {
        super(mContext, R.style.ActionSheetDialogStyle);
    }

    //private，只允许通过Builder构建DatePicker
    private static DatePickerDialog newInstance(Activity contex, Builder builder) {
        calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog();
        datePicker.minYear = builder.minYear < DatePickerDialog.DEFAULT_MIN_YEAR ? DatePickerDialog.DEFAULT_MIN_YEAR : builder.minYear;
        datePicker.maxYear = builder.maxYear > DatePickerDialog.DEFAULT_MAX_YEAR ? DatePickerDialog.DEFAULT_MAX_YEAR : builder.maxYear;
        datePicker.cancelable = builder.cancelable;
        datePicker.loopYear = builder.loopYear;
        datePicker.loopMonth = builder.loopMonth;
        datePicker.listener = builder.listener;
        return datePicker;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        wheelViewYear = findViewById(R.id.wheelViewYear);
        wheelViewMonth = findViewById(R.id.wheelViewMonth);
        //设置DialogFragment所在窗口的背景透明
        initView();

        tv_close = findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDatePickCompleted(selectedYear, selectedMonth);
                dismiss();
            }
        });


    }


    private void initView() {
        setCancelable(cancelable);
        initYear(minYear, maxYear);
        initMonth();
        //默认选择当前年份
        selectedYear = calendar.get(Calendar.YEAR);
        //position = 当前年份 - minYear
        wheelViewYear.setCurrentItem(selectedYear - minYear);
        //设置是否循环显示年份
        wheelViewYear.setCyclic(loopYear);
        wheelViewYear.setTextSize(20);

        wheelViewYear.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                selectedYear = yearList.get(position);
            }
        });
        //月份是从0到11，实际选择月份要+1
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        //position = 实际选择月份 - 1
        wheelViewMonth.setCurrentItem(selectedMonth - 1);
        //设置是否循环显示月份
        wheelViewMonth.setCyclic(loopMonth);
        wheelViewMonth.setTextSize(20);


        wheelViewMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                selectedMonth = monthList.get(position);
            }
        });
    }

    //存储可供选择的 年份/月份 集合
    private List<Integer> yearList, monthList;

    private void initYear(int minYear, int maxYear) {
        //避免传入顺序有误
        minYear = Math.min(minYear, maxYear);
        maxYear = Math.max(minYear, maxYear);

        yearList = new ArrayList<>();
        for (int i = 0; i < maxYear + 1 - minYear; i++) {
            yearList.add(minYear + i);
        }
        wheelViewYear.setAdapter(new WheelAdapter() {
            @Override
            public int getItemsCount() {
                return yearList.size();
            }

            @Override
            public Object getItem(int index) {
                return yearList.get(index) + "年";
            }

            @Override
            public int indexOf(Object o) {
                return yearList.indexOf(o);
            }
        });
    }

    private void initMonth() {
        monthList = new ArrayList<>();
        monthList.add(1);
        monthList.add(2);
        monthList.add(3);
        monthList.add(4);
        monthList.add(5);
        monthList.add(6);
        monthList.add(7);
        monthList.add(8);
        monthList.add(9);
        monthList.add(10);
        monthList.add(11);
        monthList.add(12);

        wheelViewMonth.setAdapter(new WheelAdapter() {
            @Override
            public int getItemsCount() {
                return monthList.size();
            }

            @Override
            public Object getItem(int index) {
                return monthList.get(index) + "月";
            }

            @Override
            public int indexOf(Object o) {
                return monthList.indexOf(o);
            }
        });
    }

    public interface OnDatePickedListener {
        void onDatePickCompleted(int year, int month);
    }

    public static class Builder {

        private OnDatePickedListener listener;

        public Builder(Activity contex, OnDatePickedListener listener) {
            mContext = contex;
            this.listener = listener;
        }

        private int minYear;
        private int maxYear;
        private boolean loopYear;
        private boolean loopMonth;
        private boolean cancelable;

        public Builder setYearRange(int minYear, int maxYear) {
            this.minYear = minYear;
            this.maxYear = maxYear;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder loopYear(boolean loop) {
            this.loopYear = loop;
            return this;
        }

        public Builder loopMonth(boolean loop) {
            this.loopMonth = loop;
            return this;
        }

        public DatePickerDialog build() {
            if (minYear > maxYear) {
                throw new IllegalArgumentException();//不允许minYear > maxYear
            }
            return DatePickerDialog.newInstance(mContext, this);
        }

    }

}

package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tryine.sdgq.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日期选择
 */
public class DateDialog extends Dialog {

    Context mContext;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    private int currentYear; //系统当前时间
    private int currentMonth;
    private int currentDay;
    private List listYear = new ArrayList();
    private List listMonth = new ArrayList<>();
    private List listDay = new ArrayList<>();

    private int selectYear = 0;
    private int selectMonth = 0;

    public DateDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = context;
        initCurrentDate();
        initYear();
        initMonth();
        initDay();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.view_dialog_date, null);
        ButterKnife.bind(this, mMenuView);
        setContentView(mMenuView);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        wv_year = findViewById(R.id.wv_year);
        wv_month = findViewById(R.id.wv_month);
        wv_day = findViewById(R.id.wv_day);
        //设置年数据
        wv_year.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wv_year.setSkin(WheelView.Skin.None); // common皮肤
        wv_year.setWheelData(listYear);
        wv_year.setSelection(0);
        wv_year.setWheelSize(5);
        wv_year.setVisibility(View.VISIBLE); //解决数据加载延迟
        wv_year.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                updateDay();
            }
        });

        //月
        wv_month.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wv_month.setSkin(WheelView.Skin.None); // common皮肤
        wv_month.setWheelData(listMonth);
        wv_month.setSelection(currentMonth);
        wv_month.setVisibility(View.VISIBLE);
        wv_month.setWheelSize(5);
        wv_month.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                updateDay();
            }
        });

        //日
        wv_day.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wv_day.setSkin(WheelView.Skin.None); // common皮肤
        wv_day.setWheelData(listDay);
        wv_day.setWheelSize(5);
        wv_day.setSelection(currentDay - 1);
        wv_day.setVisibility(View.VISIBLE);

        WheelView.WheelViewStyle wheelViewStyle = new WheelView.WheelViewStyle();
        wheelViewStyle.textColor = 0xffE1E1E1;
        wheelViewStyle.selectedTextColor = 0xff333333;
        wv_year.setStyle(wheelViewStyle);
        wv_month.setStyle(wheelViewStyle);
        wv_day.setStyle(wheelViewStyle);


    }

    /**
     * 初始化系统当前时间
     */
    private void initCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MARCH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 用户滑动时更新每个月的天数
     */
    private void updateDay() {
        //获取当前滑动的位置
        selectYear = Integer.parseInt(wv_year.getSelectionItem().toString().replace("年", ""));
        selectMonth = Integer.parseInt(wv_month.getSelectionItem().toString().replace("月", ""));
        //判断一个月有多少天
        int hasDay = getDay(selectYear, selectMonth);
        listDay.clear();
        for (int i = 1; i <= hasDay; i++) {
            listDay.add(String.format("%02d", i) + "日");
        }
        wv_day.setWheelData(listDay);
    }


    /**
     * 集合添加年
     */
    private void initYear() {
        for (int i = 0; i < 90; i++) {
            listYear.add((currentYear - i) + "年");
        }
    }

    /**
     * 集合添加月
     */
    private void initMonth() {
        for (int i = 1; i <= 12; i++) {
            listMonth.add(String.format("%02d", i) + "月");
        }
    }

    /**
     * 集合添加天数
     */
    private void initDay() {

        //判断一个月有多少天
        int hasDay = getDay(currentYear, (currentMonth + 1));
        for (int i = 1; i <= hasDay; i++) {
            listDay.add(String.format("%02d", i) + "日");
        }
    }

    /**
     * 根据是否闰年和月份判断本月的天数
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;

        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    private OnSelectDateListener onSelectDateListener;

    public void setSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
    }


    public interface OnSelectDateListener {
        void getDate(String date);
    }


    @OnClick({R.id.iv_close, R.id.iv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_confirm:
                if (null != onSelectDateListener) {
                    onSelectDateListener.getDate(getDate());
                }
                dismiss();
                break;


        }
    }


    /**
     * 获取时间
     *
     * @return
     */
    public String getDate() {
        String year = wv_year.getSelectionItem().toString().replace("年", "");
        String month = wv_month.getSelectionItem().toString().replace("月", "");
        String day = wv_day.getSelectionItem().toString().replace("日", "");
        return year + "-" + month + "-" + day;
    }


}

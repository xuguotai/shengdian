package com.tryine.sdgq.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.activity.VideoPlayFullScreenActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.live.adapter.LiveChapterAdapter;
import com.tryine.sdgq.common.live.adapter.LiveCourseDateAdapter;
import com.tryine.sdgq.common.live.bean.LiveCourseDateBean;
import com.tryine.sdgq.common.live.bean.LiveCourseDetailBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.List;

/**
 * 一对一直播课购买
 */
public class BuyLiveCourseDialog extends Dialog {
    private Activity mContext;
    List<LiveCourseDateBean> liveCourseDateBeanList;

    RecyclerView rv_data;
    ImageView iv_close;
    TextView tv_confirm;

    int selectedPosition = -1;

    public BuyLiveCourseDialog(Activity context, List<LiveCourseDateBean> liveCourseDateBeanList) {
        super(context, R.style.ActionSheetDialogStyle);
        this.liveCourseDateBeanList = liveCourseDateBeanList;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buylivecourse);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rv_data = findViewById(R.id.rv_data);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    if(selectedPosition != -1){
                        onItemClickListener.resultReason(liveCourseDateBeanList.get(selectedPosition));
                        dismiss();
                    }else{
                        ToastUtil.toastLongMessage("选择预约时间");
                    }

                }
            }
        });
        initView();
    }

    private void initView() {
        LiveCourseDateAdapter liveCourseDateAdapter = new LiveCourseDateAdapter(mContext, liveCourseDateBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_data.setLayoutManager(lin);
        rv_data.setAdapter(liveCourseDateAdapter);
        liveCourseDateAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(liveCourseDateBeanList.get(position).getIsSub().equals("0")){
                    selectedPosition = position;
                    liveCourseDateAdapter.setSelectedTabPosition(position);
                }
            }
        });

    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void resultReason(LiveCourseDateBean liveCourseDateBean);
    }

}

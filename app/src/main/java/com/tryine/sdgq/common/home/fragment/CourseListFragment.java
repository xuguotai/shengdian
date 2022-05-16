package com.tryine.sdgq.common.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.activity.CourseDateilActivity;
import com.tryine.sdgq.common.home.adapter.CourseListAdapter;
import com.tryine.sdgq.common.mine.adapter.JDDetailRecordAdapter;
import com.tryine.sdgq.common.mine.adapter.TabBtnAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 课程
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:13
 */
public class CourseListFragment extends BaseFragment {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<String> recordList = new ArrayList<>();
    CourseListAdapter courseListAdapter;


    @Override
    public int getlayoutId() {
        return R.layout.fragment_course_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    protected void initViews() {
        setWhiteBar();

        for (int i = 0; i < 5; i++) {
            recordList.add("");
        }
//        courseListAdapter = new CourseListAdapter(getContext(), recordList);
//        LinearLayoutManager lin = new LinearLayoutManager(getContext());
//        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
//        rc_data.setLayoutManager(lin);
//        rc_data.setAdapter(courseListAdapter);
//        courseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                CourseDateilActivity.start(mContext);
//            }
//        });

    }


}

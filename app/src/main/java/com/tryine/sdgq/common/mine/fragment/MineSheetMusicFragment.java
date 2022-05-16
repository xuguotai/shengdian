package com.tryine.sdgq.common.mine.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.home.activity.SheetMusicDetailActivity;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SheetMusicPresenter;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.common.mine.adapter.FansAdapter;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.presenter.MinePresenter;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的琴谱
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:19
 */
public class MineSheetMusicFragment extends BaseFragment implements SheetMusicView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    CommonAdapter childAdapter;
    List<HomeMenuBean> childMenuLists = new ArrayList<>();
    SheetMusicPresenter sheetMusicPresenter;

    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_tycard_list;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        smartRefresh();
        sheetMusicPresenter = new SheetMusicPresenter(mContext);
        sheetMusicPresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    protected void initViews() {

        childAdapter = new CommonAdapter(mContext, R.layout.item_sheetmusic_second, childMenuLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                HomeMenuBean categoryBean = (HomeMenuBean) o;
                holder.setText(R.id.tv_title, categoryBean.getName());
                TextView tv_silverBean = holder.getView(R.id.tv_silverBean);
                ImageView iv_isUnlock = holder.getView(R.id.iv_isUnlock);
                tv_silverBean.setText(categoryBean.getSilverBean());
                tv_silverBean.setVisibility(View.GONE);

                if (null != categoryBean.getIsUnlock() && !categoryBean.getIsUnlock().equals("1")) {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_suo);
                } else {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_per_right);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SheetMusicDetailActivity.start(mContext, childMenuLists.get(position).getPiaonScoreId());

                    }
                });
            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(childAdapter);
    }

    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                sheetMusicPresenter.getMypiaonscorelist(pageNum);

            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                sheetMusicPresenter.getMypiaonscorelist(pageNum);
            }
        });
    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetChildMenuSuccess(List<HomeMenuBean> homeMenuBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            childMenuLists.clear();
        }
        childMenuLists.addAll(homeMenuBeanList);
        if (childMenuLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBuypiaonscoreSuccess() {

    }

    @Override
    public void onGetpiaonscoredetailSuccess(SheetMusicBean sheetMusicBean, List<VideoModel> videoModelList) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

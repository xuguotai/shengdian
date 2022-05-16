package com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIImageLoader;
import com.tryine.sdgq.view.CircleImageView;

import java.util.List;


/**
 * 礼物面板礼物Item adapter
 */
public class TUIGiftPanelAdapter extends RecyclerView.Adapter<TUIGiftPanelAdapter.ViewHolder> {
    private Context mContext;
    private int mPageIndex;
    private List<TUIGiftModel> mGiftModelList;
    private OnItemClickListener mOnItemClickListener;

    int selectedTabPosition = -1;

    public TUIGiftPanelAdapter(int pageIndex, List<TUIGiftModel> list, Context context) {
        super();
        mGiftModelList = list;
        mContext = context;
        mPageIndex = pageIndex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tuigift_panel_recycle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TUIGiftModel giftModel = mGiftModelList.get(position);
        TUIImageLoader.loadImage(mContext, holder.mImageGift, giftModel.normalImageUrl);
        TUIImageLoader.loadImage(mContext, holder.mImageGift1, giftModel.normalImageUrl);
        holder.mTextGiftName.setText(giftModel.giveDesc);
        if("0".equals(giftModel.price)){
            holder.tv_price.setText("免费");
            holder.tv_price1.setText("免费");
        }else{
            if (giftModel.sdType.equals("0")) {//类型 0:金豆礼物 1:银豆礼物
                holder.tv_price.setText(giftModel.price + "SD金豆");
                holder.tv_price1.setText(giftModel.price + "SD金豆");
            } else {
                holder.tv_price.setText(giftModel.price + "SD银豆");
                holder.tv_price1.setText(giftModel.price + "SD银豆");
            }
        }

        if (selectedTabPosition == position) {
            holder.mLayoutRootView.setVisibility(View.GONE);
            holder.mLayoutRootView1.setVisibility(View.VISIBLE);
        } else {
            holder.mLayoutRootView.setVisibility(View.VISIBLE);
            holder.mLayoutRootView1.setVisibility(View.GONE);
        }

        holder.mLayoutRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemSelectClick(position);
            }
        });

        holder.mLayoutRootView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemSelectClick(position);
            }
        });

        holder.tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, giftModel, position, mPageIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGiftModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        LinearLayout mLayoutRootView;
        CircleImageView mImageGift;
        TextView mTextGiftName;
        TextView tv_price;

        RelativeLayout mLayoutRootView1;
        CircleImageView mImageGift1;
        TextView tv_price1;

        TextView tv_send;


        public ViewHolder(View view) {
            super(view);
            root = (LinearLayout) view.findViewById(R.id.root);
            mLayoutRootView = (LinearLayout) view.findViewById(R.id.ll_gift_root);
            mImageGift = (CircleImageView) view.findViewById(R.id.iv_gift_icon);
            mTextGiftName = (TextView) view.findViewById(R.id.tv_gift_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);

            mLayoutRootView1 = (RelativeLayout) view.findViewById(R.id.ll_gift_root1);
            mImageGift1 = (CircleImageView) view.findViewById(R.id.iv_gift_icon1);
            tv_price1 = (TextView) view.findViewById(R.id.tv_price1);
            tv_send = (TextView) view.findViewById(R.id.tv_send);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, TUIGiftModel giftModel, int position, int pageIndex);

        void onItemSelectClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public void setselectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();
    }

}

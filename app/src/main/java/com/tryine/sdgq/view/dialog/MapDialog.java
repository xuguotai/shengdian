package com.tryine.sdgq.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.util.NavUtil;

import org.json.JSONArray;


/**
 * 描述
 * 导航地图选择
 */

public class MapDialog extends Dialog {
    private Activity mContext;
    JSONArray jsonArray;

    LinearLayout ll_type;
    TextView tv_cancel;

    public MapDialog(Activity context, final JSONArray jsonArray) {
        super(context, R.style.ActionSheetDialogStyle);
        this.jsonArray = jsonArray;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_type = findViewById(R.id.ll_type);
        initView();
    }

    private void initView() {
        for (int i = 0; i < jsonArray.length(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_map, null);
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(jsonArray.optJSONObject(i).optString("type"));
            final int index = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtil.startNav(mContext, jsonArray.optJSONObject(index));
                    dismiss();
                }
            });
            ll_type.addView(view);
        }

    }

}

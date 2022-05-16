package com.tryine.sdgq.common.mine.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.adapter.TyCardUseRecordListAdapter;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.common.mine.presenter.TyCardPresenter;
import com.tryine.sdgq.common.mine.view.TyCardView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的体验卡转赠好友
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 10:19
 */
public class TyCardZZActivity extends BaseActivity implements TyCardView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_phone)
    EditText et_phone;

    TyCardPresenter tyCardPresenter;

    String cardId;

    public static void start(Context context,String cardId) {
        Intent intent = new Intent();
        intent.setClass(context, TyCardZZActivity.class);
        intent.putExtra("cardId",cardId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tycard_zz;
    }

    @Override
    protected void init() {
        setWhiteBar();
        cardId = getIntent().getStringExtra("cardId");
        tv_title.setText("转赠好友");
        tyCardPresenter = new TyCardPresenter(mContext);
        tyCardPresenter.attachView(this);

    }

    @OnClick({R.id.iv_black, R.id.ll_gosj, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_gosj:
                intentToContact();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x30) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name", "data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum = phoneNum.replaceAll(" ", "");
                }

                et_phone.setText(phoneNum + "");
            }
        }
    }


    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 0x30);
    }


    private void submit() {
        if ("".equals(getTextStr(et_phone))) {
            ToastUtil.toastLongMessage("请输入好友手机号码");
            return;
        }

        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示", "是否确认赠送", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                tyCardPresenter.getforwarding(cardId, et_phone.getText().toString());
            }

            @Override
            public void cancel() {
            }
        });

    }


    @Override
    public void onGetCardBeanListSuccess(List<CardBean> cardBeanList, int pages) {

    }

    @Override
    public void onGetExperienceBeanSuccess(List<ExperienceBean> experienceBeanLists, int pages) {

    }

    @Override
    public void onGetCardBeanListSuccess() {

    }

    @Override
    public void onForwardingSuccess() {
        ToastUtil.toastLongMessage("赠送成功");
        et_phone.setText("");
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}

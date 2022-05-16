package com.tryine.sdgq.mvp;

import android.content.Context;


/**
 * Created by zhuhan on 17/12/27.
 */

/**
 * Presenter接口基类
 */
public class BasePresenter implements IPresenter {
	protected Context mContext;

	public BasePresenter(Context context){
		this.mContext=context;
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void attachView(BaseView view) {

	}
}

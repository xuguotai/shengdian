package com.tryine.sdgq.mvp;


/**
 * Created by zhuhan on 17/12/27.
 */


/**
  * presenter interface基类
 */

public interface IPresenter {

	void onResume();

	void onPause();

	void onDestroy();

	void attachView(BaseView view);
}

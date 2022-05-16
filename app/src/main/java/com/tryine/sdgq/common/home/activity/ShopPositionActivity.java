package com.tryine.sdgq.common.home.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.util.NavUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * 商家的地图位置
 */
public class ShopPositionActivity extends Activity {
    MapView tMapView;
    TextView tv_address;
    TextView tv_insure;
    ImageView iv_close;

    private AMap tMap;
    Marker marker = null;
    String address;
    String longLat;

    public static void start(Context context, String longLat, String address) {
        Intent intent = new Intent();
        intent.setClass(context, ShopPositionActivity.class);
        intent.putExtra("longLat", longLat);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_shop_postition);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarColor(R.color.white)
                .init();

        longLat = getIntent().getStringExtra("longLat");
        address = getIntent().getStringExtra("address");

        tMapView = (MapView) findViewById(R.id.tMapView);
        tMapView.onCreate(savedInstanceState);// 此方法必须重写

        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_insure = (TextView) findViewById(R.id.tv_insure);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setText(address);


        NavUtil.location(this);

        addMarkersToMap();
        setListener();
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        if (tMap == null) {
            tMap = tMapView.getMap();
        }

        LatLng latlng = new LatLng(Double.parseDouble(longLat.split(",")[1]), Double.parseDouble(longLat.split(",")[0]));
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_poi))
                .position(latlng)
                .title("")
                .draggable(true);
        marker = tMap.addMarker(markerOption);
        tMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


    }


    private void setListener() {

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_insure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //导航
                NavUtil.toNav(ShopPositionActivity.this, longLat, address);
            }
        });

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        tMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        tMapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tMapView.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 关闭输入法
     *
     * @param
     */
    public void closeInput() {
        try {
            if (null != getCurrentFocus().getWindowToken()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
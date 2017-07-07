package com.example.administrator.kib_3plus;

import android.app.Application;

import cn.appscomm.presenter.PresenterAppContext;
import cn.appscomm.presenter.implement.PBluetooth;
import cn.appscomm.sp.SPKey;
import cn.appscomm.sp.SPManager;

/**
 * Created by cui on 2017/6/10.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        PresenterAppContext.INSTANCE.init(getApplicationContext(), true, false);
        PBluetooth.INSTANCE.startService();
        PublicData.InitDragListData(getApplicationContext());

        super.onCreate();
    }
}

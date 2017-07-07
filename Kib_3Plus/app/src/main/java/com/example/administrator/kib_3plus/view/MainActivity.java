package com.example.administrator.kib_3plus.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.kib_3plus.Utils.BluetoothUtils;
import com.example.administrator.kib_3plus.Utils.LogUtils;
import com.example.administrator.kib_3plus.R;
import com.example.administrator.kib_3plus.view.fragment.CreateNewAccountFragment;
import com.example.administrator.kib_3plus.view.fragment.MainFailyFragment;
import com.example.administrator.kib_3plus.view.fragment.WelcomeFragmet;
import com.example.administrator.kib_3plus.view.manage.ContentViewManage;
import com.example.administrator.kib_3plus.view.manage.NavigationManage;
import com.example.administrator.kib_3plus.view.manage.TitleManage;

import cn.appscomm.sp.SPKey;
import cn.appscomm.sp.SPManager;

public class MainActivity extends FragmentActivity  {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        getSupportFragmentManager();
        findView();
        init();
        initView();
        initData();
    }
    private void initView() {
        boolean isSign=false;
        boolean isFamily=false;
        if(SPManager.INSTANCE.getSPValue(SPKey.SP_IS_SIGN_L28t, SPManager.DATA_BOOLEAN)==null){
            isSign=false;
        }else{
            isSign=(boolean)SPManager.INSTANCE.getSPValue(SPKey.SP_IS_SIGN_L28t, SPManager.DATA_BOOLEAN);

        }
        if(SPManager.INSTANCE.getSPValue(SPKey.SP_IS_FAMILY_L28t, SPManager.DATA_BOOLEAN)==null){
            isFamily=false;
        }else{
            isFamily=(boolean)SPManager.INSTANCE.getSPValue(SPKey.SP_IS_SIGN_L28t, SPManager.DATA_BOOLEAN);

        }
        LogUtils.i("isSign="+isSign+"--isFamily="+isFamily);
        if(isSign){
            if(isFamily){
                ContentViewManage.getInstance().showOneFragment(MainFailyFragment.class.getSimpleName(), MainFailyFragment.getInstance());
                NavigationManage.getInstance().setType(NavigationManage.NA_TYPE_VISIBLE);
                //SP_FAMILY_NAME_L28t
                String familyName=(String)SPManager.INSTANCE.getSPValue(SPKey.SP_FAMILY_NAME_L28t, SPManager.DATA_STRING);
                TitleManage.getInstance().setType(TitleManage.TITLE_PURPLE_MAINNAME,familyName,null,getString(R.string.title_save));
            }else{
                ContentViewManage.getInstance().showOneFragment(CreateNewAccountFragment.class.getSimpleName(), CreateNewAccountFragment.getInstance());
                NavigationManage.getInstance().setType(NavigationManage.NA_TYPE_GONE);
                TitleManage.getInstance().setType(TitleManage.TITLE_GONE,getString(R.string.title_set_up_device),null,getString(R.string.title_save));
            }

        }else{
            ContentViewManage.getInstance().showOneFragment(WelcomeFragmet.class.getSimpleName(), WelcomeFragmet.getInstance());
            NavigationManage.getInstance().setType(NavigationManage.NA_TYPE_GONE);
            TitleManage.getInstance().setType(TitleManage.TITLE_GONE,getString(R.string.title_set_up_device),null,getString(R.string.title_save));
        }

    }
    @Override
    public void onBackPressed() {
        if(!ContentViewManage.getInstance().onBackPressed()){
            confirm_exit();
        }
    }
    private void confirm_exit() {
        TextView tv1 = new TextView(this);
        tv1.setText(R.string.ExitApp);
        tv1.setTextColor(Color.rgb(255, 255, 255));
        tv1.setTextSize(22);
        tv1.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.ExitApp);
        builder.setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LogUtils.i("==>>stopMyPushMsgService");

                        System.exit(0);
                    }
                });
            }
        });

        builder.show();
    }
    private void initData() {
    }

    private void findView() {
    }

    private void init() {
        NavigationManage.getInstance().init(this);
        TitleManage.getInstance().init(this);
        ContentViewManage.getInstance().init(this);
        BluetoothUtils.init(MainActivity.this);
    }





}

package com.example.administrator.kib_3plus.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.kib_3plus.R;
import com.example.administrator.kib_3plus.ScanDeviceModel;
import com.example.administrator.kib_3plus.http.mode.AddMenberMode;

import java.util.Calendar;

import cn.appscomm.bluetooth.interfaces.PMBluetoothCall;
import cn.appscomm.presenter.implement.PBluetooth;
import cn.appscomm.presenter.interfaces.PVBluetoothCallback;

import static android.R.attr.id;
import static com.facebook.GraphRequest.TAG;

/**
 * Created by cui on 2017/6/28.
 */

public class BluetoothUtils {
    private final static int REQUEST_ENABLE_BT = 0x9991;

    public static AlertDialog d=null;
    private static Activity mActivity;
    public static void init(Activity activity){
        mActivity=activity;
    }

    /**
     * 检查蓝牙
     * @param
     * @return
     */
    public static boolean isOpenBle(){
        BluetoothManager bluetoothManager = (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        LogUtils.i(TAG, "==>>firstSync mBluetoothAdapter.isEnabled(): " + mBluetoothAdapter.isEnabled());
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }else{
            return true;

        }
    }




    /**
     * 检查GPS权限
     *
     * @return true：有权限 false：没权限
     */
    public static boolean checkGPSPermission() {
        if ((ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            LogUtils.i(TAG, "没有GPS权限.............");
            ActivityCompat.requestPermissions((Activity) (mActivity), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
            return false;
        }
        LogUtils.i(TAG, "有GPS权限.............");
        return true;
    }

    /**
     * 检查GPS
     *
     * @param isShowTip 是否显示打开GPS提示
     * @return true : 打开 ; false : 关闭
     */
    public static boolean checkGPSStatus(boolean isShowTip) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            LogUtils.i(TAG, "手机Android系统是6.0或以上的，需要检查GPS...");
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsProviderFlag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProviderFlag = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            LogUtils.i(TAG, "gpsProviderFlag : " + gpsProviderFlag + " networkProviderFlag : " + networkProviderFlag);
            if (!(gpsProviderFlag || networkProviderFlag)) {
                LogUtils.i(TAG, "GPS没有打开");
                if (isShowTip) {
                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                DialogUtil.showChooseDialog(activity, R.string.s_public_open_gps_tip, new DialogUtil.DialogCallBackOK() {
//                                    @Override
//                                    public void onClickOK() {
                                mActivity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 6666);
//                                    }
//                                });
                            }
                        });
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static void closeDialog(){
        if(d!=null&&d.isShowing()){
            d.dismiss();
        }
    }

    public static void bindDevice(ScanDeviceModel scanDeviceModel, AddMenberMode addMenberMode, PVBluetoothCallback mPvBluetoothCallback){

        LogUtils.i("mDeviceList="+PBluetooth.INSTANCE.isConnect(scanDeviceModel.deviceID));

        getSoftwareVersion(mPvBluetoothCallback,scanDeviceModel.deviceID);
        getWatvhID(mPvBluetoothCallback,scanDeviceModel.deviceID);
        setUID(mPvBluetoothCallback,scanDeviceModel.deviceID,addMenberMode.getData().getId());
        bindInfo(mPvBluetoothCallback,scanDeviceModel.deviceID, addMenberMode);
        setName(mPvBluetoothCallback,scanDeviceModel.deviceID,addMenberMode.getData().getName());
        setTime(mPvBluetoothCallback,scanDeviceModel.deviceID);


    }

    /**
     * 获取软件版本
     * @param mPvBluetoothCallback
     * @param mac
     */
    public static void getSoftwareVersion(PVBluetoothCallback mPvBluetoothCallback ,String mac){
        byte[] bytes=new byte[]{0x6E, 0x01, (byte) 0x03, 0x03, (byte) 0x8F};
        PBluetooth.INSTANCE.getSoftwareVersion(mPvBluetoothCallback
                , PMBluetoothCall.COMMAND_TYPE_BIND,bytes,mac);
    }
    /**
     * 获取watchid
     *
     *
     */
    public static void getWatvhID(PVBluetoothCallback mPvBluetoothCallback ,String mac){
        byte[] bytes=new byte[]{0x6E, 0x01, 0x04, 0x01, (byte) 0x8F};
        PBluetooth.INSTANCE.getWatchIDL28T(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_BIND,bytes,mac);

    }

    /**
     * 设置id
     *
     */
    public static void setUID(PVBluetoothCallback mPvBluetoothCallback ,String mac,int id){
        byte[] bytes=new byte[]{0x6E, 0x01, 0x4A, 0x01,(byte)0xE7,(byte)0x86,0x00,0x00, (byte) 0x8F};//6E 01 4A 01 E7 86 00 00 8F
        int userId = new Integer(id);
        bytes[4] = NumberUtils.intToByteArray(userId)[3];
        bytes[5] = NumberUtils.intToByteArray(userId)[2];

        bytes[6] = NumberUtils.intToByteArray(userId)[1];
        bytes[7] = NumberUtils.intToByteArray(userId)[0];
        PBluetooth.INSTANCE.setUIDL28T(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_BIND,bytes,mac);

    }
    /**
     * 设置NAME
     *
     */
    public static void setName(PVBluetoothCallback mPvBluetoothCallback ,String mac,String name){
        //6E 01 4D 01 0B xx xx xx xx ...xx 8f   内容不嫩大于11位数
       byte[] nameByte= name.getBytes();
       int len= nameByte.length;
        byte []bytes=new byte[17];
        bytes[0]=(byte)0x6e;
        bytes[1]=(byte)0x01;
        bytes[2]=(byte)0x4d;
        bytes[3]=(byte)0x01;
        bytes[4]=(byte)len;
        bytes[16]=(byte)0x8f;
        for(int i=0;i<nameByte.length;i++){
            bytes[i+5]=nameByte[i];
        }
        PBluetooth.INSTANCE.setName(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_BIND,bytes,mac);

    }
    /**
     * 设置个人信息
     *
     */
    public static void bindInfo(PVBluetoothCallback mPvBluetoothCallback, String mac,AddMenberMode addMenberMode){
        byte[] bytes=new byte[]{0x6E, 0x01, 0x12, 0x00,(byte)0xE1,(byte)0x07,0x03,0x1D,0x5A,(byte)0xC0,0x26, (byte) 0x8F};//6E 01 12 00 E1 07 03 1D 5A C0 26 8F

        int isGender=new Integer(addMenberMode.getData().getGender());
        bytes[3] = (byte) isGender;
        String brithday=addMenberMode.getData().getBrithday();
       String year= brithday.split("-")[0];
       String mon= brithday.split("-")[1];
       String day= brithday.split("-")[2];
        int   current_year=new Integer(year);
        int   current_mon=new Integer(mon);
        int   current_day=new Integer(day);
        double d_height = 0, d_weight = 0;
        d_height=NumberUtils.ftToCm(addMenberMode.getData().getHeight());
        d_weight=NumberUtils.lbsToKg(addMenberMode.getData().getWeight());
        bytes[4] = NumberUtils.intToByteArray(current_year)[3]; // year
        bytes[5] = NumberUtils.intToByteArray(current_year)[2];
        bytes[6] = (byte) current_mon; // month
        bytes[7] = (byte) current_day; // day
        bytes[8] = (byte) d_height; // height
        bytes[9] = NumberUtils.intToByteArray((int) d_weight)[3]; // weight
        bytes[10] = NumberUtils.intToByteArray((int) d_weight)[2];
        PBluetooth.INSTANCE.bindInfo(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_BIND,bytes,mac);
    }

    /**
     *  设置时间
     *
     */
    public static void setTime(PVBluetoothCallback mPvBluetoothCallback ,String mac){
        //6E 01 15 E1 07 06 0D 09 24 09 8F
//        byte[] bytes=new byte[]{0x6E, 0x01, 0x15, (byte)0xE1,(byte)0x07,(byte)0x06,0x0D,0x09,0x24,(byte)0x09, (byte) 0x8F};

            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(1);
            byte abyte0[] = new byte[11];
            abyte0[0] = 110;
            abyte0[1] = 1;
            abyte0[2] = 21;
            abyte0[3] = (byte) i;
            abyte0[4] = (byte) (i >> 8);
            abyte0[5] = (byte) (1 + calendar.get(2));
            abyte0[6] = (byte) calendar.get(5);
            abyte0[7] = (byte) calendar.get(11);
            abyte0[8] = (byte) calendar.get(12);
            abyte0[9] = (byte) calendar.get(13);
            abyte0[10] = -113;
        PBluetooth.INSTANCE.setBaseTime(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_BIND,abyte0,mac);
    }

    /**
     * 获取电量
     *
     */
    public static void getPower(PVBluetoothCallback mPvBluetoothCallback,String mac){
        byte[] bytes=new byte[]{0x6E, 0x01, 0x0F, 0x01, (byte) 0x8F};
        PBluetooth.INSTANCE.getPower(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }
    /**
     * 设置震动强度
     *
     */
    public static void setVibration(PVBluetoothCallback mPvBluetoothCallback,String mac,byte vi){
        //6E 01 4F 01 01 8f
        byte[] bytes=new byte[]{0x6E, 0x01, 0x4F, 0x01,vi, (byte) 0x8F};
        PBluetooth.INSTANCE.setVibration(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }
    /**
     * 设置时间格式
     *
     */
    public static void setTimeFormat(PVBluetoothCallback mPvBluetoothCallback,String mac,byte tf){
        //6E 01 34 02 8F //24H
//        6E 01 34 03 8F //12H
        byte[] bytes=new byte[]{0x6E, 0x01, 0x34,tf, (byte) 0x8F};
        PBluetooth.INSTANCE.setTimeFormat(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }
    /**
     * 设置手动删除。不知道干嘛的。。
     *
     */
    public static void setDel(PVBluetoothCallback mPvBluetoothCallback,String mac){
        //0x6E, 0x01, 0x32, 0x04, (byte) 0x8F
        byte[] bytes=new byte[]{0x6E, 0x01, 0x32, 0x04, (byte) 0x8F};
        PBluetooth.INSTANCE.setDel(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }

    /**
     * 运动总数
     * @param mPvBluetoothCallback
     * @param
     */
    public static void getStepTotal(PVBluetoothCallback mPvBluetoothCallback,String mac){
//        0x6E, 0x01, 0x30, 0x01, (byte) 0x8F
        byte[] bytes=new byte[]{0x6E, 0x01, 0x30, 0x01, (byte) 0x8F};
        PBluetooth.INSTANCE.getStepTotal(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }
    /**
     * 运动总数
     * @param mPvBluetoothCallback
     * @param
     */
    public static void getStepData(PVBluetoothCallback mPvBluetoothCallback,String mac,int len){
//        0x6E, 0x01, 0x30, 0x01, (byte) 0x8F
        byte[] bytes=new byte[]{0x6E, 0x01, 0x06, 0x01, (byte) 0x8F};
        PBluetooth.INSTANCE.getStepData(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,len,mac);

    }

    /**
     * 睡眠还是运动状态
     * @param mPvBluetoothCallback
     * @param
     */
    public static void getSleepState(PVBluetoothCallback mPvBluetoothCallback,String mac){
//        {0x6E, 0x01, 0x36, (byte) 0x80, 0, 0, 0, (byte) 0x8F}
        byte[] bytes=new byte[]{0x6E, 0x01, 0x36,(byte) 0x80, 0x00,0x00,0x00, (byte) 0x8F};
        PBluetooth.INSTANCE.getSleepState(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }
    /**
     * 删除运动
     * @param mPvBluetoothCallback
     * @param
     */
    public static void delStepData(PVBluetoothCallback mPvBluetoothCallback,String mac){
//        {0x6E, 0x01, 0x36, (byte) 0x80, 0, 0, 0, (byte) 0x8F}
        byte[] bytes = new byte[]{0x6E, 0x01, 0x32, 0x01, (byte) 0x8F};
        PBluetooth.INSTANCE.delStepData(mPvBluetoothCallback, PMBluetoothCall.COMMAND_TYPE_SYNC,bytes,mac);

    }

}

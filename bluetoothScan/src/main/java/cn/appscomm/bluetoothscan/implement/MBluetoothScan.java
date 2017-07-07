package cn.appscomm.bluetoothscan.implement;

import cn.appscomm.bluetoothscan.BluetoothScan;
import cn.appscomm.bluetoothscan.interfaces.IBluetoothScanResultCallBack;
import cn.appscomm.bluetoothscan.interfaces.PMBluetoothScanCall;

/**
 * 作者：hsh
 * 日期：2017/3/27
 * 说明：蓝牙扫描M层
 */

public enum MBluetoothScan implements PMBluetoothScanCall {
    INSTANCE;

    @Override
    public boolean startScan(IBluetoothScanResultCallBack callBack) {
        return BluetoothScan.INSTANCE.startScan(callBack);
    }

    @Override
    public void stopScan() {
        BluetoothScan.INSTANCE.stopScan();
    }

    @Override
    public void addBluetoothScanResultCallBack(IBluetoothScanResultCallBack iBluetoothScanResultCallBack) {
        BluetoothScan.INSTANCE.addBluetoothScanResultCallBack(iBluetoothScanResultCallBack);
    }
}

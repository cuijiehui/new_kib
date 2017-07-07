package cn.appscomm.presenter.interfaces;

/**
 * 作者：hsh
 * 日期：2017/5/5
 * 说明：P层告诉V层拍照动作
 */

public interface IRemoteControl {

    /**
     * 开始拍照
     */
    void startTakePhoto();

    /**
     * 结束拍照
     */
    void endTakePhoto();
}

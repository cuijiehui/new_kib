package cn.appscomm.bluetooth.protocol.MessagePush;

import cn.appscomm.bluetooth.BluetoothCommandConstant;
import cn.appscomm.bluetooth.util.ParseUtil;
import cn.appscomm.bluetooth.interfaces.IBluetoothResultCallback;
import cn.appscomm.bluetooth.protocol.Leaf;

/**
 * Created by Administrator on 2016/8/12.
 */
public class SchedulePush extends Leaf {

    /**
     * 日程推送
     * 构造函数(0x71)
     *
     * @param iBluetoothResultCallback
     * @param len                      内容长度
     * @param type                     类型(0x01：日程内容   0x02：日程时间日期(格式为年月日‘T’时分秒))
     * @param pushContent              推送内容
     */
    public SchedulePush(IBluetoothResultCallback iBluetoothResultCallback, int len, byte type, byte[] pushContent) {
        super(iBluetoothResultCallback, BluetoothCommandConstant.COMMAND_CODE_SCHEDULE_PUSH, BluetoothCommandConstant.ACTION_SET);
        byte[] contentLen = ParseUtil.intToByteArray(len, 2);
        byte[] content = new byte[len];
        content[0] = type;
        System.arraycopy(pushContent, 0, content, 1, pushContent.length);
        super.setContentLen(contentLen);
        super.setContent(content);
    }
}


package com.hrw.linphonelibrary.listener;

import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/17 9:53
 * @desc:
 */
public interface OnLoginListener {
    //     switch (state) {
//        case Ok: // This state means you are connected, to can make and receive calls & messages
//            mLed.setImageResource(R.drawable.led_connected);
//            System.out.println("链接成功");
//            break;
//        case None: // This state is the default state
//        case Cleared: // This state is when you disconnected
//            mLed.setImageResource(R.drawable.led_disconnected);
//            break;
//        case Failed: // This one means an error happened, for example a bad password
//            mLed.setImageResource(R.drawable.led_error);
//            System.out.println("链接失败:" + msg);
//            break;
//        case Progress: // Connection is in progress, next state will be either Ok or Failed
//            mLed.setImageResource(R.drawable.led_inprogress);
//            break;
//    }
//    Core core, ProxyConfig cfg, RegistrationState state, String message
    void connectSuccess(Core core, ProxyConfig cfg);// This state means you are connected, to can make and receive calls & messages

    void connectCleared(Core core, ProxyConfig cfg);// This state is when you disconnected

    void connectProgress(Core core, ProxyConfig cfg);// Connection is in progress, next state will be either Ok or Failed

    void connectNone(Core core, ProxyConfig cfg);// This state is the default state

    void connectedFails(Core core, ProxyConfig cfg, String message);// This one means an error happened, for example a bad password


}

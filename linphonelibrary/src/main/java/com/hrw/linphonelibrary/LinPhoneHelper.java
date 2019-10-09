package com.hrw.linphonelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.hrw.linphonelibrary.call.CallBean;
import com.hrw.linphonelibrary.call.CallHelper;
import com.hrw.linphonelibrary.call.CallOutGoingActivity;
import com.hrw.linphonelibrary.call.CallSetting;
import com.hrw.linphonelibrary.call.ParamTag;
import com.hrw.linphonelibrary.config.AndroidAudioManager;
import com.hrw.linphonelibrary.listener.OnLoginListener;
import com.hrw.linphonelibrary.service.LinPhoneService;

import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/12 9:18
 * @desc:
 */
public class LinPhoneHelper {
    private List<CallBean> mCallBeans = new ArrayList<>();

    private AndroidAudioManager mAndroidAudioManager;
    private static LinPhoneHelper mLinPhoneHelper;
    private AccountCreator mAccountCreator;
    private Core mCore;
    private Handler mHandler;
    private CallHelper mCallHelper;
    private CallSetting mCallSetting;

    private CoreListenerStub mCoreListener;
    private OnLoginListener mOnLoginListener;

    public final static int TYPE_VOICE = 1;
    public final static int TYPE_VIDEO = 2;

    private LinPhoneHelper(final Context context) {
        mHandler = new Handler();
        mAndroidAudioManager = new AndroidAudioManager(context);
        if (!LinPhoneService.isReady()) {
            context.startService(new Intent(context, LinPhoneService.class));
            // And wait for it to be ready, so we can safely use it afterwards
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!LinPhoneService.isReady()) {
                        try {
                            sleep(30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("waiting thread sleep() has been interrupted");
                        }
                    }
                    // As we're in a thread, we can't do UI stuff in it, must post a runnable in UI thread
                    mHandler.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    dealAfterCall();
                                }
                            });
                }
            }).start();
        }

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {
                if (mOnLoginListener != null) {
                    switch (state) {
                        case Ok: // This state means you are connected, to can make and receive calls & messages
                            mOnLoginListener.connectSuccess(core, cfg);
                            break;
                        case None: // This state is the default state
                        case Cleared: // This state is when you disconnected
                            mOnLoginListener.connectNone(core, cfg);
                            mOnLoginListener.connectCleared(core, cfg);
                            break;
                        case Failed: // This one means an error happened, for example a bad password
                            mOnLoginListener.connectedFails(core, cfg, message);
                            break;
                        case Progress: // Connection is in progress, next state will be either Ok or Failed
                            mOnLoginListener.connectProgress(core, cfg);
                            break;
                    }
                }
            }
        };

    }

    //处理成功获取Core后数据
    private void dealAfterCall() {
        mAccountCreator = LinPhoneService.getCore().createAccountCreator(null);
        mCore = LinPhoneService.getCore();
        mCore.addListener(mCoreListener);

        mCallHelper = new CallHelper();
        mCallSetting = new CallSetting(mCore);

        mCallSetting.enableVideo(true);
        mCallSetting.setAutomaticallyAcceptVideoRequests(true);
    }

    public void setCallList(List<CallBean> mCallBeans) {
        this.mCallBeans = mCallBeans;
    }

    public String getCallNameByNumber(String callNumber) {
        String callName = null;
        for (CallBean callBean : mCallBeans) {
            if (callBean.getCallNumber().equals(callNumber)) {
                callName = callBean.getCallName();
                break;
            }
        }
        return callName;
    }

    public CallHelper getCallHelper() {
        return mCallHelper;
    }

    public CallSetting getCallSetting() {
        return mCallSetting;
    }

    /**
     * 初始化工具，用于启动服务
     *
     * @param context
     */
    public static void init(Context context) {
        if (mLinPhoneHelper == null) {
            synchronized (LinPhoneHelper.class) {
                if (mLinPhoneHelper == null) {
                    mLinPhoneHelper = new LinPhoneHelper(context);
                }
            }
        }
    }

    public static LinPhoneHelper getInstance() {
        if (mLinPhoneHelper == null)
            throw new NullPointerException("before use the method getInstance must to init in application");
        return mLinPhoneHelper;
    }

    public void addLoginListener(OnLoginListener onLoginListener) {
        mOnLoginListener = onLoginListener;
    }

    public void removeLoginListener() {
        mCore.removeListener(mCoreListener);
    }


    /**
     * 发起语音通话
     */
    public void startVoiceCall(Context context, String callNumber, String callName) {
        Intent intent = new Intent(context, CallOutGoingActivity.class);
        intent.putExtra(ParamTag.CALL_NUMBER, callNumber);
        intent.putExtra(ParamTag.CALL_TYPE, TYPE_VOICE);
        intent.putExtra(ParamTag.CALL_NAME, callName);
        context.startActivity(intent);
    }

    /**
     * 发出视屏通话
     *
     * @param context
     * @param callNumber
     */
    public void startVideoCall(Context context, String callNumber, String callName) {
        Intent intent = new Intent(context, CallOutGoingActivity.class);
        intent.putExtra(ParamTag.CALL_NUMBER, callNumber);
        intent.putExtra(ParamTag.CALL_TYPE, TYPE_VIDEO);
        intent.putExtra(ParamTag.CALL_NAME, callName);
        context.startActivity(intent);
    }

    public AndroidAudioManager getAndroidAudioManager() {
        return mAndroidAudioManager;
    }

    public Core getCore() {
        return mCore;
    }

    /**
     *
     */
    public void login(String name, String password, String address) {
        mCore.clearProxyConfig();
        mCore.clearAllAuthInfo();

        mAccountCreator.setUsername(name);
        mAccountCreator.setDomain(address);
        mAccountCreator.setPassword(password);
        mAccountCreator.setTransport(TransportType.Tcp);
        // This will automatically create the proxy config and auth info and add them to the Core
        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        // Make sure the newly created one is the default
        mCore.setDefaultProxyConfig(cfg);
    }

    public void loginOut() {
        mCore.clearProxyConfig();
        mCore.clearAllAuthInfo();
    }

}

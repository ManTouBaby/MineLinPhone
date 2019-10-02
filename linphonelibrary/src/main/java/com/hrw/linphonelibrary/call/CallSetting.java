package com.hrw.linphonelibrary.call;

import org.linphone.core.Core;
import org.linphone.core.VideoActivationPolicy;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/27 9:03
 * @desc:
 */
public class CallSetting {
    Core mCore;

    public CallSetting(Core core) {
        this.mCore = core;
    }

    /**
     * 设置启动视屏
     *
     * @param enable
     */
    public void enableVideo(boolean enable) {
        if (mCore == null) return;
        mCore.enableVideoCapture(enable);
        mCore.enableVideoDisplay(enable);
    }

    /**
     * 设置默认发出视频通话
     *
     * @param initiate
     */
    public void setInitiateVideoCall(boolean initiate) {
        if (mCore == null) return;
        VideoActivationPolicy vap = mCore.getVideoActivationPolicy();
        vap.setAutomaticallyInitiate(initiate);
        mCore.setVideoActivationPolicy(vap);
    }

    /**
     * 设置接受视频通话请求
     *
     * @param accept
     */
    public void setAutomaticallyAcceptVideoRequests(boolean accept) {
        if (mCore == null) return;
        VideoActivationPolicy vap = mCore.getVideoActivationPolicy();
        vap.setAutomaticallyAccept(accept);
        mCore.setVideoActivationPolicy(vap);
    }

    public boolean shouldInitiateVideoCall() {
        if (mCore == null) return false;
        return mCore.getVideoActivationPolicy().getAutomaticallyInitiate();
    }

    public boolean shouldAutomaticallyAcceptVideoRequests() {
        if (mCore == null) return false;
        VideoActivationPolicy vap = mCore.getVideoActivationPolicy();
        return vap.getAutomaticallyAccept();
    }
}

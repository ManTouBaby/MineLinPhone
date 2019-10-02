package com.hrw.linphonelibrary.call;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hrw.linphonelibrary.LinPhoneHelper;
import com.hrw.linphonelibrary.config.AndroidAudioManager;

import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/16 14:35
 * @desc:接受通话界面
 */
public class CallIncomingActivity extends CallActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidAudioManager audioManager = LinPhoneHelper.getInstance().getAndroidAudioManager();
        audioManager.routeAudioToSpeaker();
        tvGreaterVoice.setSelected(true);
        tvAcceptCall.setVisibility(View.VISIBLE);
        mCallHelper.addListener(new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core lc, Call call, Call.State state, String message) {
                if (state == Call.State.Connected) {//电话接通
                    tvAcceptCall.setVisibility(View.GONE);
                }
            }
        });
    }
}

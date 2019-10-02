package com.hrw.linphonelibrary.call;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hrw.linphonelibrary.LinPhoneHelper;
import com.hrw.linphonelibrary.config.AndroidAudioManager;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/12 14:33
 * @desc:拨出通话界面
 */
public class CallOutGoingActivity extends CallActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidAudioManager audioManager = LinPhoneHelper.getInstance().getAndroidAudioManager();
        audioManager.routeAudioToEarPiece();
        tvGreaterVoice.setSelected(false);
        tvAcceptCall.setVisibility(View.GONE);
        int callType = getIntent().getIntExtra(ParamTag.CALL_TYPE, LinPhoneHelper.TYPE_VOICE);
        if (callType == LinPhoneHelper.TYPE_VOICE) {
            mCallHelper.startVoiceCall(mCallNumber, mCallName);
        } else if (callType == LinPhoneHelper.TYPE_VIDEO) {
            mCallHelper.startVideoCall(mCallNumber, mCallName);
        }
    }

}

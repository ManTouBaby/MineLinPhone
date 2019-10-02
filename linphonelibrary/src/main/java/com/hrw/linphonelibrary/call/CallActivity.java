package com.hrw.linphonelibrary.call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrw.linphonelibrary.LinPhoneHelper;
import com.hrw.linphonelibrary.R;
import com.hrw.linphonelibrary.utils.ActivityUtil;
import com.hrw.linphonelibrary.widget.TimeTextView;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.VideoDefinition;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/19 9:28
 * @desc:
 */
public class CallActivity extends Activity implements OnClickListener {
    protected CallHelper mCallHelper;
    protected Core mCore;
    protected Call mCall;

    ImageView ivSwitchCamera;

    protected TextureView ttvMyVideo;
    protected TextureView ttvOtherVideo;

    protected LinearLayout tvCancelCall;//挂断通话
    protected LinearLayout tvAcceptCall;//接受通话
    protected ImageView ivAcceptCall;//接听电话图标

    protected TextView tvSwitchVoiceVideo;
    protected TextView tvMute;//静音
    protected TextView tvGreaterVoice;//扩音

    protected TimeTextView ttvComputerTime;
    protected TextView tvWaitConnect;
    protected TextView tvCallNumber;
    protected TextView tvCallName;

    protected String mCallName;
    protected String mCallNumber;
    protected CoreListenerStub mListenerStub = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
            Address address = call.getRemoteAddress();
            Log.d("MineCall", message + "--" + state.toInt() + "--" + core.videoEnabled() + "--" + address.getDisplayName() + "--" + address.getUsername());
            if (state == Call.State.IncomingReceived) {
                tvCallNumber.setText(core.getCurrentCallRemoteAddress().asString());
            } else if (state == Call.State.Connected) {//电话接通
                ttvComputerTime.setVisibility(View.VISIBLE);
                tvWaitConnect.setVisibility(View.GONE);
            } else if (state == Call.State.StreamsRunning) {//正在通话中
                ttvComputerTime.startTime();
                tvSwitchVoiceVideo.setSelected(mCallHelper.videoEnabled());
                if (mCallHelper.videoEnabled()) {
                    ttvMyVideo.setVisibility(View.VISIBLE);
                    ttvOtherVideo.setVisibility(View.VISIBLE);
                    ivSwitchCamera.setVisibility(View.VISIBLE);
                } else {
                    ttvMyVideo.setVisibility(View.GONE);
                    ttvOtherVideo.setVisibility(View.GONE);
                    ivSwitchCamera.setVisibility(View.GONE);
                }
            } else if (state == Call.State.End) {
                System.out.println("通话结束");
                finish();
            }
        }
    };


    private void initView() {
        ivSwitchCamera = (ImageView) findViewById(R.id.iv_switch_camera);

        ttvMyVideo = (TextureView) findViewById(R.id.ttv_my_video);
        ttvOtherVideo = (TextureView) findViewById(R.id.ttv_other_video);

        tvCancelCall = (LinearLayout) findViewById(R.id.bt_cancel_call);
        tvAcceptCall = (LinearLayout) findViewById(R.id.bt_accept_call);
        ivAcceptCall = (ImageView) findViewById(R.id.iv_accept_call);

        tvSwitchVoiceVideo = (TextView) findViewById(R.id.tv_switch_voice_video);
        tvMute = (TextView) findViewById(R.id.tv_mute);
        tvGreaterVoice = (TextView) findViewById(R.id.tv_greater_voice);

        ttvComputerTime = (TimeTextView) findViewById(R.id.ttv_computer_time);
        tvWaitConnect = (TextView) findViewById(R.id.tv_wait_accept);
        tvCallNumber = (TextView) findViewById(R.id.tv_call_number);
        tvCallName = (TextView) findViewById(R.id.tv_call_name);

        ivSwitchCamera.setOnClickListener(this);

        tvCancelCall.setOnClickListener(this);
        tvAcceptCall.setOnClickListener(this);

        tvSwitchVoiceVideo.setOnClickListener(this);
        tvMute.setOnClickListener(this);
        tvGreaterVoice.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCore = LinPhoneHelper.getInstance().getCore();
        mCallHelper = LinPhoneHelper.getInstance().getCallHelper();

        ActivityUtil.setStatusBarColor(this);
        setContentView(R.layout.activity_call);
        initView();

        Intent intent = getIntent();
        mCallName = intent.getStringExtra(ParamTag.CALL_NAME);
        mCallNumber = intent.getStringExtra(ParamTag.CALL_NUMBER);
        tvCallName.setText(mCallName);
        tvCallNumber.setText(mCallNumber);

        mCore.setNativeVideoWindowId(ttvOtherVideo);
        mCore.setNativePreviewWindowId(ttvMyVideo);
        mCallHelper.addListener(mListenerStub);

        lookupCurrentCall();
        if (LinPhoneHelper.getInstance() != null
                && mCall != null
                && mCall.getRemoteParams() != null
                && LinPhoneHelper.getInstance().getCallSetting().shouldAutomaticallyAcceptVideoRequests()
                && mCall.getRemoteParams().videoEnabled()) {
            ivAcceptCall.setImageResource(R.drawable.call_video_start);
        }
    }

    private void lookupCurrentCall() {
        if (LinPhoneHelper.getInstance().getCore() != null) {
            for (Call call : LinPhoneHelper.getInstance().getCore().getCalls()) {
                if (Call.State.IncomingReceived == call.getState() || Call.State.IncomingEarlyMedia == call.getState()) {
                    mCall = call;
                    break;
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        resizePreview();
    }

    @Override
    protected void onDestroy() {
        Call call = LinPhoneHelper.getInstance().getCore().getCurrentCall();
        if (call != null) call.terminate();
        mCallHelper.removeListener(mListenerStub);
        super.onDestroy();
    }

    private void resizePreview() {
        Core core = LinPhoneHelper.getInstance().getCore();
        if (core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (call == null) {
                call = core.getCalls()[0];
            }
            if (call == null) return;

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            int maxHeight = screenHeight / 4; // Let's take at most 1/4 of the screen for the camera preview

            VideoDefinition videoSize = call.getCurrentParams().getSentVideoDefinition(); // It already takes care of rotation
            if (videoSize.getWidth() == 0 || videoSize.getHeight() == 0) {
                org.linphone.core.tools.Log.w("[Video] Couldn't get sent video definition, using default video definition");
                videoSize = core.getPreferredVideoDefinition();
            }
            int width = videoSize.getWidth();
            int height = videoSize.getHeight();

            org.linphone.core.tools.Log.d("[Video] Video height is " + height + ", width is " + width);
            width = width * maxHeight / height;
            height = maxHeight;

            if (ttvMyVideo == null) {
                org.linphone.core.tools.Log.e("[Video] mCaptureView is null !");
                return;
            }

            RelativeLayout.LayoutParams newLp = new RelativeLayout.LayoutParams(width, height);
            newLp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1); // Clears the rule, as there is no removeRule until API 17.
            newLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            ttvMyVideo.setLayoutParams(newLp);
            org.linphone.core.tools.Log.d("[Video] Video preview size set to " + width + "x" + height);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_cancel_call) {
            System.out.println("取消通话");
            mCallHelper.cancelCall();
        } else if (i == R.id.bt_accept_call) {
            System.out.println("接受通话");
            mCallHelper.acceptCall(this, mCore.getCurrentCall());
        } else if (i == R.id.tv_switch_voice_video) {
            System.out.println("转换视频");
            mCallHelper.toggleVideo();
        } else if (i == R.id.tv_mute) {
            System.out.println("静音操作");
            mCallHelper.toggleMic();
            tvMute.setSelected(!mCallHelper.micEnabled());
        } else if (i == R.id.tv_greater_voice) {
            System.out.println("扩音操作:" + mCallHelper.isSpeakerphoneOn());
            mCallHelper.toggleSpeaker();
            tvGreaterVoice.setSelected(mCallHelper.isSpeakerphoneOn());
        } else if (i == R.id.iv_switch_camera) {
            System.out.println("前后摄像头切换");
            mCallHelper.switchCamera();
        }
    }
}

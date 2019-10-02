package com.hrw.minelinphone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hrw.linphonelibrary.LinPhoneHelper;
import com.hrw.linphonelibrary.listener.OnLoginListener;

import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;

public class MainActivity extends AppCompatActivity {
    EditText mUser;
    EditText mPassWord;
    EditText mAddress;
    Button btLogin, btOutGo, btVideoOutGo, btOutGo1, btVideoOutGo1;

    ImageView mLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = (EditText) findViewById(R.id.et_user_name);
        mPassWord = (EditText) findViewById(R.id.et_user_pass_word);
        mAddress = (EditText) findViewById(R.id.et_user_address);
        btLogin = (Button) findViewById(R.id.bt_login);
        mLed = (ImageView) findViewById(R.id.iv_login_state);
        btOutGo = (Button) findViewById(R.id.bt_out_go);
        btVideoOutGo = (Button) findViewById(R.id.bt_video_out_go);

        btOutGo1 = (Button) findViewById(R.id.bt_out_go1);
        btVideoOutGo1 = (Button) findViewById(R.id.bt_video_out_go1);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinPhoneHelper.getInstance().login(mUser.getText().toString(), mPassWord.getText().toString(), mAddress.getText().toString());
            }
        });
        btVideoOutGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinPhoneHelper.getInstance().startVideoCall(MainActivity.this, "38110001","人物名称1");
            }
        });
        btOutGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinPhoneHelper.getInstance().startVoiceCall(MainActivity.this, "38110001","人物名称1");
            }
        });

        btVideoOutGo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinPhoneHelper.getInstance().startVideoCall(MainActivity.this, "38110002","人物名称2");
            }
        });
        btOutGo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinPhoneHelper.getInstance().startVoiceCall(MainActivity.this, "38110002","人物名称2");
            }
        });
        LinPhoneHelper.getInstance().addLoginListener(new OnLoginListener() {
            @Override
            public void connectSuccess(Core core, ProxyConfig cfg) {
                mLed.setImageResource(R.drawable.led_connected);
                System.out.println("链接成功:" + cfg.getIdentityAddress().asStringUriOnly());
            }

            @Override
            public void connectCleared(Core core, ProxyConfig cfg) {
                mLed.setImageResource(R.drawable.led_disconnected);
            }

            @Override
            public void connectNone(Core core, ProxyConfig cfg) {
                mLed.setImageResource(R.drawable.led_disconnected);
            }

            @Override
            public void connectProgress(Core core, ProxyConfig cfg) {
                mLed.setImageResource(R.drawable.led_inprogress);
            }

            @Override
            public void connectedFails(Core core, ProxyConfig cfg, String message) {
                mLed.setImageResource(R.drawable.led_error);
                System.out.println("链接失败:" + cfg.getIdentityAddress().asStringUriOnly() + "  " + message);
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LinPhoneHelper.getInstance().loginOut();
    }
}

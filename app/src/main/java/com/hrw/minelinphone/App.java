package com.hrw.minelinphone;

import android.app.Application;

import com.hrw.linphonelibrary.LinPhoneHelper;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/17 9:19
 * @desc:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LinPhoneHelper.init(this);
//        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//        } else {
//            EasyPermissions.requestPermissions(this, "获取文件必须权限", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
    }


}

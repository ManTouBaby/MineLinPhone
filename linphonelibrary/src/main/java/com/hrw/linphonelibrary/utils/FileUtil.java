package com.hrw.linphonelibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.hrw.linphonelibrary.R;

import org.linphone.core.Address;
import org.linphone.core.tools.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/09/18 10:39
 * @desc:
 */
public class FileUtil {
    @SuppressLint("SimpleDateFormat")
    public static String getCallRecordingFilename(Context context, Address address) {
        String fileName = getRecordingsDirectory(context) + "/";

        String name = address.getDisplayName() == null ? address.getUsername() : address.getDisplayName();
        fileName += name + "_";

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        fileName += format.format(new Date()) + ".mkv";

        return fileName;
    }

    public static String getRecordingsDirectory(Context mContext) {
        String recordingsDir = Environment.getExternalStorageDirectory() + "/" + mContext.getString(R.string.app_name) + "/recordings";
        File file = new File(recordingsDir);
        if (!file.isDirectory() || !file.exists()) {
            Log.w("[File Utils] Directory " + file + " doesn't seem to exists yet, let's create it");
            boolean result = file.mkdirs();
            if (!result) {
                Log.e("[File Utils] Couldn't create recordings directory " + file.getAbsolutePath() + ", using external storage dir instead");
                return Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }
        return recordingsDir;
    }
}

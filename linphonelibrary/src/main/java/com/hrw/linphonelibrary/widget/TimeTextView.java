package com.hrw.linphonelibrary.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * @author:Administrator
 * @date:2018/03/14 下午 1:42
 * @desc:
 */

public class TimeTextView extends AppCompatTextView {
    private long mTime;
    boolean isClose = true;
    boolean isOpenThread = false;
    Handler handler = new Handler();

    public TimeTextView(Context context) {
        this(context, null);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void startTime() {
        if (isOpenThread) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isClose) {
                    isOpenThread = true;
                    try {
                        Thread.sleep(1000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setText(computerTime());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();
    }

    private String computerTime() {
        String timeString;
        mTime += 1000;

        long secondCount = mTime / 1000;
        if (secondCount <= 59) {
            String second = secondCount <= 9 ? "0" + secondCount : "" + secondCount;
            timeString = "00:" + second;
        } else {
            long minute = secondCount / 60;
            long second = secondCount % 60;
            String secondString = second <= 9 ? "0" + second : "" + second;
            String minuteString = minute <= 9 ? "0" + minute : "" + minute;
            timeString = minuteString + ":" + secondString;
        }
        return timeString;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        System.out.println("View 被关闭了");
        isClose = false;
        isOpenThread = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        System.out.println("View 被开启了");
        isClose = true;
    }
}

package com.hrw.linphonelibrary.config;

/*
AndroidAudioManager.java
Copyright (C) 2019 Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import static android.media.AudioManager.STREAM_VOICE_CALL;

public class AndroidAudioManager {
    private static final int LINPHONE_VOLUME_STREAM = STREAM_VOICE_CALL;

    private Context mContext;
    private AudioManager mAudioManager;
    private SharedPreferences mSharedPreferences;


    public AndroidAudioManager(Context context) {
        mContext = context;
        mAudioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        mSharedPreferences = mContext.getSharedPreferences("AudioManager_Speaker", Context.MODE_PRIVATE);
    }


    /* Audio routing */

    public void routeAudioToEarPiece() {
        mSharedPreferences.edit().putBoolean("IS_SPEAKER_ON", false).apply();
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.setSpeakerphoneOn(false);
    }

    public void routeAudioToSpeaker() {
        mSharedPreferences.edit().putBoolean("IS_SPEAKER_ON", true).apply();
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(true);
    }

    public boolean isAudioRoutedToSpeaker() {
        return mSharedPreferences.getBoolean("IS_SPEAKER_ON", false);
    }



}

package com.jiajun.dan.lrcmusciplayer.musicplayer.eventbus;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Created by dan on 2017/7/28.
 */

public class MediaPlayerCreatedEvent {

    public MediaPlayer mMediaPlayer;

    public MediaPlayerCreatedEvent(@NonNull MediaPlayer mediaPlayer) {
        checkNotNull(mediaPlayer);
        mMediaPlayer = mediaPlayer;
    }
}

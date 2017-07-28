package com.jiajun.dan.lrcmusciplayer.musicplayer.mainactivity;

import android.content.Intent;
import android.graphics.Bitmap;


import com.jiajun.dan.lrcmusciplayer.musicplayer.BasePresenter;
import com.jiajun.dan.lrcmusciplayer.musicplayer.BaseView;
import com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata.Song;

import java.io.File;


/**
 *
 * Created by dan on 2017/7/28.
 */

interface MainContract {
    interface View extends BaseView<Presenter> {

        void updateTitle(String title);

        void updateArtist(String artist);

        void setEndTime(String time);

        void resetSeekBar(int max);

        void setListener();

        void initLrcView(File lrcFile);

        void updateLrcView(int progress);

        void updateCoverGauss(Bitmap bitmap);

        void updateCover(Bitmap bitmap);

        void updateCoverMirror(Bitmap bitmap);

        /**
         * 设置 播放-暂停 按钮图片
         *
         * @param isPlaying -true: play图片 -false: pause图片
         */
        void updatePlayButton(boolean isPlaying);

        void updateSeekBar(int progress);

    }

    interface Presenter extends BasePresenter {

        void processIntent(Intent intent);

        void pause();

        void play();

        void playMusic(Song song);

        void onBtnPlayPausePressed();

        void onProgressChanged(int progress);

        void destroy();

    }
}

package com.jiajun.dan.lrcmusciplayer.musicplayer.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;


import com.jiajun.dan.lrcmusciplayer.R;
import com.jiajun.dan.lrcmusciplayer.musicplayer.MyApplication;
import com.jiajun.dan.lrcmusciplayer.musicplayer.entities.MusicPlayerCompleteEvent;
import com.jiajun.dan.lrcmusciplayer.musicplayer.eventbus.MediaPlayerCreatedEvent;
import com.jiajun.dan.lrcmusciplayer.musicplayer.eventbus.PlayServiceCreatedEvent;
import com.jiajun.dan.lrcmusciplayer.musicplayer.eventbus.UpdateUiEvent;
import com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata.PlayList;
import com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata.Song;
import com.jiajun.dan.lrcmusciplayer.musicplayer.util.ImageUtils;
import com.jiajun.dan.lrcmusciplayer.musicplayer.util.TextUtils;
import com.jiajun.dan.lrcmusciplayer.musicplayer.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;



import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Created by dan on 2017/7/28.
 */

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int MSG_SEEK_BAR_REFRESH = 0;
    private static final int MSG_MUSIC_LRC_REFRESH = 1;

    private MainContract.View mMainView;

    private PlayList mPlayList;

    private MediaPlayer mMediaPlayer;

    private Context mContext;

    public MainPresenter(@NonNull MainContract.View mainView, Context context,Song song) {
        mMainView = checkNotNull(mainView, "mainView cannot be null");
        mContext = context;
        mainView.setPresenter(this);
        mPlayList = PlayList.getmInstance();
//        String path = "mnt/shared/Other/张敬轩-不吐不快.mp3";
//        Song song = new Song(path);
        mPlayList.addSong(song);
        EventBus.getDefault().register(this);
    }

    /**
     * 播放歌曲
     * @param context
     * @param url 歌曲链接
     */
    public MainPresenter(Context context,String url) {
        mContext = context;
        mPlayList = PlayList.getmInstance();
        mPlayList.setIsThirdCall(true);
        Song song = new Song(url);
        mPlayList.setThirdSong(song);
        EventBus.getDefault().register(this);
        playMusic(mPlayList.getCurrSong());
    }

    /**
     * 列表播放歌曲
     *
     * @param context
     * @param position 列表位置
     */
    public MainPresenter(Context context, int position) {
        mContext = context;
        mPlayList = PlayList.getmInstance();
        if (position < mPlayList.getSongList().size() - 1)
            mPlayList.setmCurrIndex(position);
        EventBus.getDefault().register(this);
        playMusic(mPlayList.getCurrSong());
    }

    /**
     * 切换歌曲
     * @param uri
     */
    public void switchSong(String uri){
        Song song = new Song(uri);
        mPlayList.setIsThirdCall(true);
        mPlayList.setThirdSong(song);
        playMusic(mPlayList.getCurrSong());
    }

    @Override
    public void start() {

    }

    @Override
    public void processIntent(Intent intent) {
        Log.d(TAG, "intent.getAction = " + intent.getAction());
        if (intent.getAction()!=null&&intent.getAction().equals(Intent.ACTION_MAIN)) {
            return;
        }
        Uri uri = intent.getData();
        Song song = new Song(uri);
        mPlayList.setIsThirdCall(true);
        mPlayList.setThirdSong(song);
        playMusic(mPlayList.getCurrSong());
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mPlayList.pause();
        }
    }

    @Override
    public void play() {
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        if (mMediaPlayer != null) {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
            mPlayList.play();
            if(mMainView!=null)
            mMainView.updatePlayButton(false);
        }
    }

    @Override
    public void destroy() {
        mMainView = null;
        handler.removeMessages(MSG_SEEK_BAR_REFRESH);
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SEEK_BAR_REFRESH:
                    if(mMainView!=null)
                    mMainView.updateSeekBar(mMediaPlayer.getCurrentPosition());
                    sendEmptyMessageDelayed(MSG_SEEK_BAR_REFRESH, MyApplication.getContext().getResources().getInteger(R.integer.seek_bar_refresh_interval));
                    break;
                case MSG_MUSIC_LRC_REFRESH:
                    if (mMediaPlayer != null) {
                        if(mMainView!=null)
                        mMainView.updateLrcView(mMediaPlayer.getCurrentPosition());
                    }
                    sendEmptyMessageDelayed(MSG_MUSIC_LRC_REFRESH, 120);
                    break;
            }
        }
    };

    @Override
    public void playMusic(Song song) {
        if (mMediaPlayer == null) {
            return;
        }
        mPlayList.play();
        if(mMainView==null){
            return;
        }
        mMainView.resetSeekBar(song.getDuration());
        mMainView.updateTitle(song.getmTitle());
        mMainView.updateArtist(song.getArtist());
        mMainView.setEndTime(TextUtils.duration2String(song.getDuration()));
        mMainView.updatePlayButton(false);
        handler.sendEmptyMessage(MSG_SEEK_BAR_REFRESH);
    }

    /**
     * 是否在播放
     * @return
     */
    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void onBtnPlayPausePressed() {
        if (mMediaPlayer == null || mPlayList.getCurrSong() == null) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.message_music_is_null));
            return;
        }


        boolean isPlaying = mMediaPlayer.isPlaying();
        if (isPlaying) {
            // avoid ui stuck when quick switch play and pause
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);

            //pause
            mPlayList.pause();
        } else {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);

            //play
            mPlayList.play();
        }
        if(mMainView!=null)
        mMainView.updatePlayButton(isPlaying);
    }

    @Override
    public void onProgressChanged(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
            mMainView.updateSeekBar(progress);
        }
    }

    @Subscribe
    public void onPlayServiceCreated(PlayServiceCreatedEvent event) {
        playMusic(mPlayList.getCurrSong());
    }

    @Subscribe
    public void onMediaPlayerCreated(@NonNull MediaPlayerCreatedEvent event) {
        checkNotNull(event);
        mMediaPlayer = event.mMediaPlayer;
    }

    @Subscribe
    public void onUpdateUI(UpdateUiEvent event) {
        Song song = mPlayList.getCurrSong();

        if (song == null) {
            return;
        }
        if(mMainView==null){
            return;
        }
        Bitmap bitmap = song.getCover();
        if (bitmap != null) {
            mMainView.updateCoverGauss(ImageUtils.fastblur(bitmap, 0.1f, 10));
            mMainView.updateCover(bitmap);
            mMainView.updateCoverMirror(ImageUtils.createReflectionBitmapForSingle(bitmap,
                    (int) MyApplication.getContext().getResources().getDimension(R.dimen.cover_width_height),
                    (int) MyApplication.getContext().getResources().getDimension(R.dimen.cover_mirror_height)));
        } else {
            mMainView.updateCoverGauss(null);
            mMainView.updateCover(null);
            mMainView.updateCoverMirror(null);
        }

        if (song.getLrcPath() != null) {
            mMainView.initLrcView(new File(song.getLrcPath()));
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
        } else {
            mMainView.initLrcView(null);
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayingComelete(MusicPlayerCompleteEvent event) {
//        mPlayList.autoNext();
    }
}

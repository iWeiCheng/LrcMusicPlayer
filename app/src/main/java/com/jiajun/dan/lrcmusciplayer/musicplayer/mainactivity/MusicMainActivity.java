package com.jiajun.dan.lrcmusciplayer.musicplayer.mainactivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.jiajun.dan.lrcmusciplayer.R;
import com.jiajun.dan.lrcmusciplayer.musicplayer.MusicPlayService;
import com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata.AudioBean;
import com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata.Song;
import com.jiajun.dan.lrcmusciplayer.musicplayer.util.ActivityUtils;

/**
 *
 * Created by dan on 2017/7/28.
 */
public class MusicMainActivity extends FragmentActivity {

    private static final String TAG = "MusicMainActivity";

    private MainPresenter mPresenter;

    private Intent mPlayService;
    private AudioBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        bean = getIntent().getParcelableExtra("data");
        initialize();
    }



    protected void initialize() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.contentFrame);
        }
        mPlayService = new Intent(this, MusicPlayService.class);
        startService(mPlayService);
//        String path = "mnt/shared/Other/张敬轩-不吐不快.mp3";
        String path ="https://raw.githubusercontent.com/zhengken/LyricViewDemo/master/sample/%E5%BC%A0%E6%95%AC%E8%BD%A9-%E4%B8%8D%E5%90%90%E4%B8%8D%E5%BF%AB.mp3";
        Song song = new Song(path);
        song.setLocalSong(false);
        song.setmTitle("张敬轩-不吐不快");
        song.setmCover(BitmapFactory.decodeResource(getResources(),R.drawable.default_cover));
        song.setmDuration(283000);
        song.setmArtist("张敬轩");
        mPresenter = new MainPresenter(mainFragment, this,song);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
        mPresenter.processIntent(intent);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        stopService(mPlayService);
        mPresenter.destroy();
    }
}

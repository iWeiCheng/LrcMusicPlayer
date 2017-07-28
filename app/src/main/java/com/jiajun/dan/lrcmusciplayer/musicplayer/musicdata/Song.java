package com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;


import com.jiajun.dan.lrcmusciplayer.R;
import com.jiajun.dan.lrcmusciplayer.musicplayer.MyApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Created by dan on 2017/7/28.
 */

public class Song {


    public Song(@NonNull Uri songUri) {
        checkNotNull(songUri);

        try {
            String songPath = URLDecoder.decode(songUri.getPath().toString(), "UTF8");
            mSongPath = songPath;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Song(@NonNull String songPath) {
        mSongPath = checkNotNull(songPath);
    }

    private boolean isLocalSong = true ;

    private String mTitle;

    private String mArtist;

    private String mLrcPath;

    private String mSongPath;

    private int mDuration;

    private Bitmap mCover;

    private Bitmap mCoverMirror;

    private Bitmap mCoverGauss;

    public boolean isLocalSong() {
        return isLocalSong;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public void setmSongPath(String mSongPath) {
        this.mSongPath = mSongPath;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public Bitmap getmCover() {
        return mCover;
    }

    public void setmCover(Bitmap mCover) {
        this.mCover = mCover;
    }

    public void setLocalSong(boolean localSong) {
        isLocalSong = localSong;
    }

    public String getmSongPath() {
        return mSongPath;
    }

    private void setLrcPath() {
        checkNotNull(mSongPath);

        String lrcPath = mSongPath.substring(0, mSongPath.length() - 3) + "lrc";
        mLrcPath = lrcPath;
    }

    public String getLrcPath() {
        if (mLrcPath == null) {
            setLrcPath();
        }

        if (isLrcExist()) {
            return mLrcPath;
        }
        return null;
    }

    public boolean isLrcExist() {
        checkNotNull(mLrcPath);

        File lrcFile = new File(mLrcPath);
        if (lrcFile.exists()) {
            return true;
        }
        return false;
    }

    public void setmTitle() {
        if(isLocalSong) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mSongPath);
            mTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        }
    }

    public String getmTitle() {
        if (mTitle == null)
            setmTitle();
        return mTitle;
    }

    private void setArtist() {
        if(isLocalSong) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mSongPath);
            mArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        }
    }

    public String getArtist() {
        if (mArtist == null) {
            setArtist();
        }
        return mArtist;
    }

    public void setDuration() {
        if(isLocalSong) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mSongPath);
            mDuration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        }
    }

    public int getDuration() {
        if (mDuration == 0) {
            setDuration();
        }
        return mDuration;
    }

    public String getFormatDuration() {
        long time = getDuration();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        double minute = calendar.get(Calendar.MINUTE);
        double second = calendar.get(Calendar.SECOND);

        DecimalFormat format = new DecimalFormat("00");
        return format.format(minute) + ":" + format.format(second);
    }


    public void setCover() {
        if(isLocalSong) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mSongPath);
            byte[] bitmap = mmr.getEmbeddedPicture();
            if (bitmap != null) {
                mCover = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            } else {
                mCover = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),
                        R.drawable.default_cover);
            }
        }
    }

    public Bitmap getCover() {
        if (mCover == null) {
            setCover();
        }
        return mCover;
    }

    public void setCoverMirror() {
    }

    public void setCoverGauss() {
    }
}

package com.jiajun.dan.lrcmusciplayer.musicplayer.musicdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by danjj on 2017/6/28.
 */
public class AudioBean implements Parcelable {
    private String audio_name;
    private String audio_url;
    private String audio_content;
    private boolean isPlaying;
    private boolean isPause;
    private boolean isFavor;
    private int audio_id;

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getAudio_content() {
        return audio_content;
    }

    public void setAudio_content(String audio_content) {
        this.audio_content = audio_content;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.audio_name);
        dest.writeString(this.audio_url);
        dest.writeString(this.audio_content);
        dest.writeInt(this.audio_id);
    }

    public AudioBean() {
    }

    protected AudioBean(Parcel in) {
        this.audio_name = in.readString();
        this.audio_url = in.readString();
        this.audio_content = in.readString();
        this.audio_id = in.readInt();
    }

    public static final Creator<AudioBean> CREATOR = new Creator<AudioBean>() {
        @Override
        public AudioBean createFromParcel(Parcel source) {
            return new AudioBean(source);
        }

        @Override
        public AudioBean[] newArray(int size) {
            return new AudioBean[size];
        }
    };
}

package com.jiajun.dan.lrcmusciplayer.musicplayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * Created by dan on 2017/7/28.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;
    // 单例模式中获取唯一的ExitApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    // 将Activity添加到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 当要退出Activity时，遍历所有Activity 并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 获取全局的context
     */
    public static Context getContext() {
        return mContext;
    }
}

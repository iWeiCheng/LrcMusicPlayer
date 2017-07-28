package com.jiajun.dan.lrcmusciplayer.musicplayer.util;

/**
 * Toast封装
 * Created by dan on 2017/7/28.
 */

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.jiajun.dan.lrcmusciplayer.R;
import com.jiajun.dan.lrcmusciplayer.musicplayer.MyApplication;


public class ToastUtils {

    // 短时间显示Toast信息
    public static void showShort(Context context, String info) {
        Toast toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, MyApplication.getContext().getResources().getInteger(R.integer.toast_margin_top_center_in_screen));
        toast.show();
    }

    // 长时间显示Toast信息
    public static void showLong(Context context, String info) {
        Toast toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, MyApplication.getContext().getResources().getInteger(R.integer.toast_margin_top_center_in_screen));
        toast.show();
    }

}

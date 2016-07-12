package com.example.ivy.retrofit2demo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Ivy on 2016/7/12.
 */
public class MyDownloadReceiver extends BroadcastReceiver {
    private String downloadPath = "";

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d("onReceive","receiver...:");
//        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setIcon(R.mipmap.ic_launcher).setTitle("提示：")
//                .setMessage("下载完毕，是否播放？").setNegativeButton("取消", null)
//                .setPositiveButton("确认", new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.show();
    }
}

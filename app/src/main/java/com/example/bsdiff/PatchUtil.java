package com.example.bsdiff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

public class PatchUtil {

    private static PatchUtil INSTANCE;
    private Handler handler;

    private PatchUtil() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static PatchUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (PatchUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PatchUtil();

                }
            }
        }
        return INSTANCE;
    }

    private void log(String content) {
        Log.i("Vam", content);
    }

    public void patch(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {

            File file = new File(Environment.getExternalStorageDirectory(), "VamTest");
            if (!file.exists()) {
                file.mkdirs();
            }

            File patchFile = new File(file, "patch.diff");
            String patchPath = patchFile.getAbsolutePath();
            log("patch exists: " + patchFile.exists());

            String oldApkPath = context.getApplicationInfo().sourceDir;
            File oldApk = new File(oldApkPath);
            log("oldApk exists: " + oldApk.exists());

            File newApk = new File(file, "new.apk");
            if (!newApk.exists()) {
                try {
                    newApk.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            log("newApk exists: " + newApk.exists());
            patchApk(oldApkPath, newApk.getAbsolutePath(), patchPath);

            handler.post(() -> {

                if (newApk==null){
                    log("出意外了，这个空了");
                    return;
                }
                if (!newApk.exists()) {
                    System.out.println("文件不存在");
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT > 23) {
                    Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".provider", newApk);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(newApk), "application/vnd.android.package-archive");
                }

                log("开始安装");
                context.startActivity(intent);

            });

        });
    }

    native void patchApk(String oldApk, String newAPk, String patch);

}

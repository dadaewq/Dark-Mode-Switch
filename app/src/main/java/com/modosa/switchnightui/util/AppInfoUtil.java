package com.modosa.switchnightui.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @author dadaewq
 */
public final class AppInfoUtil {

    public static String getAppVersion(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(pkgName, 0);
        } catch (Exception ignore) {
        }

        if (applicationInfo != null) {
            return getApkVersion(context, applicationInfo.sourceDir)[0];
        }
        return null;
    }

    public static String getAppVersions(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(pkgName, 0);
        } catch (Exception ignore) {
        }

        if (applicationInfo != null) {
            return getApkVersion(context, applicationInfo.sourceDir)[0] +
                    "_" +
                    getApkVersion(context, applicationInfo.sourceDir)[1];
        }
        return null;
    }

//    private static String getApkVersion(Context context, String apkPath) {
//        PackageManager pm = context.getPackageManager();
//        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
//
//        if (pkgInfo != null) {
//            pkgInfo.applicationInfo.sourceDir = apkPath;
//            pkgInfo.applicationInfo.publicSourceDir = apkPath;
//
//            return new StringBuilder().append(pkgInfo.versionName).append("_").append(Build.VERSION.SDK_INT < Build.VERSION_CODES.P ? Integer.toString(pkgInfo.versionCode) : Long.toString(pkgInfo.getLongVersionCode())).toString();
//        } else {
//            return null;
//        }
//    }

    private static String[] getApkVersion(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);

        if (pkgInfo != null) {
            pkgInfo.applicationInfo.sourceDir = apkPath;
            pkgInfo.applicationInfo.publicSourceDir = apkPath;

            return new String[]{pkgInfo.versionName,
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.P ? Integer.toString(pkgInfo.versionCode) : Long.toString(pkgInfo.getLongVersionCode())
            };
        } else {
            return null;
        }
    }


}

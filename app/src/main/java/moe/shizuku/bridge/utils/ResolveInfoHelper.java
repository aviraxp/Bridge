package moe.shizuku.bridge.utils;

import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collection;

import moe.shizuku.bridge.BridgeSettings;
import moe.shizuku.bridge.BuildConfig;

/**
 * Created by Rikka on 2017/4/6.
 */

public class ResolveInfoHelper {

    public static ArrayList<ResolveInfo> filter(Collection<ResolveInfo> resolveInfo, boolean editMode) {
        ArrayList<ResolveInfo> list = new ArrayList<>();
        for (ResolveInfo info : resolveInfo) {
            /*if ((info.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }*/
            if (info.activityInfo.packageName.equals(BuildConfig.APPLICATION_ID)
                    || info.activityInfo.packageName.startsWith("com.android.")
                    || info.activityInfo.packageName.startsWith("com.google.")
                    || info.activityInfo.packageName.equals("android")) {
                continue;
            }

            if (!editMode
                    && !BridgeSettings.isActivityForward(info.activityInfo.name)) {
                continue;
            }

            list.add(info);
        }
        return list;
    }
}

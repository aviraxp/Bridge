package moe.shizuku.bridge.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

import moe.shizuku.bridge.R;

/**
 * Created by Rikka on 2017/4/6.
 */

public class IntentUtils {

    public static boolean isValid(Context context, Intent intent) {
        return isValid(context, intent, 0);
    }

    public static boolean isValid(Context context, Intent intent, int minSize) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > minSize;
    }

    public static void startOtherActivity(Context context, Intent intent) {
        startOtherActivity(context, intent, context.getString(R.string.noApplications));
    }

    public static void startOtherActivity(Context context, Intent intent, String notFoundMessage) {
        if (isValid(context, intent)) {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } else {
            Toast.makeText(context, notFoundMessage, Toast.LENGTH_LONG).show();
        }
    }

    public static void startOtherActivityForResult(Activity activity, Intent intent, int requestCode) {
        startOtherActivityForResult(activity, intent, requestCode, activity.getString(R.string.noApplications));
    }

    public static void startOtherActivityForResult(Activity activity, Intent intent, int requestCode, String notFoundMessage) {
        if (isValid(activity, intent)) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(activity, notFoundMessage, Toast.LENGTH_LONG).show();
        }
    }
}

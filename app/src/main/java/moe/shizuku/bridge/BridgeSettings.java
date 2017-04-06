package moe.shizuku.bridge;

import java.util.HashSet;
import java.util.Set;

import moe.shizuku.bridge.utils.Settings;

/**
 * Created by Rikka on 2017/4/6.
 */

public class BridgeSettings {

    private static final String FORWARD_ACTIVITIES = "forward_activities";

    private static Set<String> sForwardActivities;

    private static Set<String> getForwardActivities() {
        if (sForwardActivities == null) {
            sForwardActivities = Settings.getStringSet(FORWARD_ACTIVITIES, new HashSet<String>());
        }
        return sForwardActivities;
    }

    public static boolean isActivityForward(String name) {
        return getForwardActivities().contains(name);
    }

    public static void setActivityForward(String name, boolean add) {
        if (add && !getForwardActivities().contains(name)) {
            getForwardActivities().add(name);
        } else {
            getForwardActivities().remove(name);
        }
        Settings.putStringSet(FORWARD_ACTIVITIES, getForwardActivities());
    }
}

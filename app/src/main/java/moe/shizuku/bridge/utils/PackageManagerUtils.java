package moe.shizuku.bridge.utils;

import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rikka on 2017/4/2.
 */

public class PackageManagerUtils {

    public static void setComponentState(PackageManager pm, String pkg, String cls, boolean enable) {
        setComponentState(pm, new ComponentName(pkg, cls), enable);
    }

    public static void setComponentState(PackageManager pm, ComponentName componentName, boolean enable) {
        final int oldState = pm.getComponentEnabledSetting(componentName);
        final int newState = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        if (newState != oldState) {
            final int flags = PackageManager.DONT_KILL_APP;
            pm.setComponentEnabledSetting(componentName, newState, flags);
        }
    }

    public static boolean isComponentEnabled(PackageManager pm, ComponentName componentName) {
        return isComponentEnabled(pm, componentName.getPackageName(), componentName.getClassName());
    }

    public static boolean isComponentEnabled(PackageManager pm, String pkg, String cls) {
        ComponentName componentName = new ComponentName(pkg, cls);
        int componentEnabledSetting = pm.getComponentEnabledSetting(componentName);

        switch (componentEnabledSetting) {
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                return false;
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                return true;
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
            default:
                // We need to get the application info to get the component's default state
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES
                            | PackageManager.GET_RECEIVERS
                            | PackageManager.GET_SERVICES
                            | PackageManager.GET_PROVIDERS
                            | PackageManager.GET_DISABLED_COMPONENTS);

                    List<ComponentInfo> components = new ArrayList<>();
                    if (packageInfo.activities != null) Collections.addAll(components, packageInfo.activities);
                    if (packageInfo.services != null) Collections.addAll(components, packageInfo.services);
                    if (packageInfo.providers != null) Collections.addAll(components, packageInfo.providers);

                    for (ComponentInfo componentInfo : components) {
                        if (componentInfo.name.equals(cls)) {
                            return componentInfo.enabled; //This is the default value (set in AndroidManifest.xml)
                            //return componentInfo.isEnabled(); //Whole package dependant
                        }
                    }

                    // the component is not declared in the AndroidManifest
                    return false;
                } catch (PackageManager.NameNotFoundException e) {
                    // the package isn't installed on the device
                    return false;
                }
        }
    }
}

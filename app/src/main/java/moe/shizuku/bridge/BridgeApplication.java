package moe.shizuku.bridge;

import android.app.Application;
import android.os.StrictMode;

import moe.shizuku.bridge.utils.Settings;

/**
 * Created by Rikka on 2017/3/26.
 */

public class BridgeApplication extends Application {

    // avoid FileUriExposedException
    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Settings.init(this);
    }
}

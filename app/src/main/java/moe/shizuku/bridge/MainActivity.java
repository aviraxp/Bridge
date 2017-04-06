package moe.shizuku.bridge;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Switch;

import moe.shizuku.bridge.utils.PackageManagerUtils;

public class MainActivity extends Activity {

    public static final ComponentName SAVE_ACTIVITY = ComponentName.createRelative(BuildConfig.APPLICATION_ID, ".SaveActivity");
    public static final ComponentName FORWARD_ACTIVITY = ComponentName.createRelative(BuildConfig.APPLICATION_ID, ".ForwardingActivity");

    private static final int REQUEST_PERMISSION = 10000;

    private Switch mSwitchSave;
    private Switch mSwitchForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // too bad
        if ("paid".equals(BuildConfig.FLAVOR)
                && !"com.android.vending".equals(getPackageManager().getInstallerPackageName(BuildConfig.APPLICATION_ID))) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PackageManagerUtils.setComponentState(getPackageManager(), SAVE_ACTIVITY, false);
        }

        mSwitchSave = (Switch) findViewById(R.id.switch_save);
        mSwitchSave.setChecked(PackageManagerUtils.isComponentEnabled(getPackageManager(), SAVE_ACTIVITY));
        mSwitchSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                    return;
                }
                PackageManagerUtils.setComponentState(getPackageManager(), SAVE_ACTIVITY, mSwitchSave.isChecked());
            }
        });

        mSwitchForward = (Switch) findViewById(R.id.switch_forward);
        mSwitchForward.setChecked(PackageManagerUtils.isComponentEnabled(getPackageManager(), FORWARD_ACTIVITY));
        mSwitchForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManagerUtils.setComponentState(getPackageManager(), FORWARD_ACTIVITY, mSwitchForward.isChecked());
            }
        });

        findViewById(R.id.select_forward_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooserActivity.start(v.getContext());
            }
        });

        UserManager userManager = (UserManager) getSystemService(USER_SERVICE);
        boolean usingWorkProfile = userManager.getUserProfiles().size() > 1;
        if (!usingWorkProfile) {
            findViewById(R.id.work_profile_desc).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PackageManagerUtils.setComponentState(getPackageManager(), SAVE_ACTIVITY, true);
                } else {
                    mSwitchSave.setChecked(false);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

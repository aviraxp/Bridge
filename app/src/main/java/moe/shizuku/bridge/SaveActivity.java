package moe.shizuku.bridge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import moe.shizuku.bridge.service.FileSaveService;

/**
 * Created by Rikka on 2017/3/26.
 */

public class SaveActivity extends Activity {

    private static final String TAG = "SaveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_SEND)) {
            Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

            if (uri != null) {
                FileSaveService.startSaveFile(this, uri, getIntent().getType(), isForwardActivity());
            }
            finish();
        }
    }

    protected boolean isForwardActivity() {
        return false;
    }
}

package moe.shizuku.bridge.service;

import android.Manifest;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import moe.shizuku.bridge.BuildConfig;
import moe.shizuku.bridge.ChooserActivity;
import moe.shizuku.bridge.R;
import moe.shizuku.bridge.utils.FileUtils;
import moe.shizuku.bridge.utils.FilenameResolver;
import moe.shizuku.bridge.utils.ResolveInfoHelper;

public class FileSaveService extends IntentService {

    private static final String TAG = "FileSaveService";

    private static final String ACTION_SAVE_FILE = BuildConfig.APPLICATION_ID + ".intent.action.SAVE_FILE";

    private static final String EXTRA_SHARE = BuildConfig.APPLICATION_ID + ".intent.extra.SHARE";

    public FileSaveService() {
        super("FileSaveService");
    }

    public static void startSaveFile(Context context, Uri uri, String type, boolean share) {
        Intent intent = new Intent(context, FileSaveService.class);
        intent.setAction(ACTION_SAVE_FILE);
        intent.setDataAndType(uri, type);
        intent.putExtra(EXTRA_SHARE, share);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleSaveFile(intent.getData(), intent.getType(), intent.getBooleanExtra(EXTRA_SHARE, false));
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleSaveFile(Uri uri, String type, boolean share) {
        // TODO: too bad
        FileUtils.clearCache(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!share && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "no write storage permission");

                onFailed(getString(R.string.fail_no_write_storage_permission));
                return;
            }
        }

        try {
            String filename = FilenameResolver.query(getContentResolver(), uri, Long.toString(System.currentTimeMillis()));
            InputStream is = getContentResolver().openInputStream(uri);

            if (is == null) {
                onFailed("InputStream is null");
                return;
            }

            File file;

            if (share) {
                file = FileUtils.getCacheFile(this, "files/" + filename);
            } else {
                // too bad
                if (uri.getPath().contains("ScreenshotProvider")) {
                    file = FileUtils.getExternalStoragePublicFile(Environment.DIRECTORY_PICTURES, "Screenshots", filename);
                } else {
                    file = FileUtils.getExternalStoragePublicFile(Environment.DIRECTORY_DOWNLOADS, "Bridge", filename);
                }
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(file);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            if (share) {
                uri = Uri.fromFile(file);
            } else {
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            }
            onSuccess(file, uri, type, share);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();

            onFailed(e.getMessage());
        }
    }

    private void onSuccess(final File file, Uri uri, String type, boolean share) {
        Log.d(TAG, "saved " + file.getAbsolutePath() + " " + uri);

        int start = Environment.getExternalStorageDirectory().getAbsolutePath().length();
        final String path = file.getAbsolutePath().substring(start);

        /*NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_stat)
                .setContentTitle(getString(R.string.save_notification))
                .setContentText(path)
                .build();

        notificationManager.notify(file.getAbsolutePath().hashCode(), notification);*/

        if (!share) {
            /*
            // require internet permission
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadManager.addCompletedDownload(file.getName(), "Save via Bridge", true, type, file.getPath(), file.length(), false);*/

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getString(R.string.saved, path), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(type);
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            ChooserActivity.start(this, uri, type, ResolveInfoHelper.filter(getPackageManager().queryIntentActivities(intent, 0), false));
        }
    }

    private void onFailed(final String message) {
        Log.d(TAG, "failed");

        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.save_failed, message), Toast.LENGTH_LONG).show();
            }
        });
    }
}

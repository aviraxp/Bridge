package moe.shizuku.bridge.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Rikka on 2017/3/26.
 */

public class FileUtils {

    public static File getExternalStoragePublicFile(String type, String path, String filename) {
        return new File(Environment.getExternalStoragePublicDirectory(type).getAbsolutePath() + "/" + path, filename);
    }

    public static File getCacheFile(Context context, String filename) {
        if (context.getExternalCacheDir() != null) {
            return new File(context.getExternalCacheDir(), filename);
        } else {
            return new File(context.getCacheDir(), filename);
        }
    }

    public static void clearCache(Context context) {
        if (context.getExternalCacheDir() != null) {
            delete(context.getExternalCacheDir());
        }

        delete(context.getCacheDir());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void delete(File file) {
        if (!file.isDirectory()) {
            file.delete();
            return;
        }

        File[] files = file.listFiles();
        if(files != null) {
            for (File f : files){
                file.delete();
            }
        }
    }
}

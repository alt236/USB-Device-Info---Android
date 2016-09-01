package aws.apps.usbDeviceEnumerator.util;

import android.util.Log;

import java.io.File;

public final class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean createDirectories(String dirs) {
        Log.d(TAG, "^ createDirectories - Attempting to create: " + dirs);
        try {

            if (new File(dirs).exists()) {
                Log.d(TAG, "^ createDirectories - Directory already exist:" + dirs);
                return true;
            }

            // create a File object for the parent directory
            File newDirectories = new File(dirs);
            // have the object build the directory structure, if needed.
            if (newDirectories.mkdirs()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {//Catch exception if any
            return false;
        }
    }


}

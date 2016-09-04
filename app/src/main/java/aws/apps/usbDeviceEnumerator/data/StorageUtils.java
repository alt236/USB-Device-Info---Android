package aws.apps.usbDeviceEnumerator.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.File;

/*package*/ class StorageUtils {

    public static File getExternalStorageLocation(final Context context) {
        final File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        final File dir;

        if (dirs == null || dirs.length == 0) {
            dir = null;
        } else {
            dir = dirs[0];
        }

        return dir;
    }
}

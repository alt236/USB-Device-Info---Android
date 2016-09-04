package aws.apps.usbDeviceEnumerator.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static boolean isOnline(final Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                Log.d(TAG, "^ isOnline()=true");
                return cm.getActiveNetworkInfo().isConnected();
            } else {
                Log.d(TAG, "^ isOnline()=false");
                return false;
            }

        } catch (Exception e) {
            Log.e(TAG, "^ isOnline()=false", e);
            return false;
        }
    }
}

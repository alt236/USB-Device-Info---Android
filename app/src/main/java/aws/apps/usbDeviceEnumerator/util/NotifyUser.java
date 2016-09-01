package aws.apps.usbDeviceEnumerator.util;

import android.content.Context;
import android.widget.Toast;

public class NotifyUser {

    public static void notify(final Context context, final CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void notify(final Context context, final int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}

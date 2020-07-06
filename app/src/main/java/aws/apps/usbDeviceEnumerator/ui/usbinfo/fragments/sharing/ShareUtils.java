package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import aws.apps.usbDeviceEnumerator.R;

public class ShareUtils {

    public static void share(final Activity activity,
                             final String subject,
                             final String text) {

        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        final Intent share = Intent.createChooser(intent, activity.getString(R.string.share_result_via));
        ActivityCompat.startActivity(activity, share, null);
    }
}

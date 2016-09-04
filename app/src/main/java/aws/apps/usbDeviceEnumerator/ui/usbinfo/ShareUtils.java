package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

/*package*/ class ShareUtils {
    private static final String TAG = ShareUtils.class.getSimpleName();

    public static void share(final Activity activity,
                             final String subject,
                             final String text) {

        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        final Intent share = Intent.createChooser(intent, activity.getString(R.string.share_result_via));
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static String tableToString(TableLayout t) {
        StringBuilder sb = new StringBuilder();
        if (t != null) {

            for (int i = 0; i <= t.getChildCount() - 1; i++) {
                final TableRow row = (TableRow) t.getChildAt(i);

                for (int j = 0; j <= row.getChildCount() - 1; j++) {
                    final View v = row.getChildAt(j);

                    try {
                        if (v instanceof TextView) {
                            final TextView textView = (TextView) v;
                            sb.append(textView.getText());

                            if (j == 0) {
                                sb.append(" ");
                            }
                        }
                    } catch (Exception e) {
                        sb.append(e.toString());
                        Log.e(TAG, "^ ERROR: tableToString: " + e.toString());
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}

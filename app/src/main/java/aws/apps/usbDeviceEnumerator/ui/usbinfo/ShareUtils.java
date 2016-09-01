package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

/*package*/ class ShareUtils {
    private static final String TAG = ShareUtils.class.getSimpleName();

    public static void share(Context context, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent share = Intent.createChooser(intent, context.getString(R.string.share_result_via));
        context.startActivity(share);
    }

    public static String tableToString(TableLayout t) {
        String res = "";
        if (t == null) {
            return res;
        }

        for (int i = 0; i <= t.getChildCount() - 1; i++) {
            TableRow row = (TableRow) t.getChildAt(i);

            for (int j = 0; j <= row.getChildCount() - 1; j++) {
                View v = row.getChildAt(j);

                try {
                    if (v.getClass() == Class.forName("android.widget.TextView")) {
                        TextView tmp = (TextView) v;
                        res += tmp.getText();

                        if (j == 0) {
                            res += " ";
                        }
                    } else if (v.getClass() == Class.forName("android.widget.EditText")) {
                        EditText tmp = (EditText) v;
                        res += tmp.getText().toString();
                    } else {
                        //do nothing
                    }
                } catch (Exception e) {
                    res = e.toString();
                    Log.e(TAG, "^ ERROR: tableToString: " + res);
                }
            }
            res += "\n";
        }
        return res;
    }
}

package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder;

public class SharePayloadFactory {
    private static final String TAG = ShareUtils.class.getSimpleName();

    public String getSharePayload(final ViewHolder holder) {
        final StringBuilder sb = new StringBuilder();
        sb.append(tableToString(holder.getHeaderTable()));
        sb.append(tableToString(holder.getTopTable()));
        sb.append('\n');
        sb.append(tableToString(holder.getFirstBottomTable()));
        sb.append('\n');
        sb.append(tableToString(holder.getSecondBottomTable()));
        return sb.toString();
    }

    private String tableToString(TableLayout table) {
        final StringBuilder sb = new StringBuilder();

        if (table == null) {
            return sb.toString();
        }

        for (int i = 0; i <= table.getChildCount() - 1; i++) {
            final TableRow row = (TableRow) table.getChildAt(i);

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

        return sb.toString();
    }
}

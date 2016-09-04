package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

/*package*/ class CommonLogic {
    private static final String TAG = CommonLogic.class.getSimpleName();

    public static void addDataRow(LayoutInflater inflater, TableLayout tlb, String cell1Text, String cell2Text) {
        final TableRow row = (TableRow) inflater.inflate(R.layout.usb_table_row_data, null);
        final TextView tv1 = (TextView) row.findViewById(R.id.usb_tablerow_cell1);
        final TextView tv2 = (TextView) row.findViewById(R.id.usb_tablerow_cell2);
        tv1.setText(cell1Text);
        tv2.setText(cell2Text);
        tlb.addView(row);
    }

    public static String padLeft(String string, String padding, int size) {
        String pad = "";
        while ((pad + string).length() < size) {
            pad += padding + pad;
        }
        return pad + string;
    }

    public static String getSharePayload(final InfoViewHolder holder) {
        final StringBuilder sb = new StringBuilder();
        sb.append(ShareUtils.tableToString(holder.getHeaderTable()));
        sb.append(ShareUtils.tableToString(holder.getTopTable()));
        sb.append('\n');
        sb.append(ShareUtils.tableToString(holder.getBottomTable()));
        return sb.toString();
    }
}

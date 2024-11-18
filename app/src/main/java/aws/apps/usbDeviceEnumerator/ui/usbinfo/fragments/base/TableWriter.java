package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

public class TableWriter {
    private final LayoutInflater inflater;
    private final TableLayout tlb;

    public TableWriter(final LayoutInflater inflater, final TableLayout table) {
        this.inflater = inflater;
        this.tlb = table;
    }

    public void addDataRow(String title, int data) {
        addDataRow(title, String.valueOf(data));
    }

    public void addDataRow(String title, String data) {
        final TableRow row = (TableRow) inflater.inflate(R.layout.usb_table_row_data, null);
        final TextView tv1 = row.findViewById(R.id.usb_tablerow_cell1);
        final TextView tv2 = row.findViewById(R.id.usb_tablerow_cell2);
        tv1.setText(title);
        tv2.setText(data);
        tlb.addView(row);
    }

    public void addTitleRow(String title) {
        final TableRow row = (TableRow) inflater.inflate(R.layout.usb_table_row_data, null);
        final TextView tv1 = row.findViewById(R.id.usb_tablerow_cell1);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setText(title);
        tlb.addView(row);
    }

    public void addEmptyRow() {
        addDataRow("", "");
    }

}

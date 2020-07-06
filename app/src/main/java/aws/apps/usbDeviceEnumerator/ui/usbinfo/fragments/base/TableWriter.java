package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

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

    public void addDataRow(String cell1Text,
                           String cell2Text) {
        final TableRow row = (TableRow) inflater.inflate(R.layout.usb_table_row_data, null);
        final TextView tv1 = row.findViewById(R.id.usb_tablerow_cell1);
        final TextView tv2 = row.findViewById(R.id.usb_tablerow_cell2);
        tv1.setText(cell1Text);
        tv2.setText(cell2Text);
        tlb.addView(row);
    }

}

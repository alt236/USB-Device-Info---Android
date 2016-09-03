package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

/*package*/ class InfoViewHolder {
    private final TableLayout tblUsbInfoHeader;
    private final TableLayout tblUsbInfoTop;
    private final TableLayout tblUsbInfoBottom;
    private final TextView tvVID;
    private final TextView tvPID;
    private final TextView tvVendorReported;
    private final TextView tvProductReported;
    private final TextView tvVendorDb;
    private final TextView tvProductDb;
    private final TextView tvDevicePath;
    private final TextView tvDeviceClass;
    private final ImageButton logo;

    public InfoViewHolder(final View rootView) {
        tblUsbInfoHeader = (TableLayout) rootView.findViewById(R.id.tblUsbInfo_title);
        tblUsbInfoTop = (TableLayout) rootView.findViewById(R.id.tblUsbInfo_top);
        tblUsbInfoBottom = (TableLayout) rootView.findViewById(R.id.tblUsbInfo_bottom);
        tvVID = ((TextView) rootView.findViewById(R.id.tvVID));
        tvPID = ((TextView) rootView.findViewById(R.id.tvPID));
        tvProductDb = ((TextView) rootView.findViewById(R.id.tvProductDb));
        tvVendorDb = ((TextView) rootView.findViewById(R.id.tvVendorDb));
        tvProductReported = ((TextView) rootView.findViewById(R.id.tvProductReported));
        tvVendorReported = ((TextView) rootView.findViewById(R.id.tvVendorReported));
        tvDevicePath = ((TextView) rootView.findViewById(R.id.tvDevicePath));
        tvDeviceClass = ((TextView) rootView.findViewById(R.id.tvDeviceClass));
        logo = (ImageButton) rootView.findViewById(R.id.btnLogo);
    }

    public ImageView getLogo() {
        return logo;
    }

    public TableLayout getHeaderTable() {
        return tblUsbInfoHeader;
    }

    public TableLayout getTopTable() {
        return tblUsbInfoTop;
    }

    public TableLayout getBottomTable() {
        return tblUsbInfoBottom;
    }

    public TextView getPid() {
        return tvPID;
    }

    public TextView getVid() {
        return tvVID;
    }

    public TextView getDevicePath() {
        return tvDevicePath;
    }

    public TextView getDeviceClass() {
        return tvDeviceClass;
    }

    public TextView getProductFromDb() {
        return tvProductDb;
    }

    public TextView getVendorFromDb() {
        return tvVendorDb;
    }

    public TextView getReportedProduct() {
        return tvProductReported;
    }

    public TextView getReportedVendor() {
        return tvVendorReported;
    }
}

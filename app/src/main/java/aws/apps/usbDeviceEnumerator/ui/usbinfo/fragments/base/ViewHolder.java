package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

public class ViewHolder {
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

    public ViewHolder(final View rootView) {
        tblUsbInfoHeader = rootView.findViewById(R.id.tblUsbInfo_title);
        tblUsbInfoTop = rootView.findViewById(R.id.tblUsbInfo_top);
        tblUsbInfoBottom = rootView.findViewById(R.id.tblUsbInfo_bottom);
        tvVID = rootView.findViewById(R.id.tvVID);
        tvPID = rootView.findViewById(R.id.tvPID);
        tvProductDb = rootView.findViewById(R.id.tvProductDb);
        tvVendorDb = rootView.findViewById(R.id.tvVendorDb);
        tvProductReported = rootView.findViewById(R.id.tvProductReported);
        tvVendorReported = rootView.findViewById(R.id.tvVendorReported);
        tvDevicePath = rootView.findViewById(R.id.tvDevicePath);
        tvDeviceClass = rootView.findViewById(R.id.tvDeviceClass);
        logo = rootView.findViewById(R.id.btnLogo);
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

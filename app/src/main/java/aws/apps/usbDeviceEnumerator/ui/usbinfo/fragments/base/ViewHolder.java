package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import aws.apps.usbDeviceEnumerator.R;

public class ViewHolder {
    private final TableLayout tblUsbInfoHeader;
    private final TableLayout tblUsbInfoTop;
    private final TableLayout firstBottomInfoTable;
    private final TableLayout secondBottomInfoTable;
    private final TextView tvVID;
    private final TextView tvPID;
    private final TextView tvVendorReported;
    private final TextView tvProductReported;
    private final TextView tvVendorDb;
    private final TextView tvProductDb;
    private final TextView tvDevicePath;
    private final TextView tvDeviceClass;
    private final ImageButton logo;

    private final TabLayout bottomInfoTabLayout;
    private final ViewPager bottomTabViewPager;
    private final View rootView;

    public ViewHolder(final View rootView) {
        this.rootView = rootView;
        tblUsbInfoHeader = rootView.findViewById(R.id.tblUsbInfo_title);
        tblUsbInfoTop = rootView.findViewById(R.id.tblUsbInfo_top);
        firstBottomInfoTable = rootView.findViewById(R.id.first_bottom_table);
        secondBottomInfoTable = rootView.findViewById(R.id.second_bottom_table);
        bottomInfoTabLayout = rootView.findViewById(R.id.tabs);
        bottomTabViewPager = rootView.findViewById(R.id.pager);
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

    public View getRootView() {
        return rootView;
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

    public TableLayout getFirstBottomTable() {
        return firstBottomInfoTable;
    }

    public TableLayout getSecondBottomTable() {
        return secondBottomInfoTable;
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

    public TabLayout getBottomInfoTabLayout() {
        return bottomInfoTabLayout;
    }

    public ViewPager getBottomTabViewPager() {
        return bottomTabViewPager;
    }
}

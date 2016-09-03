/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.usb.UsbConstants;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbDevice;

public class LinuxUsbInfoFragment extends BaseInfoFragment {
    public final static String DEFAULT_STRING = "???";
    private final static String EXTRA_DATA = LinuxUsbInfoFragment.class.getName() + ".BUNDLE_DATA";
    private static final int LAYOUT_ID = R.layout.fragment_usb_info;
    private final String TAG = this.getClass().getName();
    private DbAccessUsb dbUsb;
    private DbAccessCompany dbComp;
    private ZipAccessCompany zipComp;
    private SysBusUsbDevice device;
    private InfoViewHolder viewHolder;

    public static Fragment create(final SysBusUsbDevice usbDevice) {
        final Fragment fragment = new LinuxUsbInfoFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA, usbDevice);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        dbUsb = new DbAccessUsb(context);
        dbComp = new DbAccessCompany(context);
        zipComp = new ZipAccessCompany(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        return inflater.inflate(LAYOUT_ID, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        viewHolder = new InfoViewHolder(view);

        device = getArguments().getParcelable(EXTRA_DATA);

        populateLinuxTable(LayoutInflater.from(getContext()));
    }

    private void populateLinuxTable(LayoutInflater inflater) {
        if (device == null) {
            return;
        }

        final String vid = CommonLogic.padLeft(device.getVID(), "0", 4);
        final String pid = CommonLogic.padLeft(device.getPID(), "0", 4);
        final String deviceClass = UsbConstants.resolveUsbClass(device.getDeviceClass());

        viewHolder.getLogo().setImageResource(R.drawable.no_image);

        viewHolder.getVid().setText(vid);
        viewHolder.getPid().setText(pid);
        viewHolder.getDevicePath().setText(device.getDevicePath());
        viewHolder.getDeviceClass().setText(deviceClass);

        viewHolder.getReportedVendor().setText(device.getReportedVendorName());
        viewHolder.getReportedProduct().setText(device.getReportedProductName());

        if (dbUsb.doDBChecks()) {
            final String vendorFromDb = dbUsb.getVendor(vid);
            viewHolder.getVendorFromDb().setText(vendorFromDb);
            viewHolder.getProductFromDb().setText(dbUsb.getProduct(vid, pid));

            if (dbComp.doDBChecks()) {
                final String searchFor;

                if (!TextUtils.isEmpty(vendorFromDb)) {
                    searchFor = vendorFromDb;
                } else {
                    searchFor = device.getReportedVendorName();
                }

                final String logo = dbComp.getLogo(searchFor);
                CommonLogic.loadLogo(viewHolder.getLogo(), zipComp, logo);
            }
        }

        final TableLayout bottomTable = viewHolder.getBottomTable();
        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.usb_version_), device.getUsbVersion());
        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.speed_), device.getSpeed());
        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.protocol_), device.getDeviceProtocol());
        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.maximum_power_), device.getMaxPower());
        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.serial_number_), device.getSerialNumber());
    }

    @Override
    public String getSharePayload() {
        return CommonLogic.getSharePayload(viewHolder);
    }
}

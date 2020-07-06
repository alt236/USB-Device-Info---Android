/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.BaseInfoFragment;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing.SharePayloadFactory;
import aws.apps.usbDeviceEnumerator.util.StringUtils;
import uk.co.alt236.usbdeviceenumerator.UsbConstantResolver;

public class InfoFragmentAndroid extends BaseInfoFragment {
    public final static String DEFAULT_STRING = "???";
    private final static String EXTRA_DATA = InfoFragmentAndroid.class.getName() + ".BUNDLE_DATA";
    private static final SharePayloadFactory SHARE_PAYLOAD_FACTORY = new SharePayloadFactory();
    private static final int LAYOUT_ID = R.layout.fragment_usb_info;

    private final String TAG = this.getClass().getName();
    private String usbKey = DEFAULT_STRING;
    private ViewHolder viewHolder;
    private UsbManager usbMan;
    private UsbDevice device;
    private boolean validData;

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        usbMan = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saved) {
        usbKey = getArguments().getString(EXTRA_DATA, DEFAULT_STRING);
        device = usbMan.getDeviceList().get(usbKey);

        final View view;

        if (usbKey == null || device == null) {
            view = inflater.inflate(R.layout.fragment_error, container, false);
            validData = false;
        } else {
            view = inflater.inflate(LAYOUT_ID, container, false);
            validData = true;
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        viewHolder = new ViewHolder(view);

        usbKey = getArguments().getString(EXTRA_DATA, DEFAULT_STRING);

        if (validData) {
            viewHolder = new ViewHolder(view);
            populateDataTable(LayoutInflater.from(getContext()));
        } else {
            final TextView textView = view.findViewById(R.id.errorText);
            if (usbKey == null) {
                textView.setText(R.string.error_loading_device_info_unknown);
            } else {
                textView.setText(R.string.error_loading_device_info_device_disconnected);
            }
        }
    }

    private void populateDataTable(LayoutInflater inflater) {
        final String vid = StringUtils.padLeft(Integer.toHexString(device.getVendorId()), '0', 4);
        final String pid = StringUtils.padLeft(Integer.toHexString(device.getProductId()), '0', 4);
        final String deviceClass = UsbConstantResolver.resolveUsbClass(device.getDeviceClass());

        viewHolder.getLogo().setImageResource(R.drawable.no_image);
        viewHolder.getVid().setText(vid);
        viewHolder.getPid().setText(pid);
        viewHolder.getDevicePath().setText(usbKey);
        viewHolder.getDeviceClass().setText(deviceClass);

        new BottomTableBuilder(getResources(), inflater).build(viewHolder.getBottomTable(), device);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.getReportedVendor().setText(device.getManufacturerName());
            viewHolder.getReportedProduct().setText(device.getProductName());
            loadAsyncData(viewHolder, vid, pid, device.getManufacturerName());
        } else {
            viewHolder.getReportedVendor().setText(R.string.not_provided);
            viewHolder.getReportedProduct().setText(R.string.not_provided);
            loadAsyncData(viewHolder, vid, pid, null);
        }
    }

    @Override
    public String getSharePayload() {
        return SHARE_PAYLOAD_FACTORY.getSharePayload(viewHolder);
    }

    public static Fragment create(final String usbKey) {
        final Fragment fragment = new InfoFragmentAndroid();
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, usbKey);
        fragment.setArguments(bundle);
        return fragment;
    }
}

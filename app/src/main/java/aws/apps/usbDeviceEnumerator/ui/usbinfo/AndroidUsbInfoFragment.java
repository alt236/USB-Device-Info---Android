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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyInfo;
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyLogo;
import aws.apps.usbDeviceEnumerator.data.DataProviderUsbInfo;
import uk.co.alt236.usbdeviceenumerator.UsbConstants;

public class AndroidUsbInfoFragment extends BaseInfoFragment {
    public final static int TYPE_ANDROID_INFO = 0;
    public final static int TYPE_LINUX_INFO = 1;
    public final static String DEFAULT_STRING = "???";
    private final static String EXTRA_DATA = AndroidUsbInfoFragment.class.getName() + ".BUNDLE_DATA";
    private static final int LAYOUT_ID = R.layout.fragment_usb_info;

    private final String TAG = this.getClass().getName();
    private String usbKey = DEFAULT_STRING;
    private InfoViewHolder viewHolder;
    private UsbManager usbMan;
    private DataFetcher dataFetcher;
    private UsbDevice device;
    private boolean validData;

    public static Fragment create(final String usbKey) {
        final Fragment fragment = new AndroidUsbInfoFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, usbKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        usbMan = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
        dataFetcher = new DataFetcher(
                new DataProviderCompanyInfo(context),
                new DataProviderUsbInfo(context),
                new DataProviderCompanyLogo(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
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
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        viewHolder = new InfoViewHolder(view);

        usbKey = getArguments().getString(EXTRA_DATA, DEFAULT_STRING);


        if (validData) {
            viewHolder = new InfoViewHolder(view);
            populateDataTable(LayoutInflater.from(getContext()));
        } else {
            final TextView textView = (TextView) view.findViewById(R.id.errorText);
            if (usbKey == null) {
                textView.setText(R.string.error_loading_device_info_unknown);
            } else {
                textView.setText(R.string.error_loading_device_info_device_disconnected);
            }
        }
    }

    private void populateDataTable(LayoutInflater inflater) {
        final String vid = CommonLogic.padLeft(Integer.toHexString(device.getVendorId()), "0", 4);
        final String pid = CommonLogic.padLeft(Integer.toHexString(device.getProductId()), "0", 4);
        final String deviceClass = UsbConstants.resolveUsbClass(device.getDeviceClass());

        viewHolder.getLogo().setImageResource(R.drawable.no_image);

        viewHolder.getVid().setText(vid);
        viewHolder.getPid().setText(pid);
        viewHolder.getDevicePath().setText(usbKey);
        viewHolder.getDeviceClass().setText(deviceClass);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.getReportedVendor().setText(device.getManufacturerName());
            viewHolder.getReportedProduct().setText(device.getProductName());
        } else {
            viewHolder.getReportedVendor().setText("not provided");
            viewHolder.getReportedProduct().setText("not provided");
        }

        UsbInterface iFace;
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            iFace = device.getInterface(i);
            if (iFace != null) {
                final TableLayout bottomTable = viewHolder.getBottomTable();
                final String usbClass = UsbConstants.resolveUsbClass((iFace.getInterfaceClass()));

                CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.interface_) + i, "");
                CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.class_), usbClass);

                if (iFace.getEndpointCount() > 0) {
                    String endpointText;
                    for (int j = 0; j < iFace.getEndpointCount(); j++) {
                        endpointText = getEndpointText(iFace.getEndpoint(j), j);
                        CommonLogic.addDataRow(inflater, bottomTable, getString(R.string.endpoint_), endpointText);
                    }
                } else {
                    CommonLogic.addDataRow(inflater, bottomTable, "\tEndpoints:", "none");
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadAsyncData(vid, pid, device.getManufacturerName());
        } else {
            loadAsyncData(vid, pid, null);
        }
    }

    private String getEndpointText(final UsbEndpoint endpoint, final int index) {
        final String addressInBinary = CommonLogic.padLeft(Integer.toBinaryString(endpoint.getAddress()), "0", 8);

        String endpointText = "#" + index + "\n";
        endpointText += getString(R.string.address_) + endpoint.getAddress() + " (" + addressInBinary + ")\n";
        endpointText += getString(R.string.number_) + endpoint.getEndpointNumber() + "\n";
        endpointText += getString(R.string.direction_) + UsbConstants.resolveUsbEndpointDirection(endpoint.getDirection()) + "\n";
        endpointText += getString(R.string.type_) + UsbConstants.resolveUsbEndpointType(endpoint.getType()) + "\n";
        endpointText += getString(R.string.poll_interval_) + endpoint.getInterval() + "\n";
        endpointText += getString(R.string.max_packet_size_) + endpoint.getMaxPacketSize() + "\n";
        endpointText += getString(R.string.attributes_) + CommonLogic.padLeft(Integer.toBinaryString(endpoint.getAttributes()), "0", 8);

        return endpointText;
    }

    private void loadAsyncData(String vid, String pid, String reportedVendorName) {
        dataFetcher.fetchData(vid, pid, reportedVendorName, new DataFetcher.Callback() {
            @Override
            public void onSuccess(final String vendorFromDb,
                                  final String productFromDb,
                                  final Bitmap bitmap) {

                if (isAdded() && getActivity() != null && getView() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.getVendorFromDb().setText(vendorFromDb);
                            viewHolder.getProductFromDb().setText(productFromDb);
                            if (bitmap != null) {
                                final BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                                viewHolder.getLogo().setImageDrawable(drawable);
                            } else {
                                viewHolder.getLogo().setImageResource(R.drawable.no_image);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public String getSharePayload() {
        return CommonLogic.getSharePayload(viewHolder);
    }
}

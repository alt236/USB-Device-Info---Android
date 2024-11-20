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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.common.IntExt;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper.ApiConditionalResultMapper;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.BaseInfoFragment;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing.SharePayloadFactory;
import dagger.hilt.android.AndroidEntryPoint;
import uk.co.alt236.androidusbmanager.AndroidUsbManager;
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice;

@AndroidEntryPoint
public class InfoFragmentAndroid extends BaseInfoFragment {
    public final static String DEFAULT_STRING = "???";
    private final static String EXTRA_DATA = InfoFragmentAndroid.class.getName() + ".BUNDLE_DATA";
    private static final SharePayloadFactory SHARE_PAYLOAD_FACTORY = new SharePayloadFactory();
    private static final int LAYOUT_ID = R.layout.fragment_usb_info;

    @Inject
    protected AndroidUsbManager usbManager;
    @Inject
    protected ApiConditionalResultMapper resultMapper;
    @Inject
    protected AndroidUsbInfoDataBinder binder;

    private String usbKey = DEFAULT_STRING;
    private ViewHolder viewHolder;

    private boolean validData;
    private AndroidUsbDevice device;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saved) {
        usbKey = requireArguments().getString(EXTRA_DATA, DEFAULT_STRING);
        device = usbManager.getDeviceList().get(usbKey);

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

        usbKey = requireArguments().getString(EXTRA_DATA, DEFAULT_STRING);

        if (validData) {
            viewHolder = new ViewHolder(view);
            populateDataTable(LayoutInflater.from(requireContext()));
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
        binder.bind(inflater, viewHolder, usbKey, device);
        final String manufacturerName = device.getManufacturerName().getValueOrNull();
        loadAsyncData(
                viewHolder,
                IntExt.INSTANCE.formatVidPid(device.getVendorId(), false),
                IntExt.INSTANCE.formatVidPid(device.getProductId(), false),
                manufacturerName);
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

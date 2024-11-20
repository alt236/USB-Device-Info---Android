package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android

import android.view.LayoutInflater
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.common.IntExt.formatVidPid
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper.ApiConditionalResultMapper
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.table.ConfigurationTableBuilder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.table.InterfaceTableBuilder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.tabs.BottomTabSetup
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.UsbConstantResolver
import javax.inject.Inject

class AndroidUsbInfoDataBinder @Inject constructor(
    private val apiConditionalResultMapper: ApiConditionalResultMapper
) {
    fun bind(
        inflater: LayoutInflater,
        viewHolder: ViewHolder,
        usbKey: String,
        device: AndroidUsbDevice
    ) {
        val vid = device.vendorId.formatVidPid()
        val pid = device.productId.formatVidPid()
        val deviceClass = UsbConstantResolver.resolveUsbClass(device.deviceClass);

        viewHolder.logo.setImageResource(R.drawable.no_image);
        viewHolder.vid.text = vid;
        viewHolder.pid.text = pid;
        viewHolder.devicePath.text = usbKey;
        viewHolder.deviceClass.text = deviceClass;

        populateBottomTabs(inflater, viewHolder, device);

        val manufacturedNameResult = device.manufacturerName
        val mappedManufacturerNameValue = apiConditionalResultMapper.map(manufacturedNameResult);

        viewHolder.reportedVendor.text = mappedManufacturerNameValue;
        viewHolder.reportedProduct.text = apiConditionalResultMapper.map(device.productName);
    }

    private fun populateBottomTabs(
        inflater: LayoutInflater,
        viewHolder: ViewHolder,
        device: AndroidUsbDevice
    ) {
        BottomTabSetup().setup(
            viewHolder, listOf(
                "Interfaces",
                "Configurations"
            )
        )

        val resources = viewHolder.rootView.resources
        InterfaceTableBuilder(resources, inflater).build(
            viewHolder.firstBottomTable,
            device
        )
        ConfigurationTableBuilder(resources, inflater).build(
            viewHolder.secondBottomTable,
            device
        )
    }
}
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.linux

import android.content.res.Resources
import android.view.LayoutInflater
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.TableWriter
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.tabs.BottomTabSetup
import aws.apps.usbDeviceEnumerator.util.StringUtils
import dev.alt236.usbdeviceenumerator.UsbConstantResolver
import dev.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice
import javax.inject.Inject

class SysUsbInfoDataBinder @Inject constructor(private val res: Resources) {
    fun bind(
        inflater: LayoutInflater,
        viewHolder: ViewHolder,
        device: SysBusUsbDevice
    ) {
        val vid = StringUtils.padLeft(device.vid, '0', 4)
        val pid = StringUtils.padLeft(device.pid, '0', 4)
        val deviceClass = UsbConstantResolver.resolveUsbClass(device)

        viewHolder.logo.setImageResource(R.drawable.no_image)

        viewHolder.vid.text = vid
        viewHolder.pid.text = pid
        viewHolder.devicePath.text = device.devicePath
        viewHolder.deviceClass.text = deviceClass

        viewHolder.reportedVendor.text = device.reportedVendorName
        viewHolder.reportedProduct.text = device.reportedProductName

        BottomTabSetup().setup(viewHolder, listOf(res.getString(R.string.additional_info)))

        val tableWriter = TableWriter(inflater, viewHolder.firstBottomTable)
        tableWriter.addDataRow(res.getString(R.string.usb_version_), device.usbVersion)
        tableWriter.addDataRow(res.getString(R.string.speed_), device.speed)
        tableWriter.addDataRow(res.getString(R.string.protocol_), device.deviceProtocol)
        tableWriter.addDataRow(res.getString(R.string.maximum_power_), device.maxPower)
        tableWriter.addDataRow(res.getString(R.string.serial_number_), device.serialNumber)
    }
}
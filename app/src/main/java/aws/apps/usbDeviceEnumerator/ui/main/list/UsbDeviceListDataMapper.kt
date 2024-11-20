package aws.apps.usbDeviceEnumerator.ui.main.list

import aws.apps.usbDeviceEnumerator.ui.main.list.UsbDeviceListAdapter.UsbDevice
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice
import javax.inject.Inject

class UsbDeviceListDataMapper @Inject constructor() {

    fun map(input: Map<String, Any>): List<UsbDevice> {
        val result = ArrayList<UsbDevice>()

        for (pair in input) {
            val listDevice = when (val device = pair.value) {
                is AndroidUsbDevice -> UsbDevice.AndroidUsb(pair.key, device)
                is SysBusUsbDevice -> UsbDevice.SysUsb(pair.key, device)
                else -> throw IllegalArgumentException("Don't know how to map '${device}'")
            }
            result.add(listDevice)
        }
        result.sortBy { it.key }
        return result
    }

}
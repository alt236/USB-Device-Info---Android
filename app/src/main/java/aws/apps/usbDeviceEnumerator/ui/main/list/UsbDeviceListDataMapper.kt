package aws.apps.usbDeviceEnumerator.ui.main.list

import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice
import javax.inject.Inject

class UsbDeviceListDataMapper @Inject constructor() {

    fun map(input: Map<String, *>): List<UiUsbDevice> {
        val result = ArrayList<UiUsbDevice>()

        for (pair in input) {
            val listDevice = when (val device = pair.value) {
                is AndroidUsbDevice -> UiUsbDevice.AndroidUsb(pair.key, device)
                is SysBusUsbDevice -> UiUsbDevice.SysUsb(pair.key, device)
                else -> throw IllegalArgumentException("Don't know how to map '${device}'")
            }
            result.add(listDevice)
        }
        result.sortBy { it.key }
        return result
    }

}
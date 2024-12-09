package uk.co.alt236.androidusbmanager

import android.hardware.usb.UsbManager
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice

class AndroidUsbManager(private val usbManager: UsbManager?) {

    fun getDeviceList(): Map<String, AndroidUsbDevice> {
        val usbDevices = usbManager?.deviceList ?: emptyMap()

        val wrappedDevices = HashMap<String, AndroidUsbDevice>(usbDevices.size)

        for (pair in usbDevices) {
            wrappedDevices[pair.key] = AndroidUsbDevice(pair.value)
        }

        return wrappedDevices
    }
}
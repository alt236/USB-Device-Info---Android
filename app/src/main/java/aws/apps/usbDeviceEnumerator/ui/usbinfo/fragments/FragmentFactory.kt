package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments

import androidx.fragment.app.Fragment
import aws.apps.usbDeviceEnumerator.ui.main.list.UiUsbDevice
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.AndroidUsbInfoFragment
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.linux.LinuxUsbInfoFragment
import javax.inject.Inject

class FragmentFactory @Inject constructor() {

    fun getFragment(device: UiUsbDevice): Fragment {
        return when (device) {
            is UiUsbDevice.AndroidUsb -> getFragment(device)
            is UiUsbDevice.SysUsb -> getFragment(device)
        }
    }

    fun getFragment(device: UiUsbDevice.AndroidUsb): Fragment =
        AndroidUsbInfoFragment.create(device.device)

    fun getFragment(device: UiUsbDevice.SysUsb) = LinuxUsbInfoFragment.create(device.device)

}

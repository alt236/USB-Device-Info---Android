package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments

import androidx.fragment.app.Fragment
import aws.apps.usbDeviceEnumerator.ui.main.list.UiUsbDevice
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.InfoFragmentAndroid
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.linux.InfoFragmentLinux
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice
import javax.inject.Inject

class FragmentFactory @Inject constructor() {

    fun getFragment(device: UiUsbDevice): Fragment {
        return when (device) {
            is UiUsbDevice.AndroidUsb -> getFragment(device.key)
            is UiUsbDevice.SysUsb -> getFragment(device.device)
        }
    }

    fun getFragment(androidKey: String): Fragment = InfoFragmentAndroid.create(androidKey)
    fun getFragment(device: SysBusUsbDevice) = InfoFragmentLinux.create(device)

}

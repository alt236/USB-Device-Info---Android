package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments;

import android.support.v4.app.Fragment;

import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice;

public final class FragmentFactory {

    public static Fragment getFragment(String usbKey) {
        return InfoFragmentAndroid.create(usbKey);
    }

    public static Fragment getFragment(SysBusUsbDevice usbDevice) {
        return InfoFragmentLinux.create(usbDevice);
    }
}

package aws.apps.usbDeviceEnumerator.ui.common;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.UsbInfoActivity;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.FragmentFactory;
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice;

public class Navigation {
    private static final String TAG = Navigation.class.getSimpleName();
    private static final int FRAGMENT_CONTAINER = R.id.fragment_container;
    private static final int DEFAULT_FRAGMENT_TRANSACTION = FragmentTransaction.TRANSIT_FRAGMENT_FADE;

    private final AppCompatActivity activity;

    public Navigation(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void showAndroidUsbDeviceInfo(String device) {
        if (isSmallScreen()) {
            final Intent i = new Intent(activity.getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_ANDROID, device);
            startActivity(i);
        } else {
            final Fragment fragment = FragmentFactory.getFragment(device);
            stackFragment(fragment);
        }
    }

    public void showLinuxUsbDeviceInfo(SysBusUsbDevice device) {
        if (isSmallScreen()) {
            final Intent i = new Intent(activity.getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_LINUX, device);
            startActivity(i);
        } else {
            final Fragment fragment = FragmentFactory.getFragment(device);
            stackFragment(fragment);
        }
    }

    public boolean isSmallScreen() {
        final boolean res = activity.findViewById(FRAGMENT_CONTAINER) == null;
        Log.d(TAG, "^ Is " + activity.getClass().getName() + " running in a small screen? " + res);
        return res;
    }

    public void stackFragment(Fragment fragment) {
        final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(FRAGMENT_CONTAINER, fragment);
        ft.setTransition(DEFAULT_FRAGMENT_TRANSACTION);

        ft.commit();
    }

    public void removeFragmentsFromContainer() {
        final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER);

        if (fragment != null) {
            final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.setTransition(DEFAULT_FRAGMENT_TRANSACTION);
            ft.commit();
        }
    }

    private void startActivity(final Intent intent) {
        ActivityCompat.startActivity(activity, intent, null);
    }
}

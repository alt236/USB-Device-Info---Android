package aws.apps.usbDeviceEnumerator.ui.common;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.main.list.UiUsbDevice;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.UsbInfoActivity;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.FragmentFactory;

public class Navigation {
    private static final String TAG = Navigation.class.getSimpleName();
    private static final int FRAGMENT_CONTAINER = R.id.fragment_container;
    private static final int DEFAULT_FRAGMENT_TRANSACTION = FragmentTransaction.TRANSIT_FRAGMENT_FADE;

    private final AppCompatActivity activity;
    private final FragmentFactory fragmentFactory;

    public Navigation(AppCompatActivity activity, FragmentFactory fragmentFactory) {
        this.activity = activity;
        this.fragmentFactory = fragmentFactory;
    }

    public void showUsbDeviceInfo(UiUsbDevice device) {
        if (isSmallScreen()) {
            final Intent i = UsbInfoActivity.createIntent(activity, device);
            startActivity(i);
        } else {
            final Fragment fragment = fragmentFactory.getFragment(device);
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

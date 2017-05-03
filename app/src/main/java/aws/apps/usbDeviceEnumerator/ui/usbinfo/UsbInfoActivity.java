/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import aws.apps.usbDeviceEnumerator.R;
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice;

public class UsbInfoActivity extends AppCompatActivity {
    public static final String EXTRA_DATA_ANDROID = UsbInfoActivity.class.getName() + ".EXTRA_DATA_ANDROID";
    public static final String EXTRA_DATA_LINUX = UsbInfoActivity.class.getName() + ".EXTRA_DATA_LINUX";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_usb_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
        } else {
            final String androidKey = b.getString(EXTRA_DATA_ANDROID);
            final SysBusUsbDevice linuxDevice = (SysBusUsbDevice) b.getSerializable(EXTRA_DATA_LINUX);

            final Fragment fragment;

            if (androidKey != null) {
                fragment = InfoFragmentFactory.getFragment(androidKey);
            } else if (linuxDevice != null) {
                fragment = InfoFragmentFactory.getFragment(linuxDevice);
            } else {
                fragment = null;
            }

            if (fragment == null) {
                finish();
            } else {
                showFragment(fragment);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFragment(final Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}

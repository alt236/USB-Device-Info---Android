/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.main.list.UiUsbDevice;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.FragmentFactory;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UsbInfoActivity extends AppCompatActivity {
    public static final String EXTRA_DATA_DEVICE = UsbInfoActivity.class.getName() + ".EXTRA_DATA_DEVICE";

    @Inject
    protected FragmentFactory deviceInfoFragmentFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_usb_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
        } else {
            final UiUsbDevice device = b.getParcelable(EXTRA_DATA_DEVICE);
            if (device == null) {
                finish();
            }

            final Fragment fragment = deviceInfoFragmentFactory.getFragment(device);
            showFragment(fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    public static Intent createIntent(Context context, UiUsbDevice device) {
        final Intent intent = new Intent(context, UsbInfoActivity.class);
        intent.putExtra(UsbInfoActivity.EXTRA_DATA_DEVICE, device);
        return intent;
    }
}

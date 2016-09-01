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
package aws.apps.usbDeviceEnumerator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public abstract class AbstractUsbDeviceInfoFragment extends Fragment {
    public final static int TYPE_ANDROID_INFO = 0;
    public final static int TYPE_LINUX_INFO = 1;

    public abstract int getType();

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.frag_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_export:
                UsefulBits.share(getActivity(), "USB Info", this.toString());
                return true;
        }
        return false;
    }

    public abstract String toString();
}

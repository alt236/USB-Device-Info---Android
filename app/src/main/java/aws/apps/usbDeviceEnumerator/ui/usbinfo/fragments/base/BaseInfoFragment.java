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
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing.ShareUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseInfoFragment extends Fragment {

    @Inject
    DataFetcher dataFetcher;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.frag_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_export) {
            ShareUtils.share(
                    requireActivity(),
                    getString(R.string.app_name),
                    getSharePayload());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract String getSharePayload();


    protected void loadAsyncData(final ViewHolder viewHolder,
                                 final String vid,
                                 final String pid,
                                 final String reportedVendorName) {

        dataFetcher.fetchData(vid, pid, reportedVendorName, (vendorFromDb, productFromDb, bitmap) -> {

            if (isFragmentValidAndHasView()) {
                requireActivity().runOnUiThread(() -> {
                    viewHolder.getVendorFromDb().setText(vendorFromDb);
                    viewHolder.getProductFromDb().setText(productFromDb);
                    if (bitmap != null) {
                        final BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                        viewHolder.getLogo().setImageDrawable(drawable);
                    } else {
                        viewHolder.getLogo().setImageResource(R.drawable.no_image);
                    }
                });
            }
        });
    }

    private boolean isFragmentValidAndHasView() {
        return isAdded() && getActivity() != null && getView() != null;
    }
}

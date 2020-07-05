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
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyInfo;
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyLogo;
import aws.apps.usbDeviceEnumerator.data.DataProviderUsbInfo;

/*package*/ abstract class BaseInfoFragment extends Fragment {

    private DataFetcher dataFetcher;

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        dataFetcher = new DataFetcher(
                new DataProviderCompanyInfo(context),
                new DataProviderUsbInfo(context),
                new DataProviderCompanyLogo(context));
    }

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
                    getActivity(),
                    getString(R.string.app_name),
                    getSharePayload());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract String getSharePayload();


    protected void loadAsyncData(final ViewHolder viewHolder,
                                 final String vid,
                                 final String pid,
                                 final String reportedVendorName) {

        dataFetcher.fetchData(vid, pid, reportedVendorName, (vendorFromDb, productFromDb, bitmap) -> {

            if (isAdded() && getActivity() != null && getView() != null) {
                getActivity().runOnUiThread(() -> {
                    viewHolder.getVendorFromDb().setText(vendorFromDb);
                    viewHolder.getProductFromDb().setText(productFromDb);
                    if (bitmap != null) {
                        final BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                        viewHolder.getLogo().setImageDrawable(drawable);
                    } else {
                        viewHolder.getLogo().setImageResource(R.drawable.no_image);
                    }
                });
            }
        });
    }

    protected void addDataRow(LayoutInflater inflater,
                              TableLayout tlb,
                              String cell1Text,
                              String cell2Text) {
        final TableRow row = (TableRow) inflater.inflate(R.layout.usb_table_row_data, null);
        final TextView tv1 = row.findViewById(R.id.usb_tablerow_cell1);
        final TextView tv2 = row.findViewById(R.id.usb_tablerow_cell2);
        tv1.setText(cell1Text);
        tv2.setText(cell2Text);
        tlb.addView(row);
    }

    protected String padLeft(String string,
                             String padding,
                             int size) {
        StringBuilder pad = new StringBuilder();
        while ((pad + string).length() < size) {
            pad.append(padding).append(pad);
        }
        return pad + string;
    }
}

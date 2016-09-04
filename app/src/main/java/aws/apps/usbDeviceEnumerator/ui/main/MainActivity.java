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
package aws.apps.usbDeviceEnumerator.ui.main;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.ui.common.Navigation;
import aws.apps.usbDeviceEnumerator.ui.dbupdate.DatabaseUpdater;
import aws.apps.usbDeviceEnumerator.ui.main.tabs.TabController;
import aws.apps.usbDeviceEnumerator.ui.main.tabs.TabViewHolder;
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.InfoFragmentFactory;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbDevice;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbManager;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getName();

    private UsbManager mUsbManAndroid;
    private SysBusUsbManager mUsbManagerLinux;

    private DbAccessUsb mDbUsb;
    private DbAccessCompany mDbComp;
    private ZipAccessCompany mZipComp;

    private Map<String, UsbDevice> mAndroidDeviceMap;
    private Map<String, SysBusUsbDevice> mLinuxDeviceMap;

    private ProgressDialogControl progressDialogControl;
    private Navigation mNavigation;

    private TabController mTabController;

    private void checkIfDbPresent() {
        // Prompt user to DL db if it is missing.
        if (!new File(mDbUsb.getFilePath()).exists()) {
            DialogFactory.createOkDialog(this,
                    R.string.alert_db_not_found_title,
                    R.string.alert_db_not_found_instructions)
                    .show();
            Log.w(TAG, "^ Database not found: " + mDbUsb.getFilePath());
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mTabController = new TabController(this);
        mNavigation = new Navigation(this);

        mUsbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
        mUsbManagerLinux = new SysBusUsbManager();

        mDbUsb = new DbAccessUsb(this);
        mDbComp = new DbAccessCompany(this);
        mZipComp = new ZipAccessCompany(this);

        mTabController.setup(new TabController.OnTabChangeListener() {
            @Override
            public void onTabChangeListener(String tag, TabViewHolder holder) {
                onTabChanged(tag, holder);
            }
        });

        // Setup android list - tab1;
        mTabController.getHolderForTag(TabController.TAB_ANDROID_INFO)
                .getList().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ListView) parent).setItemChecked(position, true);
                mNavigation.showAndroidUsbDeviceInfo(((TextView) view).getText().toString());
            }
        });


        // Setup linux list - tab2
        mTabController.getHolderForTag(TabController.TAB_LINUX_INFO)
                .getList().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ListView) parent).setItemChecked(position, true);
                mNavigation.showLinuxUsbDeviceInfo(mLinuxDeviceMap.get(((TextView) view).getText().toString()));
            }
        });


        checkIfDbPresent();
        refreshUsbDevices();
    }

    /**
     * Creates the menu items
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles item selections
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutDialogFactory.createAboutDialog(this).show();
                return true;
            case R.id.menu_update_db:
                final ProgressDialogControl control = new ProgressDialogControl(getSupportFragmentManager());
                final DatabaseUpdater databaseUpdater = new DatabaseUpdater(control, mDbComp, mDbUsb, mZipComp);

                databaseUpdater.start(this);
                return true;
            case R.id.menu_refresh:
                refreshUsbDevices();
                return true;
        }

        return false;
    }

    private void onTabChanged(String tabId, TabViewHolder tabViewHolder) {
        if (mNavigation.isSmallScreen()) {
            return;
        }

        final ListView listView = tabViewHolder.getList();
        final int checkedItemPosition = listView.getCheckedItemPosition();
        final Fragment fragment;

        if (checkedItemPosition == ListView.INVALID_POSITION) {
            fragment = InfoFragmentFactory.getFragment("");
        } else {
            final String text = (String) listView.getItemAtPosition(checkedItemPosition);

            switch (tabId) {
                case TabController.TAB_ANDROID_INFO:
                    fragment = InfoFragmentFactory.getFragment(text);
                    break;
                case TabController.TAB_LINUX_INFO:
                    fragment = InfoFragmentFactory.getFragment(mLinuxDeviceMap.get(text));
                    break;
                default:
                    fragment = InfoFragmentFactory.getFragment("");
                    break;
            }
        }

        mNavigation.stackFragment(fragment);
    }


    private void refreshUsbDevices() {
        mAndroidDeviceMap = mUsbManAndroid.getDeviceList();
        mLinuxDeviceMap = mUsbManagerLinux.getUsbDevices();

        updateList(mTabController.getHolderForTag(TabController.TAB_ANDROID_INFO), mAndroidDeviceMap);
        updateList(mTabController.getHolderForTag(TabController.TAB_LINUX_INFO), mLinuxDeviceMap);
    }

    private void updateList(final TabViewHolder holder, final Map<String, ?> map) {
        final String[] array = map.keySet().toArray(new String[map.keySet().size()]);

        Arrays.sort(array);

        final ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, array);
        holder.getList().setAdapter(adapter);

        final String count = getString(R.string.text_number_of_devices, array.length);
        holder.getCount().setText(count);
    }
}

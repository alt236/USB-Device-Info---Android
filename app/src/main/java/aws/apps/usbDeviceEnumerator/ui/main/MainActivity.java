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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.ui.common.Navigation;
import aws.apps.usbDeviceEnumerator.ui.dbupdate.DatabaseUpdater;
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.InfoFragmentFactory;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbDevice;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbManager;

public class MainActivity extends AppCompatActivity implements OnTabChangeListener {
    final String TAG = this.getClass().getName();

    private ListView mListUsbAndroid;
    private TextView mTvDeviceCountAndroid;

    private ListView mListUsbLinux;
    private TextView mTvDeviceCountLinux;

    private UsbManager mUsbManAndroid;
    private SysBusUsbManager mUsbManagerLinux;

    private DbAccessUsb mDbUsb;
    private DbAccessCompany mDbComp;
    private ZipAccessCompany mZipComp;


    private HashMap<String, UsbDevice> mAndroidUsbDeviceList;
    private HashMap<String, SysBusUsbDevice> mLinuxUsbDeviceList;

    private ProgressDialogControl progressDialogControl;
    private Navigation mNavigation;

    private void initialiseDbComponents() {
        // Prompt user to DL db if it is missing.
        if (!new File(mDbUsb.getLocalDbFullPath()).exists()) {
            DialogFactory.createOkDialog(this,
                    R.string.alert_db_not_found_title,
                    R.string.alert_db_not_found_instructions)
                    .show();
            Log.w(TAG, "^ Database not found: " + mDbUsb.getLocalDbFullPath());
            return;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mNavigation = new Navigation(this);

        mUsbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
        mUsbManagerLinux = new SysBusUsbManager();
        mTvDeviceCountAndroid = (TextView) findViewById(R.id.lbl_devices_api);
        mTvDeviceCountLinux = (TextView) findViewById(R.id.lbl_devices_linux);

        mDbUsb = new DbAccessUsb(this);
        mDbComp = new DbAccessCompany(this);
        mZipComp = new ZipAccessCompany(this);

        mListUsbAndroid = (ListView) findViewById(R.id.usb_list_api);
        mListUsbAndroid.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListUsbAndroid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListUsbAndroid.setItemChecked(position, true);
                mNavigation.showAndroidUsbDeviceInfo(((TextView) view).getText().toString());
            }
        });
        View emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
        ((ViewGroup) mListUsbAndroid.getParent()).addView(emptyView);
        mListUsbAndroid.setEmptyView(emptyView);
        ///
        mListUsbLinux = (ListView) findViewById(R.id.usb_list_linux);
        mListUsbLinux.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListUsbLinux.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListUsbLinux.setItemChecked(position, true);
                mNavigation.showLinuxUsbDeviceInfo(mLinuxUsbDeviceList.get(((TextView) view).getText().toString()));
            }
        });

        emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
        ((ViewGroup) mListUsbLinux.getParent()).addView(emptyView);
        mListUsbLinux.setEmptyView(emptyView);

        final TabSetup tabSetup = new TabSetup(this);
        tabSetup.setup(this);

        initialiseDbComponents();
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

    @Override
    public void onTabChanged(String tabId) {
        if (mNavigation.isSmallScreen()) {
            return;
        }

        final int position;

        final Fragment fragment;

        switch (tabId) {
            case TabSetup.TAB_ANDROID_INFO:
                position = mListUsbAndroid.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    final String text = (String) mListUsbAndroid.getItemAtPosition(position);
                    fragment = InfoFragmentFactory.getFragment(text);
                } else {
                    fragment = InfoFragmentFactory.getFragment("");
                }
                break;
            case TabSetup.TAB_LINUX_INFO:
                position = mListUsbLinux.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    final String text = (String) mListUsbLinux.getItemAtPosition(position);
                    fragment = InfoFragmentFactory.getFragment(mLinuxUsbDeviceList.get(text));
                } else {
                    fragment = InfoFragmentFactory.getFragment("");
                }
                break;
            default:
                fragment = InfoFragmentFactory.getFragment("");
                break;
        }

        mNavigation.stackFragment(fragment);
    }


    private void refreshUsbDevices() {

        // Getting devices from API
        {
            mAndroidUsbDeviceList = mUsbManAndroid.getDeviceList();
            String[] array = mAndroidUsbDeviceList.keySet().toArray(new String[mAndroidUsbDeviceList.keySet().size()]);

            Arrays.sort(array);

            ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, array);
            mListUsbAndroid.setAdapter(adaptor);
            mTvDeviceCountAndroid.setText("Device List (" + mAndroidUsbDeviceList.size() + "):");
        }

        // Getting devices from Linux subsystem
        {
            mLinuxUsbDeviceList = mUsbManagerLinux.getUsbDevices();
            String[] array = mLinuxUsbDeviceList.keySet().toArray(new String[mLinuxUsbDeviceList.keySet().size()]);

            Arrays.sort(array);

            ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, array);
            mListUsbLinux.setAdapter(adaptor);
            mTvDeviceCountLinux.setText("Device List (" + mLinuxUsbDeviceList.size() + "):");
        }
    }

    private View getListViewEmptyView(String text) {
        TextView emptyView = new TextView(getApplicationContext());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        emptyView.setText(text);
        emptyView.setTextSize(20f);
        emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return emptyView;
    }
}

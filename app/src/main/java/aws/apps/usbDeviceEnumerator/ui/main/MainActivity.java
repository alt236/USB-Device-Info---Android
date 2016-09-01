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
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.ui.dbupdate.DatabaseUpdater;
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.AndroidUsbInfoFragment;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.BaseInfoFragment;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.LinuxUsbInfoFragment;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.UsbInfoActivity;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbDevice;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbManager;

public class MainActivity extends AppCompatActivity implements OnTabChangeListener {
    private final static String TAB_ANDROID_INFO = "Android";
    private final static String TAB_LINUX_INFO = "Linux";
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

    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private HashMap<String, UsbDevice> mAndroidUsbDeviceList;
    private HashMap<String, SysBusUsbDevice> mLinuxUsbDeviceList;

    private boolean mIsSmallScreen = true;
    private ProgressDialogControl progressDialogControl;

    private void displayAndroidUsbDeviceInfo(String device) {
        if (mIsSmallScreen) {
            Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, BaseInfoFragment.TYPE_ANDROID_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_ANDROID, device);
            startActivity(i);
        } else {
            stackAFragment(device);
        }
    }

    private void displayLinuxUsbDeviceInfo(SysBusUsbDevice device) {
        if (mIsSmallScreen) {
            Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, BaseInfoFragment.TYPE_LINUX_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_LINUX, device);
            startActivity(i);
        } else {
            stackAFragment(device);
        }
    }

    private View getListViewEmptyView(String text) {
        TextView emptyView = new TextView(getApplicationContext());
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        emptyView.setText(text);
        emptyView.setTextSize(20f);
        emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return emptyView;
    }

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

    private boolean isSmallScreen() {
        Boolean res;
        if (findViewById(R.id.fragment_container) == null) {
            res = true;
        } else {
            res = false;
        }
        Log.d(TAG, "^ Is this device a small screen? " + res);
        return res;
    }

    private TabSpec newTab(String tag, int labelId, int tabContentId) {
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(tag);
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mIsSmallScreen = isSmallScreen();

        mUsbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
        mUsbManagerLinux = new SysBusUsbManager();
        mTvDeviceCountAndroid = (TextView) findViewById(R.id.lbl_devices_api);
        mTvDeviceCountLinux = (TextView) findViewById(R.id.lbl_devices_linux);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        mDbUsb = new DbAccessUsb(this);
        mDbComp = new DbAccessCompany(this);
        mZipComp = new ZipAccessCompany(this);

        mListUsbAndroid = (ListView) findViewById(R.id.usb_list_api);
        mListUsbAndroid.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListUsbAndroid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListUsbAndroid.setItemChecked(position, true);
                displayAndroidUsbDeviceInfo(((TextView) view).getText().toString());
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
                displayLinuxUsbDeviceInfo(mLinuxUsbDeviceList.get(((TextView) view).getText().toString()));
            }
        });

        emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
        ((ViewGroup) mListUsbLinux.getParent()).addView(emptyView);
        mListUsbLinux.setEmptyView(emptyView);

        setupTabs();

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
        if (mIsSmallScreen) {
            return;
        }
        int position = -1;

        if (tabId.equals(TAB_ANDROID_INFO)) {
            position = mListUsbAndroid.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                String text = (String) mListUsbAndroid.getItemAtPosition(position);
                stackAFragment(text);
            } else {
                stackAFragment(new String());
            }
        } else if (tabId.equals(TAB_LINUX_INFO)) {
            position = mListUsbLinux.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                String text = (String) mListUsbLinux.getItemAtPosition(position);
                stackAFragment(mLinuxUsbDeviceList.get(text));
            } else {
                stackAFragment(new String());
            }

        }
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

    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!

        mTabHost.addTab(newTab(TAB_ANDROID_INFO, R.string.label_tab_api, R.id.tab_1));
        mTabHost.addTab(newTab(TAB_LINUX_INFO, R.string.label_tab_linux, R.id.tab_2));

        mTabWidget = mTabHost.getTabWidget();

        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
            final TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(this.getResources().getColorStateList(R.drawable.tab_text_selector));
        }

        mTabHost.setOnTabChangedListener(this);
    }

    private void stackAFragment(String usbKey) {
        Fragment f = new AndroidUsbInfoFragment(usbKey);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commit();
    }

    private void stackAFragment(SysBusUsbDevice usbDevice) {
        Fragment f = new LinuxUsbInfoFragment(usbDevice);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commit();
    }

}

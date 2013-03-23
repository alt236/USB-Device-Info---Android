/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.usbDeviceEnumerator.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.fragments.AbstractUsbDeviceInfoFragment;
import aws.apps.usbDeviceEnumerator.fragments.ProgressDialogFragment;
import aws.apps.usbDeviceEnumerator.fragments.UsbDeviceInfoAndroidFragment;
import aws.apps.usbDeviceEnumerator.fragments.UsbDeviceInfoLinuxFragment;
import aws.apps.usbDeviceEnumerator.myusb.MyUsbDevice;
import aws.apps.usbDeviceEnumerator.myusb.MyUsbManager;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public class MainActivity extends Activity{
	final String TAG =  this.getClass().getName();
	private final String DIALOG_FRAGMENT_TAG = "progress_dialog";
	
	private final static String TAB_ANDROID_INFO = "Android";
	private final static String TAB_LINUX_INFO = "Linux";

	private UsefulBits mUsefulBits;
	
	private ListView mListUsbAndroid;
	private TextView mTvDeviceCountAndroid;

	private ListView mListUsbLinux;
	private TextView mTvDeviceCountLinux;

	private UsbManager mUsbManAndroid;
	private MyUsbManager mUsbManagerLinux;

	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private HashMap<String, UsbDevice> mAndroidUsbDeviceList;
	private HashMap<String, MyUsbDevice> mLinuxUsbDeviceList;	

	private String mUsbDbDirectory = "";
	private String mUsbDbFullPath = "";

	private String mCompanyDbDirectory = "";
	private String mCompanyDbFullPath = "";

	private String mCompanyLogoZipDirectory = "";
	private String mCompanyLogoZipFullPath = "";

	private boolean mIsSmallScreen = true;

	private void dialogFragmentDismiss(String tag){
		Log.d(TAG, "^ Dimissing Fragment : " + tag);
		
		DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag(tag); 
		if (dialog != null) { 
			if(DIALOG_FRAGMENT_TAG.equals(tag)){
				Log.d(TAG, "^ Dimissing Fragment!");
				((ProgressDialogFragment) dialog).dismissAllowingStateLoss();
			} else {
				dialog.dismiss();
			}
		}
	}

	private void dialogFragmentShow(String tag){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag(tag);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    
		DialogFragment newFragment = null;
		if(DIALOG_FRAGMENT_TAG.equals(tag)){
			newFragment	= ProgressDialogFragment.newInstance(getString(R.string.text_downloading_files), null);
		}
		ft.add(newFragment, tag);
        ft.commitAllowingStateLoss();
	}

	private void dialogFragmentUpdate(String tag, String title, Integer progress){
		DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag(tag); 
		if (dialogFragment != null) { 
			if(title != null){
				((ProgressDialogFragment) dialogFragment).setTitle(title);
			}
			if(progress != null){
				((ProgressDialogFragment) dialogFragment).setProgress(progress);
			}
		}
	}
	
	private void displayAndroidUsbDeviceInfo(String device){
		if(mIsSmallScreen){
			Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, AbstractUsbDeviceInfoFragment.TYPE_ANDROID_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_ANDROID, device);
            startActivity(i);
		} else {
			stackAFragment(device);
		}
	}
	
	private void displayLinuxUsbDeviceInfo(MyUsbDevice device){
		if(mIsSmallScreen){
			Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, AbstractUsbDeviceInfoFragment.TYPE_LINUX_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_LINUX, device);
            startActivity(i);
		} else {
			stackAFragment(device);
		}
	}

	private void doDbPathStuff(){
		mUsbDbDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_db_location_usb);
		mUsbDbFullPath = mUsbDbDirectory + getString(R.string.sd_db_name_usb);

		mCompanyDbDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_db_location_company);
		mCompanyDbFullPath = mCompanyDbDirectory + getString(R.string.sd_db_name_company);

		mCompanyLogoZipDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_zip_location_company);
		mCompanyLogoZipFullPath = mCompanyLogoZipDirectory + getString(R.string.sd_zip_name_company);

		// Prompt user to DL db if it is missing.
		if (!new File(mUsbDbFullPath).exists()){
			mUsefulBits.ShowAlert(getString(R.string.alert_db_not_found_title), 
					getString(R.string.alert_db_not_found_instructions), 
					getString(android.R.string.ok));
			Log.e(TAG, "^ Database not found: " + mUsbDbFullPath);
			return;
		}
	}

	private View getListViewEmptyView(String text){
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText(text);
		emptyView.setTextSize(20f);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		return emptyView;
	}

	private boolean isSmallScreen(){
		Boolean res;
		if(findViewById(R.id.fragment_container) == null){
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		mIsSmallScreen = isSmallScreen();
		mUsefulBits = new UsefulBits(this);

		mUsbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
		mUsbManagerLinux = new MyUsbManager();
		mTvDeviceCountAndroid = (TextView) findViewById(R.id.lbl_devices_api);
		mTvDeviceCountLinux = (TextView) findViewById(R.id.lbl_devices_linux);

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
	
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
		((ViewGroup)mListUsbAndroid.getParent()).addView(emptyView);
		mListUsbAndroid.setEmptyView(emptyView);
		///
		mListUsbLinux = (ListView) findViewById(R.id.usb_list_linux);
		mListUsbLinux.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListUsbLinux.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListUsbLinux.setItemChecked(position, true);
				displayLinuxUsbDeviceInfo( mLinuxUsbDeviceList.get(((TextView) view).getText().toString()));
			}
		});

		emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
		((ViewGroup)mListUsbLinux.getParent()).addView(emptyView);
		mListUsbLinux.setEmptyView(emptyView);

		setupTabs();

		doDbPathStuff();
		refreshUsbDevices();
	}

	/** Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	/** Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:			
			mUsefulBits.showAboutDialogue();
			return true;
		case R.id.menu_update_db:
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				Log.d(TAG, "^ SD card not available.");
				mUsefulBits.showToast(getString(R.string.sd_not_available), Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				return true;
			}

			if (!mUsefulBits.createDirectories(mUsbDbDirectory)){return true;}
			if (!mUsefulBits.createDirectories(mCompanyDbDirectory)){return true;}
			if (!mUsefulBits.createDirectories(mCompanyLogoZipDirectory)){return true;}

			if (!mUsefulBits.isOnline()){  // If we are not online, cancel everything
				mUsefulBits.ShowAlert(
						getString(R.string.text_device_offline), 
						getString(R.string.text_device_offline_instructions), 
						getString(android.R.string.ok));
				return true;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.alert_update_db))
			.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {

					String[][] files = {
							{getString(R.string.url_usb_db), 			mUsbDbFullPath},
							{getString(R.string.url_company_db),		mCompanyDbFullPath},
							{getString(R.string.url_company_logo_zip), 	mCompanyLogoZipFullPath}
					};
					new DownloadFile().execute(files);
				}

			})
			.setNegativeButton(getString(android.R.string.no), null).show();
			return true;
		case R.id.menu_refresh:
			refreshUsbDevices();
			return true;
		}

		return false;
	}


	private void refreshUsbDevices(){

		// Getting devices from API
		{
			mAndroidUsbDeviceList = mUsbManAndroid.getDeviceList();
			String[] androidUsbArray = mAndroidUsbDeviceList.keySet().toArray(new String[mAndroidUsbDeviceList.keySet().size()]);

			ArrayAdapter<String> usbDeviceAdaptorAndroid = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, androidUsbArray);
			mListUsbAndroid.setAdapter(usbDeviceAdaptorAndroid);
			mTvDeviceCountAndroid.setText("Device List (" + mAndroidUsbDeviceList.size()+ "):");
		}

		// Getting devices from Linux subsystem
		{
			mLinuxUsbDeviceList = mUsbManagerLinux.getUsbDevices();
			String[] linuxUsbArray = mLinuxUsbDeviceList.keySet().toArray(new String[mLinuxUsbDeviceList.keySet().size()]);

			ArrayAdapter<String> usbDeviceAdaptorLinux = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, linuxUsbArray);
			mListUsbLinux.setAdapter(usbDeviceAdaptorLinux);
			mTvDeviceCountLinux.setText("Device List (" + mLinuxUsbDeviceList.size()+ "):");
		}
	}

	
	private void setupTabs() {
		mTabHost.setup(); // you must call this before adding your tabs!
		
		mTabHost.addTab(newTab(TAB_ANDROID_INFO, R.string.label_tab_api, R.id.tab_1));
		mTabHost.addTab(newTab(TAB_LINUX_INFO, R.string.label_tab_linux, R.id.tab_2));

		mTabWidget = mTabHost.getTabWidget();
		
		for (int i = 0; i < mTabWidget.getChildCount(); i ++){
			final TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);        
			tv.setTextColor(this.getResources().getColorStateList(R.drawable.tab_text_selector));
		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if(mIsSmallScreen){ return; }
				int position = -1;

				if(tabId.equals(TAB_ANDROID_INFO)){
					position = mListUsbAndroid.getCheckedItemPosition();
					if(position != ListView.INVALID_POSITION){
						String text = (String) mListUsbAndroid.getItemAtPosition(position);
						stackAFragment(text);
					}else{
						stackAFragment(new String());
					}
				}
				else if(tabId.equals(TAB_LINUX_INFO)){
					position = mListUsbLinux.getCheckedItemPosition();
					if(position != ListView.INVALID_POSITION){
						String text = (String) mListUsbLinux.getItemAtPosition(position);
						stackAFragment(mLinuxUsbDeviceList.get(text));
					}else{
						stackAFragment(new String());
					}

				}
			}
		});
	}
	
	private void stackAFragment(MyUsbDevice usbDevice) {
		Fragment f = new UsbDeviceInfoLinuxFragment(usbDevice);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.commit();
	}

	private void stackAFragment(String usbKey) {
		Fragment f = new UsbDeviceInfoAndroidFragment(usbKey);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.commit();
	}

	private class DownloadFile extends AsyncTask<String[][], Integer, Boolean>{
		// This is the message which will be shown on the progress bar
		MessageFormat form = new MessageFormat("Downloading file: {0} of {1}...");

		@Override
		protected Boolean doInBackground(String[][]... list) {
			int count;

			URL url;
			String filePath = "";
			URLConnection conection;
			InputStream is;
			OutputStream os;
			Boolean bOK = true;

			String[][] pair = list[0];
			for(int i = 0; i < list[0].length; i++){
				try {
					url = new URL(pair[i][0]);
					filePath = pair[i][1];

					Log.d(TAG, "^ Downloading: " + url);
					Log.d(TAG, "^ To         : " + filePath);

					conection = url.openConnection();
					conection.connect();
					int lenghtOfFile = conection.getContentLength();

					// download the file
					is = new BufferedInputStream(url.openStream());
					os = new FileOutputStream(filePath);

					byte data[] = new byte[1024];

					long total = 0;

					while ((count = is.read(data)) != -1) {
						total += count;
						// The first number is the current file
						// The second is the total number of files to download
						// The third is the current progress
						publishProgress(i+1, list[0].length, (int)(total*100/lenghtOfFile));
						os.write(data, 0, count);
					}

					os.flush();
					os.close();
					is.close();
				} catch (Exception e) {
					Log.e(TAG, "^ Error while downloading.", e);
					bOK = false;
					e.printStackTrace();
				}
			}
			return bOK;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if(result){ // The download is ok.
				Toast.makeText(MainActivity.this, getString(R.string.download_ok), Toast.LENGTH_SHORT).show();
			}else{     // There was an error.
				Toast.makeText(MainActivity.this, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
			}

			dialogFragmentDismiss(DIALOG_FRAGMENT_TAG);
		}

		@Override
		protected void onPreExecute(){
			dialogFragmentShow(DIALOG_FRAGMENT_TAG);
		}
		
		@Override
		public void onProgressUpdate(Integer... args){
			Object[] testArgs = {args[0],args[1]};
			dialogFragmentUpdate(DIALOG_FRAGMENT_TAG, form.format(testArgs), args[2]);
		}
	}
}

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
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
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
import aws.apps.usbDeviceEnumerator.fragments.UsbDeviceInfoAndroidFragment;
import aws.apps.usbDeviceEnumerator.fragments.UsbDeviceInfoLinuxFragment;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbDevice;
import aws.apps.usbDeviceEnumerator.usb.sysbususb.SysBusUsbManager;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public class MainActivity extends Activity{
	final String TAG =  this.getClass().getName();
	final int DIALOGUE_UPDATE_DB = 0;

	private final static String TAB_ANDROID_INFO = "Android";
	private final static String TAB_LINUX_INFO = "Linux";

	private UsefulBits uB;
	private ProgressDialog dlProgressDialog;
	private String usbDbDirectory = "";
	private String usbDbFullPath = "";

	private String companyDbDirectory = "";
	private String companyDbFullPath = "";

	private String companyLogoZipDirectory = "";
	private String companyLogoZipFullPath = "";

	private ListView listUsbAndroid;
	private TextView tvDeviceCountAndroid;

	private ListView listUsbLinux;
	private TextView tvDeviceCountLinux;

	private UsbManager usbManAndroid;
	private SysBusUsbManager usbManagerLinux;

	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private HashMap<String, UsbDevice> androidUsbDeviceList;
	private HashMap<String, SysBusUsbDevice> linuxUsbDeviceList;	

	//private Frag_AbstractUsbDeviceInfo currentInfoFragment;

	private boolean isSmallScreen = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		isSmallScreen = isSmallScreen();
		uB = new UsefulBits(this);

		usbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
		usbManagerLinux = new SysBusUsbManager();
		tvDeviceCountAndroid = (TextView) findViewById(R.id.lbl_devices_api);
		tvDeviceCountLinux = (TextView) findViewById(R.id.lbl_devices_linux);

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
	
		listUsbAndroid = (ListView) findViewById(R.id.usb_list_api);
		listUsbAndroid.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listUsbAndroid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listUsbAndroid.setItemChecked(position, true);

				displayAndroidUsbDeviceInfo(((TextView) view).getText().toString());
			}
		});
		View emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
		((ViewGroup)listUsbAndroid.getParent()).addView(emptyView);
		listUsbAndroid.setEmptyView(emptyView);
		///
		listUsbLinux = (ListView) findViewById(R.id.usb_list_linux);
		listUsbLinux.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listUsbLinux.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listUsbLinux.setItemChecked(position, true);
				displayLinuxUsbDeviceInfo( linuxUsbDeviceList.get(((TextView) view).getText().toString()));
			}
		});

		emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
		((ViewGroup)listUsbLinux.getParent()).addView(emptyView);
		listUsbLinux.setEmptyView(emptyView);

		setupTabs();

		doDbPathStuff();
		refreshUsbDevices();
	}

	private void displayAndroidUsbDeviceInfo(String device){
		if(isSmallScreen){
			Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, AbstractUsbDeviceInfoFragment.TYPE_ANDROID_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_ANDROID, device);
            startActivity(i);
		} else {
			stackAFragment(device);
		}
	}

	private void displayLinuxUsbDeviceInfo(SysBusUsbDevice device){
		if(isSmallScreen){
			Intent i = new Intent(getApplicationContext(), UsbInfoActivity.class);
            i.putExtra(UsbInfoActivity.EXTRA_TYPE, AbstractUsbDeviceInfoFragment.TYPE_LINUX_INFO);
            i.putExtra(UsbInfoActivity.EXTRA_DATA_LINUX, device);
            startActivity(i);
		} else {
			stackAFragment(device);
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
				if(isSmallScreen){ return; }
				int position = -1;

				if(tabId.equals(TAB_ANDROID_INFO)){
					position = listUsbAndroid.getCheckedItemPosition();
					if(position != ListView.INVALID_POSITION){
						String text = (String) listUsbAndroid.getItemAtPosition(position);
						stackAFragment(text);
					}else{
						stackAFragment(new String());
					}
				}
				else if(tabId.equals(TAB_LINUX_INFO)){
					position = listUsbLinux.getCheckedItemPosition();
					if(position != ListView.INVALID_POSITION){
						String text = (String) listUsbLinux.getItemAtPosition(position);
						stackAFragment(linuxUsbDeviceList.get(text));
					}else{
						stackAFragment(new String());
					}

				}
			}
		});
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(tag);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	private View getListViewEmptyView(String text){
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText(text);
		emptyView.setTextSize(20f);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		return emptyView;
	}

	private void doDbPathStuff(){
		usbDbDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_db_location_usb);
		usbDbFullPath = usbDbDirectory + getString(R.string.sd_db_name_usb);

		companyDbDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_db_location_company);
		companyDbFullPath = companyDbDirectory + getString(R.string.sd_db_name_company);

		companyLogoZipDirectory = Environment.getExternalStorageDirectory() + getString(R.string.sd_zip_location_company);
		companyLogoZipFullPath = companyLogoZipDirectory + getString(R.string.sd_zip_name_company);

		// Prompt user to DL db if it is missing.
		if (!new File(usbDbFullPath).exists()){
			uB.ShowAlert(getString(R.string.alert_db_not_found_title), 
					getString(R.string.alert_db_not_found_instructions), 
					getString(android.R.string.ok));
			Log.e(TAG, "^ Database not found: " + usbDbFullPath);
			return;
		}
	}

	private void refreshUsbDevices(){

		// Getting devices from API
		{
			androidUsbDeviceList = usbManAndroid.getDeviceList();
			String[] androidUsbArray = androidUsbDeviceList.keySet().toArray(new String[androidUsbDeviceList.keySet().size()]);

			ArrayAdapter<String> usbDeviceAdaptorAndroid = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, androidUsbArray);
			listUsbAndroid.setAdapter(usbDeviceAdaptorAndroid);
			tvDeviceCountAndroid.setText("Device List (" + androidUsbDeviceList.size()+ "):");
		}

		// Getting devices from Linux subsystem
		{
			linuxUsbDeviceList = usbManagerLinux.getUsbDevices();
			String[] linuxUsbArray = linuxUsbDeviceList.keySet().toArray(new String[linuxUsbDeviceList.keySet().size()]);

			ArrayAdapter<String> usbDeviceAdaptorLinux = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, linuxUsbArray);
			listUsbLinux.setAdapter(usbDeviceAdaptorLinux);
			tvDeviceCountLinux.setText("Device List (" + linuxUsbDeviceList.size()+ "):");
		}
	}

	private void stackAFragment(String usbKey) {
		Fragment f = new UsbDeviceInfoAndroidFragment(usbKey);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.commit();
	}

	private void stackAFragment(SysBusUsbDevice usbDevice) {
		Fragment f = new UsbDeviceInfoLinuxFragment(usbDevice);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.commit();
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
			uB.showAboutDialogue();
			return true;
		case R.id.menu_update_db:
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				Log.d(TAG, "^ SD card not available.");
				uB.showToast(getString(R.string.sd_not_available), Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				return true;
			}

			if (!uB.createDirectories(usbDbDirectory)){return true;}
			if (!uB.createDirectories(companyDbDirectory)){return true;}
			if (!uB.createDirectories(companyLogoZipDirectory)){return true;}

			if (!uB.isOnline()){  // If we are not online, cancel everything
				uB.ShowAlert(
						getString(R.string.text_device_offline), 
						getString(R.string.text_device_offline_instructions), 
						getString(android.R.string.ok));
				return true;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.alert_update_db))
			.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener(){
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showDialog(DIALOGUE_UPDATE_DB);

					ArrayList<Pair<String, String>> downloads = new ArrayList<Pair<String, String>>();
					
					downloads.add(new Pair<String, String>(
							getString(R.string.url_usb_db), 
							usbDbFullPath));
	
					downloads.add(new Pair<String, String>(
							getString(R.string.url_company_db), 
							companyDbFullPath));
					
					downloads.add(new Pair<String, String>(
							getString(R.string.url_company_logo_zip), 
							companyLogoZipFullPath));
					
					new DownloadFile().execute(downloads);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOGUE_UPDATE_DB:
			dlProgressDialog = new ProgressDialog(this);
			dlProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dlProgressDialog.setCancelable(false);
			dlProgressDialog.setTitle(getString(R.string.text_downloading_files));
			dlProgressDialog.setMax(100);

			return dlProgressDialog;
		default:
			return null;
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOGUE_UPDATE_DB:
			dlProgressDialog.setTitle(getString(R.string.text_downloading_files));
			dlProgressDialog.setProgress(0);
		}
	}

	private class DownloadFile extends AsyncTask<ArrayList<Pair<String, String>>, Integer, Boolean>{
		// This is the message which will be shown on the progress bar
		MessageFormat form = new MessageFormat("Downloading file: {0} of {1}...");

		@Override
		protected Boolean doInBackground(ArrayList<Pair<String, String>>... downloadLists ) {
			int count;

			URL url;
			String filePath = "";
			URLConnection conection;
			InputStream is;
			OutputStream os;
			Boolean bOK = true;

			
			ArrayList<Pair<String, String>> downloads = downloadLists[0];
			
			int downloadCounter = 0;
					
			for( Pair<String, String> download : downloads){
				try {
					url = new URL(download.first);
					filePath = download.second;

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
						publishProgress(downloadCounter+1, downloads.size(), (int)(total*100/lenghtOfFile));
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
				
				downloadCounter += 1;
			}
			
			
			
//			String[][] pair = list[0];
//			for(int i = 0; i < list[0].length; i++){
//				
//			}
			return bOK;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if(result){ // The download is ok.
				Toast.makeText(MainActivity.this, getString(R.string.download_ok), Toast.LENGTH_SHORT).show();
			}else{     // There was an error.
				Toast.makeText(MainActivity.this, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
			}

			Log.d(TAG, "^ Dismissing dialogue");
			dismissDialog(DIALOGUE_UPDATE_DB);
		}

		@Override
		public void onProgressUpdate(Integer... args){
			Object[] testArgs = {args[0],args[1]};
			dlProgressDialog.setTitle(form.format(testArgs));
			dlProgressDialog.setProgress(args[2]);
		}
	}
}

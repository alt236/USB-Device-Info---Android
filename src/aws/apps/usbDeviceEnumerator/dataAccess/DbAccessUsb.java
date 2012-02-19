/*******************************************************************************
 * Copyright 2012 Alexandros Schillings
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
package aws.apps.usbDeviceEnumerator.dataAccess;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public class DbAccessUsb {
	private final String TAG =  this.getClass().getName();
	public final static String UNKNOWN_RESULT = "not in database";
	private Context context;
	private UsefulBits uB;

	private String localDbLocation = "";
	private String localDbFullPath = "";

	private SQLiteDatabase db;

	public DbAccessUsb(Context context){
		this.context = context;
		uB = new UsefulBits(context);
		doDbPathStuff();
	}


	public boolean doDBChecks(){
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Log.d(TAG, "^ SD card not available.");
			uB.ShowAlert(context.getString(R.string.sd_error), 
					context.getString(R.string.sd_not_available), 
					context.getString(android.R.string.ok));
			return false;
		}

		if (!new File(localDbFullPath).exists()){
			uB.ShowAlert(context.getString(R.string.alert_db_not_found_title), 
					context.getString(R.string.alert_db_not_found_instructions), 
					context.getString(android.R.string.ok));
			Log.e(TAG, "^ Database not found: " + localDbFullPath);
			return false;
		}

		return true;
	}


	public String getVendor(String VID){
		String result = "";
		Cursor cur = executeQuery(  "usb", 
				new String[]{"vid","vendor_name","did","device_name","ifid","interface_name"}, 
				"vid='" + VID +"' AND did=''", 
				"vid, did, ifid ASC");

		if (cur!= null){
			//Log.d(TAG, "^ getVendor(" + VID + "): " + cur.getCount());
			if(cur.getCount() > 0){
				cur.moveToFirst();
				result = cur.getString(cur.getColumnIndex("vendor_name"));
				cur.close();
				db.close();
			} else {
				result = UNKNOWN_RESULT;
			}
			
			if(!cur.isClosed()){cur.close();}
			if(db.isOpen()){db.close();}
		}

		return tryNull(result, UNKNOWN_RESULT);
	}

	public String getProduct(String VID, String PID){
		String result = "";
		Cursor cur = executeQuery(  "usb", 
				new String[]{"vid","vendor_name","did","device_name","ifid","interface_name"}, 
				"did='" + PID + "' AND vid='" + VID + "'", 
				"vid, did, ifid ASC");

		if (cur!= null){
			//Log.d(TAG, "^ getProduct(" + PID + "): " + cur.getCount());
			if(cur.getCount() > 0){
				cur.moveToFirst();
				result = cur.getString(cur.getColumnIndex("device_name"));
				cur.close();
				db.close();
			} else {
				result = "not in db";
			}
			if(!cur.isClosed()){cur.close();}
			if(db.isOpen()){db.close();}
		}

		return tryNull(result, UNKNOWN_RESULT);
	}

	private Cursor executeQuery(String table, String[] fields, String where, String order){		

		try {
			db = SQLiteDatabase.openDatabase(localDbFullPath, null, SQLiteDatabase.OPEN_READONLY);

			if(!db.isOpen()){
				Log.e(TAG, "^ DB was not opened!");
				uB.showToast(context.getString(R.string.error_could_not_open_db), 
						Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				return null;
			}

			return db.query(table, fields, where, null, null, null, order);
		} catch (Exception e) {
			Log.e(TAG,"^ executeQuery(): " + e.getMessage());
			if(db.isOpen()){db.close();}
		}
		return null;
	}

	private void doDbPathStuff(){
		localDbLocation = Environment.getExternalStorageDirectory() + context.getString(R.string.sd_db_location_usb);
		localDbFullPath = localDbLocation + context.getString(R.string.sd_db_name_usb);
	}


	private String tryNull(String suspect, String defaultString){
		if(suspect == null){
			return defaultString;
		}
		return suspect;
	}
}

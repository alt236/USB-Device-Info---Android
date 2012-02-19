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
package aws.apps.usbDeviceEnumerator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.usbDeviceEnumerator.R;

public class UsefulBits {
	public enum SOFTWARE_INFO{
		NAME, VERSION, NOTES, CHANGELOG, COPYRIGHT, ACKNOWLEDGEMENTS
	}
	
	final String TAG =  this.getClass().getName();
	private Context c;

	public UsefulBits(Context cntx) {
		c = cntx;
	}

	public Calendar convertMillisToDate(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar;
	}

	public boolean createDirectories(String dirs){
		Log.d(TAG, "^ createDirectories - Attempting to create: " + dirs);
		try{

			if (new File(dirs).exists()){
				Log.d(TAG, "^ createDirectories - Directory already exist:" + dirs);
				return true;
			}

			// create a File object for the parent directory
			File newDirectories = new File(dirs);
			// have the object build the directory structure, if needed.
			if(newDirectories.mkdirs()){
				showToast("Directories created: " + dirs, 
						Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				Log.d(TAG, "^ createDirectories - Directory created:" + dirs);
				return true;					
			} else {
				showToast("Could not create: " + dirs, 
						Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				Log.e(TAG, "^ createDirectories - Could not create:" + dirs);
				return false;
			}

		}catch (Exception e){//Catch exception if any
			showToast("Could not create: " + dirs, 
					Toast.LENGTH_SHORT, Gravity.TOP,0,0);
			Log.e(TAG, "^ createDirectories - something went wrong (" + dirs + ") " + e.getMessage());	
			return false;
		}
	}
	
	public String formatDateTime(String formatString, Date d){
		Format formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	} 

	public String getAboutDialogueText(){
		StringBuilder sbText = new StringBuilder();
		
		
		sbText.append(c.getString(R.string.app_changelog));
		sbText.append("\n\n");
		sbText.append(c.getString(R.string.app_notes));
		sbText.append("\n\n");
		sbText.append(c.getString(R.string.app_acknowledgements));
		sbText.append("\n\n");
		sbText.append(c.getString(R.string.app_copyright));
		
		return sbText.toString();
	}
	
	public String getSoftwareInfo(SOFTWARE_INFO info) {
		try {
			PackageInfo pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			Resources appR = c.getResources();
			CharSequence txt;
			// Store the software version code and version name somewhere..
			switch(info){
			case VERSION:
				return pi.versionName;
			case NAME:
				txt = appR.getText(appR.getIdentifier("app_name", "string", c.getPackageName())); 
				break;
			case NOTES:
				txt = appR.getText(appR.getIdentifier("app_notes", "string", c.getPackageName())); 
				break;
			case CHANGELOG:
				txt = appR.getText(appR.getIdentifier("app_changelog", "string", c.getPackageName())); 
				break;
			case COPYRIGHT:
				txt = appR.getText(appR.getIdentifier("app_copyright", "string", c.getPackageName())); 
				break;
			case ACKNOWLEDGEMENTS:
				txt = appR.getText(appR.getIdentifier("app_acknowledgements", "string", c.getPackageName())); 
				break;
			default:
				return "";
			}
			String res = txt.toString();
			res = res.replaceAll("\\t", "");
			res = res.replaceAll("\\n\\n", "\n");

			return res.trim();
		} catch (Exception e) {
			Log.e(TAG, "^ Error @ getSoftwareInfo(" + info.name() + ") ", e);
			return "";
		}
	}

	public boolean isOnline() {
		try{ 
			ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (cm != null) {
				Log.d(TAG, "^ isOnline()=true");
				return cm.getActiveNetworkInfo().isConnected();
			} else {
				Log.d(TAG, "^ isOnline()=false");
				return false;
			}
					
			}catch(Exception e){
				Log.e(TAG, "^ isOnline()=false", e);
				return false;
			}
	}

	public void saveToFile(String fileName, File directory, String contents){

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			try {

				if (directory.canWrite()){
					File gpxfile = new File(directory, fileName);
					FileWriter gpxwriter = new FileWriter(gpxfile);
					BufferedWriter out = new BufferedWriter(gpxwriter);
					out.write(contents);
					out.close();
					showToast("Saved to SD as '" + directory.getAbsolutePath() + "/" + fileName + "'", 
							Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				}

			} catch (Exception e) {
				showToast("Could not write file:\n+ e.getMessage()", 
						Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				Log.e(TAG, "^ Could not write file " + e.getMessage());
			}

		}else{
			showToast("No SD card is mounted...", Toast.LENGTH_SHORT, Gravity.TOP,0,0);
			Log.e(TAG, "^ No SD card is mounted.");		
		}
	}

	public static void share(Context context, String subject, String text){
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		Intent share = Intent.createChooser(intent, context.getString(R.string.share_result_via));
		context.startActivity(share);
	}

	public void ShowAlert(String title, String text, String button){
		if (button.equals("")){button = c.getString(android.R.string.ok);}

		try{
			AlertDialog.Builder ad = new AlertDialog.Builder(c);
			ad.setTitle( title );
			ad.setMessage(text);

			ad.setPositiveButton( button, null );
			ad.show();
		}catch (Exception e){
			Log.e(TAG, "^ ShowAlert()", e);
		}	
	}
	
	public void showToast(String message, int duration, int location, int x_offset, int y_offset){
		Toast toast = Toast.makeText(c.getApplicationContext(), message, duration);
		toast.setGravity(location,x_offset,y_offset);
		toast.show();
	}
	
	public String tableToString(TableLayout t) {
		String res = "";
		if(t==null){return res;}

		for (int i=0; i <= t.getChildCount()-1; i++){
			TableRow row = (TableRow) t.getChildAt(i);

			for (int j=0; j <= row.getChildCount()-1; j++){
				View v = row.getChildAt(j);

				try {
					if(v.getClass() == Class.forName("android.widget.TextView")){
						TextView tmp = (TextView) v;
						res += tmp.getText();

						if(j==0){res += " ";}
					} else if(v.getClass() == Class.forName("android.widget.EditText")){
						EditText tmp = (EditText) v;
						res += tmp.getText().toString();
					} else {
						//do nothing
					}
				} catch (Exception e) {
					res = e.toString();
					Log.e(TAG, "^ ERROR: tableToString: " + res);
				}
			}
			res +="\n";
		}
		return res;
	}
}

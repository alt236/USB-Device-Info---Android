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
package aws.apps.usbDeviceEnumerator.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.myusb.MyUsbDevice;
import aws.apps.usbDeviceEnumerator.util.UsbConstants;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public class UsbDeviceInfoLinuxFragment extends AbstractUsbDeviceInfoFragment {
	private final String TAG =  this.getClass().getName();
	private final static String BUNDLE_MY_USB_INFO = "BUNDLE_MY_USB_INFO";

	public final static int TYPE_ANDROID_INFO = 0;
	public final static int TYPE_LINUX_INFO = 1;	

	public final static String DEFAULT_STRING = "???";
	private TableLayout tblUsbInfoHeader;
	private TableLayout tblUsbInfoTop;
	private TableLayout tblUsbInfoBottom;
	private TextView tvVID;
	private TextView tvPID;
	private TextView tvVendorReported;
	private TextView tvProductReported;
	private TextView tvVendorDb;
	private TextView tvProductDb;		
	private TextView tvDevicePath;
	private TextView tvDeviceClass;
	private ImageButton btnLogo;
	private DbAccessUsb dbUsb;
	private DbAccessCompany dbComp;
	private ZipAccessCompany zipComp;
	private MyUsbDevice myUsbDevice;


	private Context context;
	public UsbDeviceInfoLinuxFragment() {

	}

	public UsbDeviceInfoLinuxFragment(MyUsbDevice myUsbDevice) {
		this.myUsbDevice = myUsbDevice;
	}

	private void addDataRow(LayoutInflater inflater, TableLayout tlb, String cell1Text, String cell2Text){
		TableRow row = (TableRow)inflater.inflate(R.layout.usb_table_row_data, null);
		TextView tv1 = (TextView) row.findViewById(R.id.usb_tablerow_cell1);
		TextView tv2 = (TextView) row.findViewById(R.id.usb_tablerow_cell2);
		tv1.setText(cell1Text);
		tv2.setText(cell2Text);
		tlb.addView(row);
	}

	@Override
	public int getType() {
		return TYPE_LINUX_INFO;
	}

	private void loadLogo(String logo){
		Drawable d = context.getResources().getDrawable(R.drawable.no_image);
		Bitmap b = zipComp.getLogo(logo);

		if(b != null){
			d = new BitmapDrawable(b);
		} else {
			Log.w(TAG, "^ Bitmap is null");
		}

		btnLogo.setImageDrawable(d);
	}


	/**
	 * If we are being created with saved state, restore our state
	 */
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		if (null != saved) {
			myUsbDevice = (MyUsbDevice) saved.getParcelable(BUNDLE_MY_USB_INFO);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {   	
		View v = new LinearLayout(getActivity().getApplicationContext());
		context = getActivity().getApplicationContext();

		if(myUsbDevice == null){
			return v;
		} else {
			v = inflater.inflate(R.layout.usb_info_linux, container, false);
		}

		tblUsbInfoHeader = (TableLayout) v.findViewById(R.id.tblUsbInfo_title);
		tblUsbInfoTop = (TableLayout) v.findViewById(R.id.tblUsbInfo_top);
		tblUsbInfoBottom = (TableLayout) v.findViewById(R.id.tblUsbInfo_bottom);
		tvVID = ((TextView) v.findViewById(R.id.tvVID));
		tvPID = ((TextView) v.findViewById(R.id.tvPID));
		tvProductDb = ((TextView) v.findViewById(R.id.tvProductDb));
		tvVendorDb = ((TextView) v.findViewById(R.id.tvVendorDb));
		tvProductReported = ((TextView) v.findViewById(R.id.tvProductReported));
		tvVendorReported = ((TextView) v.findViewById(R.id.tvVendorReported));
		tvDevicePath = ((TextView) v.findViewById(R.id.tvDevicePath));
		tvDeviceClass = ((TextView) v.findViewById(R.id.tvDeviceClass));
		btnLogo = (ImageButton) v.findViewById(R.id.btnLogo);
		btnLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));
		dbUsb = new DbAccessUsb(context);
		dbComp = new DbAccessCompany(context);
		zipComp = new ZipAccessCompany(context);

		populateLinuxTable(inflater);

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle toSave) {
		toSave.putParcelable(BUNDLE_MY_USB_INFO, myUsbDevice);
	}

	private String padLeft(String string, String padding, int size){
		String pad = "";
		while((pad+string).length() < size){
			pad += padding + pad;
		}
		return pad+string;
	}

	private void populateLinuxTable(LayoutInflater inflater){
		if(myUsbDevice == null){return;}
		tvDevicePath.setText(myUsbDevice.getDevicePath());

		tvVID.setText(padLeft(myUsbDevice.getVID(),"0",4));
		tvPID.setText(padLeft(myUsbDevice.getPID(),"0",4));
		tvDeviceClass.setText(UsbConstants.resolveUsbClass(myUsbDevice.getDeviceClass()));

		tvVendorReported.setText(myUsbDevice.getReportedVendorName());
		tvProductReported.setText(myUsbDevice.getReportedProductName());

		if(dbUsb.doDBChecks()){
			String vid = tvVID.getText().toString();
			String pid = tvPID.getText().toString();
			tvVendorDb.setText(dbUsb.getVendor(vid));
			tvProductDb.setText(dbUsb.getProduct(vid, pid));
		}

		if(dbComp.doDBChecks()){
			String searchFor = "";

			if(tvVendorDb.getText().toString().trim().length() > 0){
				searchFor = tvVendorDb.getText().toString();
			}else{
				searchFor = myUsbDevice.getReportedVendorName();
			}
			Log.d(TAG, "^ Searching for '"+searchFor+"'");
			loadLogo(dbComp.getLogo(searchFor));
		}
		addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.usb_version_),myUsbDevice.getUsbVersion());
		addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.speed_),myUsbDevice.getSpeed());
		addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.protocol_), myUsbDevice.getDeviceProtocol());
		addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.maximum_power_),myUsbDevice.getMaxPower());
		addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.serial_number_),myUsbDevice.getSerialNumber());

		//addHeaderRow(inflater, tblUsbInfo, "Interfaces");

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		UsefulBits uB = new UsefulBits(getActivity());
		sb.append(uB.tableToString(tblUsbInfoHeader));
		sb.append(uB.tableToString(tblUsbInfoTop));
		sb.append("\n");
		sb.append(uB.tableToString(tblUsbInfoBottom));
		return sb.toString();
	}
}

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
package aws.apps.usbDeviceEnumerator.MyUsb;

import android.os.Parcel;
import android.os.Parcelable;

public class MyUsbDevice implements Parcelable {
	private String VID;
	private String PID;
	private String ReportedProductName;
	private String ReportedVendorName;
	private String SerialNumber;
	private String Speed;
	private String DeviceClass;
	private String DeviceProtocol;
	private String MaxPower;
	private String DeviceSubClass;
	private String BusNumber;
	private String DeviceNumber;
	private String UsbVersion;
	private String DevicePath;

	public static final Parcelable.Creator<MyUsbDevice> CREATOR = new Parcelable.Creator<MyUsbDevice>() {
		public MyUsbDevice createFromParcel(Parcel in) {
			return new MyUsbDevice(in);
		}

		public MyUsbDevice[] newArray(int size) {
			return new MyUsbDevice[size];
		}
	};
	
	public MyUsbDevice(){}
	
	public MyUsbDevice(Parcel in) {
		VID = in.readString();
		PID = in.readString();
		ReportedProductName = in.readString();
		ReportedVendorName = in.readString();
		SerialNumber = in.readString();
		Speed = in.readString();
		DeviceClass = in.readString();
		DeviceProtocol = in.readString();
		MaxPower = in.readString();
		DeviceSubClass = in.readString();
		BusNumber = in.readString();
		DeviceNumber = in.readString();
		UsbVersion = in.readString();
		DevicePath = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getBusNumber() {
		return BusNumber;
	}

	public String getDeviceClass() {
		return DeviceClass;
	}

	public String getDeviceNumber() {
		return DeviceNumber;
	}

	public String getDevicePath() {
		return DevicePath;
	}

	public String getDeviceProtocol() {
		return DeviceProtocol;
	}

	public String getDeviceSubClass() {
		return DeviceSubClass;
	}

	public String getMaxPower() {
		return MaxPower;
	}

	public String getPID() {
		return PID;
	}

	public String getReportedProductName() {
		return ReportedProductName;
	}

	public String getReportedVendorName() {
		return ReportedVendorName;
	}

	public String getSerialNumber() {
		return SerialNumber;
	}

	public String getSpeed() {
		return Speed;
	}

	public String getUsbVersion() {
		return UsbVersion;
	}

	public String getVID() {
		return VID;
	}

	public void setBusNumber(String busNumber) {
		BusNumber = busNumber;
	}

	public void setDeviceClass(String deviceClass) {
		DeviceClass = deviceClass;
	}

	public void setDeviceNumber(String deviceNumber) {
		DeviceNumber = deviceNumber;
	}

	public void setDevicePath(String devicePath) {
		DevicePath = devicePath;
	}

	public void setDeviceProtocol(String deviceProtocol) {
		DeviceProtocol = deviceProtocol;
	}

	public void setDeviceSubClass(String deviceSubClass) {
		DeviceSubClass = deviceSubClass;
	}

	public void setMaxPower(String maxPower) {
		MaxPower = maxPower;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public void setReportedProductName(String reportedProductName) {
		ReportedProductName = reportedProductName;
	}

	public void setReportedVendorName(String reportedVendorName) {
		ReportedVendorName = reportedVendorName;
	}

	public void setSerialNumber(String serial) {
		SerialNumber = serial;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public void setUsbVersion(String usbVersion) {
		UsbVersion = usbVersion;
	}

	public void setVID(String vID) {
		VID = vID;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(VID);
		dest.writeString(PID);
		dest.writeString(ReportedProductName);
		dest.writeString(ReportedVendorName);
		dest.writeString(SerialNumber);
		dest.writeString(Speed);
		dest.writeString(DeviceClass);
		dest.writeString(DeviceProtocol);
		dest.writeString(MaxPower);
		dest.writeString(DeviceSubClass);
		dest.writeString(BusNumber);
		dest.writeString(DeviceNumber);
		dest.writeString(UsbVersion);
		dest.writeString(DevicePath);
	}
}

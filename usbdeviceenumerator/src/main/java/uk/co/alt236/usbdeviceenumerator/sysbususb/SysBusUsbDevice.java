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
package uk.co.alt236.usbdeviceenumerator.sysbususb;

import android.os.Parcel;
import android.os.Parcelable;

public class SysBusUsbDevice implements Parcelable {
    public static final Parcelable.Creator<SysBusUsbDevice> CREATOR = new Parcelable.Creator<SysBusUsbDevice>() {
        public SysBusUsbDevice createFromParcel(Parcel in) {
            return new SysBusUsbDevice(in);
        }

        public SysBusUsbDevice[] newArray(int size) {
            return new SysBusUsbDevice[size];
        }
    };
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

    public SysBusUsbDevice() {
    }

    public SysBusUsbDevice(Parcel in) {
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

    public void setBusNumber(String busNumber) {
        BusNumber = busNumber;
    }

    public String getDeviceClass() {
        return DeviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        DeviceClass = deviceClass;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public String getDevicePath() {
        return DevicePath;
    }

    public void setDevicePath(String devicePath) {
        DevicePath = devicePath;
    }

    public String getDeviceProtocol() {
        return DeviceProtocol;
    }

    public void setDeviceProtocol(String deviceProtocol) {
        DeviceProtocol = deviceProtocol;
    }

    public String getDeviceSubClass() {
        return DeviceSubClass;
    }

    public void setDeviceSubClass(String deviceSubClass) {
        DeviceSubClass = deviceSubClass;
    }

    public String getMaxPower() {
        return MaxPower;
    }

    public void setMaxPower(String maxPower) {
        MaxPower = maxPower;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String pID) {
        PID = pID;
    }

    public String getReportedProductName() {
        return ReportedProductName;
    }

    public void setReportedProductName(String reportedProductName) {
        ReportedProductName = reportedProductName;
    }

    public String getReportedVendorName() {
        return ReportedVendorName;
    }

    public void setReportedVendorName(String reportedVendorName) {
        ReportedVendorName = reportedVendorName;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serial) {
        SerialNumber = serial;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getUsbVersion() {
        return UsbVersion;
    }

    public void setUsbVersion(String usbVersion) {
        UsbVersion = usbVersion;
    }

    public String getVID() {
        return VID;
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

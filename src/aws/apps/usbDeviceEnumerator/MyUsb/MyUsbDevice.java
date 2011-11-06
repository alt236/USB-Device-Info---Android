package aws.apps.usbDeviceEnumerator.MyUsb;

import java.io.Serializable;

public class MyUsbDevice implements Serializable{
	private static final long serialVersionUID = 5383159744871594658L;
	
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
	
	public String getVID() {
		return VID;
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
	public String getDeviceClass() {
		return DeviceClass;
	}
	public String getDeviceProtocol() {
		return DeviceProtocol;
	}
	public String getMaxPower() {
		return MaxPower;
	}
	public String getDeviceSubClass() {
		return DeviceSubClass;
	}
	public void setVID(String vID) {
		VID = vID;
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
	public void setDeviceClass(String deviceClass) {
		DeviceClass = deviceClass;
	}
	public void setDeviceProtocol(String deviceProtocol) {
		DeviceProtocol = deviceProtocol;
	}
	public void setMaxPower(String maxPower) {
		MaxPower = maxPower;
	}
	public void setDeviceSubClass(String deviceSubClass) {
		DeviceSubClass = deviceSubClass;
	}
	public String getBusNumber() {
		return BusNumber;
	}
	public String getDeviceNumber() {
		return DeviceNumber;
	}
	public void setBusNumber(String busNumber) {
		BusNumber = busNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		DeviceNumber = deviceNumber;
	}
	public String getUsbVersion() {
		return UsbVersion;
	}
	public void setUsbVersion(String usbVersion) {
		UsbVersion = usbVersion;
	}
	public String getDevicePath() {
		return DevicePath;
	}
	public void setDevicePath(String devicePath) {
		DevicePath = devicePath;
	}
	
}

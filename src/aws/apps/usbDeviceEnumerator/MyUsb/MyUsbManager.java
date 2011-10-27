package aws.apps.usbDeviceEnumerator.MyUsb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import aws.apps.usbDeviceEnumerator.util.ExecTerminal;

public class MyUsbManager {
	//final String TAG =  this.getClass().getName();
	private HashMap<String, MyUsbDevice> myUsbDevices;
	
	private static final String DEVICE_START = "__DEV_START__";
	private static final String DEVICE_END = "__DEV_END__";
	private static final String COMMAND_GET_USB_INFO = "for DEVICE in /sys/bus/usb/devices/*; do " +
			" echo "+ DEVICE_START + ";" +
			" [ -f $DEVICE/idProduct ] && echo PID: $(cat $DEVICE/idProduct);" +
			" [ -f $DEVICE/idVendor ] && echo BUSNUM: $(cat $DEVICE/busnum);" +
			" [ -f $DEVICE/idVendor ] && echo DEVCLASS: $(cat $DEVICE/bDeviceClass);" +
			" [ -f $DEVICE/idVendor ] && echo DEVNUM: $(cat $DEVICE/devnum);" +
			" [ -f $DEVICE/idVendor ] && echo DEVPROTOCOL: $(cat $DEVICE/bDeviceProtocol);" +
			" [ -f $DEVICE/idVendor ] && echo DEVSUBCLASS: $(cat $DEVICE/bDeviceSubClass);" +
			" [ -f $DEVICE/idVendor ] && echo MAXPOWER: $(cat $DEVICE/bMaxPower);" +
			" [ -f $DEVICE/idVendor ] && echo SERIAL: $(cat $DEVICE/serial);" +
			" [ -f $DEVICE/idVendor ] && echo SPEED: $(cat $DEVICE/speed);" +
			" [ -f $DEVICE/idVendor ] && echo VERSION: $(cat $DEVICE/version);" +			
			" [ -f $DEVICE/idVendor ] && echo VID: $(cat $DEVICE/idVendor);" +
			" [ -f $DEVICE/product ] && echo MANUFACTURER: $(cat $DEVICE/manufacturer);"  +
			" [ -f $DEVICE/product ] && echo PRODUCT: $(cat $DEVICE/product);"  +
			" echo "+ DEVICE_END + ";" +
			" done";

	public MyUsbManager(){
		myUsbDevices = new HashMap<String, MyUsbDevice>();
	}

	private void populateList(){
		myUsbDevices.clear();		
		File dir = new File("/sys/bus/usb/devices/");
		if (!dir.isDirectory()){return;}
		MyUsbDevice usb;

		for (File child : dir.listFiles()) {
			
			if (".".equals(child.getName()) || "..".equals(child.getName())) {
				continue;  // Ignore the self and parent aliases.
			}
			
			String parentPath = child.getAbsolutePath() + File.separator;
			
			usb = new MyUsbDevice();
			usb.setDevicePath(parentPath);
			usb.setBusNumber(readFile(parentPath + "busnum"));
			usb.setDeviceClass(readFile(parentPath + "bDeviceClass"));
			usb.setDeviceNumber(readFile(parentPath + "devnum"));
			usb.setDeviceProtocol(readFile(parentPath + "bDeviceProtocol"));
			usb.setDeviceSubClass(readFile(parentPath + "bDeviceSubClass"));
			usb.setMaxPower(readFile(parentPath + "bMaxPower"));
			usb.setPID(readFile(parentPath + "idProduct"));
			usb.setReportedProductName(readFile(parentPath + "product"));
			usb.setReportedVendorName(readFile(parentPath + "manufacturer"));
			usb.setSerialNumber(readFile(parentPath + "serial"));
			usb.setSpeed(readFile(parentPath + "speed"));
			usb.setVID(readFile(parentPath + "idVendor"));
			usb.setUsbVersion(readFile(parentPath + "version"));

			if(usb.getBusNumber().length() > 0 && usb.getDeviceNumber().length() > 0){
//				String key = "/" + pad(usb.getBusNumber(), 3, "0") + 
//						"/" + pad(usb.getDeviceNumber(), 3, "0");
				String key = child.getName();
				myUsbDevices.put(key, usb);
			}

		}
	}

	private String pad(String text, int maxsize, String padding){
		String res = text;
		while(res.length()<maxsize){
			res = padding + res;
		}
		return res;
	}
		
	public HashMap<String, MyUsbDevice> getUsbDevices(){
		populateList();
		return myUsbDevices;
	}

	private static String readFile(String filePath){
		File file = new File(filePath);
		if(!file.exists()){return "";}
		if(file.isDirectory()){return "";}
		
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[1024];
        int numRead=0;
        
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        
        reader.close();
        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		String res = fileData.toString();
		if(res == null){
			res = "";
		}
        return res.trim();
	}

	public static String getUsbInfo(){
		String res = (new ExecTerminal()).exec(COMMAND_GET_USB_INFO);
		
		res = res.replace(DEVICE_START + "\n" + DEVICE_END + "\n", "");
		return res;
	}
}
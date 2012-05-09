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
package aws.apps.usbDeviceEnumerator.usb.sysbususb;

class SysBusUsbConstantsResolver {

	private final static int USB_CLASS_PER_INTERFACE 		= 0x00;
	private final static int USB_CLASS_AUDIO 				= 0x01;
	private final static int USB_CLASS_COMM 				= 0x02;
	private final static int USB_CLASS_HID 					= 0x03;
	private final static int USB_CLASS_PHYSICAL				= 0x05;
	private final static int USB_CLASS_STILL_IMAGE			= 0x06;
	private final static int USB_CLASS_PRINTER				= 0x07;
	private final static int USB_CLASS_MASS_STORAGE			= 0x08;
	private final static int USB_CLASS_HUB					= 0x09;
	private final static int USB_CLASS_CDC_DATA				= 0x0a;
	private final static int USB_CLASS_CSCID 				= 0x0b; /* chip + smart card */
	private final static int USB_CLASS_CONTENT_SEC 			= 0x0d; /* content security */
	private final static int USB_CLASS_VIDEO 				= 0x0e;
	private final static int USB_CLASS_PERSONAL_HEALTH		= 0x0f;
	private final static int USB_CLASS_DIAGNOSTICS			= 0xdc;
	private final static int USB_CLASS_WIRELESS_CONTROLLER  = 0xe0;
	private final static int USB_CLASS_MISC 				= 0xef;
	private final static int USB_CLASS_APP_SPEC 			= 0xfe;
	private final static int USB_CLASS_VENDOR_SPEC 			= 0xff;

	private final static int USB_DIR_OUT = 0;
	private final static int USB_DIR_IN = 128;

	private final static int  USB_ENDPOINT_XFER_CONTROL = 0;
	private final static int  USB_ENDPOINT_XFER_ISOC = 1;
	private final static int  USB_ENDPOINT_XFER_BULK = 2;
	private final static int  USB_ENDPOINT_XFER_INT = 3;

	protected static String resolveUsbClass(int usbClass){
		switch(usbClass){
		case USB_CLASS_PER_INTERFACE:
			return "Use class information in the Interface Descriptors (0x"+Integer.toHexString(usbClass) + ")";		
		case USB_CLASS_AUDIO:
			return "Audio Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_COMM:
			return "Communication Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_HID:
			return "Human Interaction Device (0x"+Integer.toHexString(usbClass) + ")";			
		case USB_CLASS_PHYSICAL:
			return "Physical Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_STILL_IMAGE:
			return "Still Image Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_PRINTER:
			return "Printer (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_MASS_STORAGE:
			return "Mass Storage Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_HUB:
			return "USB Hub (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_CDC_DATA:
			return "Communication Device Class (CDC) (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_CSCID:
			return "Content SmartCard Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_CONTENT_SEC:
			return "Content Security Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_APP_SPEC:
			return "Application Specific (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_VENDOR_SPEC:
			return "Vendor Specific (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_VIDEO:
			return "Video Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_PERSONAL_HEALTH:
			return "Personal Healthcare Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_DIAGNOSTICS:
			return "Diagnostics Device (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_WIRELESS_CONTROLLER:
			return "Wireless Controller (0x"+Integer.toHexString(usbClass) + ")";
		case USB_CLASS_MISC:
			return "Miscellaneous (0x"+Integer.toHexString(usbClass) + ")";
		default:
			return "Unknown (0x"+Integer.toHexString(usbClass) + ")";
		}
	}

	protected static String resolveUsbEndpointDirection(int usbEndpointDirection){

		switch(usbEndpointDirection){
		case USB_DIR_OUT:
			return "Outbound (0x"+Integer.toHexString(usbEndpointDirection) + ")";		
		case USB_DIR_IN:
			return "Inbound (0x"+Integer.toHexString(usbEndpointDirection) + ")";
		default:
			return "Unknown (0x"+Integer.toHexString(usbEndpointDirection) + ")";
		}
	}

	protected static String resolveUsbEndpointType(int usbEndpointType) {

		switch(usbEndpointType){
		case USB_ENDPOINT_XFER_CONTROL:
			return "Control (0x"+Integer.toHexString(usbEndpointType) + ")";		
		case USB_ENDPOINT_XFER_ISOC:
			return "Isochronous (0x"+Integer.toHexString(usbEndpointType) + ")";
		case USB_ENDPOINT_XFER_BULK:
			return "Bulk (0x"+Integer.toHexString(usbEndpointType) + ")";
		case USB_ENDPOINT_XFER_INT:
			return "Intrrupt (0x"+Integer.toHexString(usbEndpointType) + ")";				
		default:
			return "Unknown (0x"+Integer.toHexString(usbEndpointType) + ")";
		}
	}
}

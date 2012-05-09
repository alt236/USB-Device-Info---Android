Description
-----------
Android 3.1 introduced USB host mode which allows the user to plug USB devices to your Android tablet in the same way as a Desktop PC and extend its functionality (if the tablet has the correct drivers of course).

This application will provide information about almost all currently plugged-in USB device (see below for more info about the 'almost').

Information includes:

* The Device class
* The USB device path
* The Vendor ID (VID) and the Product ID (PID).
* A List of all interfaces and their endpoints.

No ads.

Apart from using the build in API it now also parses "/sys/bus/usb/devices/" to display devices which are hidden by the Android OS, although in less detail. 

Notes
-----------

* If you download the accompanying databases, the application will also be able to provide you with additional info such as the Vendor of the device (which is not necessarily the brand!), the vendor's logo and the product name.
* The Vendor ID database is the one used by my "USB VEN/DEV Database" application. Updating in one application will also update the other.
* As this application is using the default Android API some devices might not appear. USB Hubs (and devices connected to them) are invisible event though they seem to function properly, and mice seem to be filtered out of the provided list. I'm looking into ways to fix this.
* Export to file/email and Device Subclass resolution will be added in the next version.
* I wrote this application as an exercise to explore fragments and the new USB host API. Hopefully it will be useful to someone else.
* The database is parsed from: [http://www.linux-usb.org/usb.ids]()

Changelog
-----------
* v0.0.1 First public release\n
* v0.0.2 Added Interface and Endpoint resolution\n
* v0.0.3 Databases can now be downloaded.\n
* v0.0.4 Now also parses /sys/bus/usb/devices/ for those pesky devices android hides. Added export.
* v0.0.5 Added support for small screen devices. Bugfixes and stability improvements.
	
Permission Explanation
-----------
* ACCESS_NETWORK_STATE: Used to check if the device is on-line and using which interface.
* INTERNET: Used to go on-line and download the updated database.
* WRITE_EXTERNAL_STORAGE: Used to write the database to the SD card.
	
Links
-----------
* Market link: [https://market.android.com/details?id=aws.apps.usbDeviceEnumerator]()
* Webpage: [http://aschillings.co.uk/html/usb_device_info.html]()
* Github: [https://github.com/alt236/USB-Device-Info---Android]()

Credits
-----------
Author: [Alexandros Schillings](https://github.com/alt236).

All logos are the property of their respective owners

The code in this project is licensed under the Apache Software License 2.0.

Copyright (c) 2011 Alexandros Schillings.
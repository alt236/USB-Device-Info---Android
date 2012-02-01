package aws.apps.usbDeviceEnumerator;

import android.app.Fragment;

public abstract class Frag_UsbDeviceInfo extends Fragment{
	public final static int TYPE_ANDROID_INFO = 0;
	public final static int TYPE_LINUX_INFO = 1;
	
	public abstract String toString();
	public abstract int getType();
}

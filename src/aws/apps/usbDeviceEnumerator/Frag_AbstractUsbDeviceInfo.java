package aws.apps.usbDeviceEnumerator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import aws.apps.usbDeviceEnumerator.util.UsefulBits;

public abstract class Frag_AbstractUsbDeviceInfo extends Fragment{
	public final static int TYPE_ANDROID_INFO = 0;
	public final static int TYPE_LINUX_INFO = 1;
	
	public abstract String toString();
	public abstract int getType();
	
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setHasOptionsMenu(true);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_export:
				UsefulBits.share(getActivity(), "USB Info", this.toString());
				return true;
		}
		return false;
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.frag_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
}

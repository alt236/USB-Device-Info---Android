package aws.apps.usbDeviceEnumerator.dataAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import aws.apps.usbDeviceEnumerator.R;

public class ZipAccessCompany {
	private final String TAG =  this.getClass().getName();
	private Context context;

	private String localZipLocation = "";
	private String localZipFullPath = "";

	public ZipAccessCompany(Context context){
		this.context = context;
		doPathStuff();
	}

	private void doPathStuff(){
		localZipLocation = Environment.getExternalStorageDirectory() + context.getString(R.string.sd_zip_location_company);
		localZipFullPath = localZipLocation + context.getString(R.string.sd_zip_name_company);
	}

	public Bitmap getLogo(final String logo){
		Log.d(TAG, "^ Getting logo '" + logo + "' from '" + localZipFullPath +"'");
		Bitmap result = null;
		try {
			FileInputStream fis = new FileInputStream(localZipFullPath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = null;

			// Until we find their map, or we run out of options
			if(zis.getNextEntry()!=null) ze = zis.getNextEntry();

			while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equals(logo)) {
                	result = BitmapFactory.decodeStream(zis);
                    break;
                }
            } 
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, "^ Error opening zip file: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "^ Error opening zip file: ", e);
			e.printStackTrace();
		} 
		
		return result;
	}
}

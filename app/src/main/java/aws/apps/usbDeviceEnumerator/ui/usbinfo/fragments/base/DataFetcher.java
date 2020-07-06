package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base;

import android.graphics.Bitmap;
import android.text.TextUtils;

import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyInfo;
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyLogo;
import aws.apps.usbDeviceEnumerator.data.DataProviderUsbInfo;

/*package*/ class DataFetcher {

    private final DataProviderCompanyInfo dbComp;
    private final DataProviderUsbInfo dbUsb;
    private final DataProviderCompanyLogo zipComp;

    public DataFetcher(DataProviderCompanyInfo dbComp, DataProviderUsbInfo dbUsb, DataProviderCompanyLogo zipComp) {
        this.dbComp = dbComp;
        this.dbUsb = dbUsb;
        this.zipComp = zipComp;
    }

    public void fetchData(final String vid,
                          final String pid,
                          final String reportedVendorName,
                          final Callback callback) {

        final Runnable runnable = () -> {
            final String vendorFromDb;
            final String productFromDb;
            final Bitmap bitmap;

            if (dbUsb.isDataAvailable()) {
                vendorFromDb = dbUsb.getVendorName(vid);
                productFromDb = dbUsb.getProductName(vid, pid);

                if (dbComp.isDataAvailable()) {
                    final String searchFor;

                    if (!TextUtils.isEmpty(vendorFromDb)) {
                        searchFor = vendorFromDb;
                    } else {
                        searchFor = reportedVendorName;
                    }

                    final String logo = dbComp.getLogoName(searchFor);
                    bitmap = zipComp.getLogoBitmap(logo);
                } else {
                    bitmap = null;
                }
            } else {
                vendorFromDb = null;
                productFromDb = null;
                bitmap = null;
            }

            callback.onSuccess(vendorFromDb, productFromDb, bitmap);

        };

        final Thread thread = new Thread(runnable);
        thread.start();
    }

    public interface Callback {

        void onSuccess(String vendorFromDb, String productFromDb, Bitmap bitmap);

    }

}

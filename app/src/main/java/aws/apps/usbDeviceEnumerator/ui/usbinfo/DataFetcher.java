package aws.apps.usbDeviceEnumerator.ui.usbinfo;

import android.graphics.Bitmap;
import android.text.TextUtils;

import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;

/*package*/ class DataFetcher {

    private final DbAccessCompany dbComp;
    private final DbAccessUsb dbUsb;
    private final ZipAccessCompany zipComp;

    public DataFetcher(DbAccessCompany dbComp, DbAccessUsb dbUsb, ZipAccessCompany zipComp) {
        this.dbComp = dbComp;
        this.dbUsb = dbUsb;
        this.zipComp = zipComp;
    }

    public void fetchData(final String vid,
                          final String pid,
                          final String reportedVendorName,
                          final Callback callback) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String vendorFromDb;
                final String productFromDb;
                final Bitmap bitmap;

                if (dbUsb.doDBChecks()) {
                    vendorFromDb = dbUsb.getVendor(vid);
                    productFromDb = dbUsb.getProduct(vid, pid);

                    if (dbComp.doDBChecks()) {
                        final String searchFor;

                        if (!TextUtils.isEmpty(vendorFromDb)) {
                            searchFor = vendorFromDb;
                        } else {
                            searchFor = reportedVendorName;
                        }

                        final String logo = dbComp.getLogo(searchFor);
                        bitmap = zipComp.getLogo(logo);
                    } else {
                        bitmap = null;
                    }
                } else {
                    vendorFromDb = null;
                    productFromDb = null;
                    bitmap = null;
                }

                callback.onSuccess(vendorFromDb, productFromDb, bitmap);

            }
        };

        final Thread thread = new Thread(runnable);
        thread.start();
    }

    public interface Callback {

        void onSuccess(String vendorFromDb, String productFromDb, Bitmap bitmap);

    }

}

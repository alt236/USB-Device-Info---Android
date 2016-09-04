package aws.apps.usbDeviceEnumerator.ui.dbupdate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.data.DbAccessCompany;
import aws.apps.usbDeviceEnumerator.data.DbAccessUsb;
import aws.apps.usbDeviceEnumerator.data.ZipAccessCompany;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl;
import aws.apps.usbDeviceEnumerator.util.NetworkUtils;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

public class DatabaseUpdater {
    private static final String TAG = DatabaseUpdater.class.getSimpleName();

    private final ProgressDialogControl progressDialogControl;
    private final DbAccessCompany dbAccessCompany;
    private final DbAccessUsb dbAccessUsb;
    private final ZipAccessCompany zipAccessCompany;

    public DatabaseUpdater(final ProgressDialogControl progressDialogControl,
                           final DbAccessCompany dbAccessCompany,
                           final DbAccessUsb dbAccessUsb,
                           final ZipAccessCompany zipAccessCompany) {

        this.progressDialogControl = progressDialogControl;
        this.dbAccessCompany = dbAccessCompany;
        this.dbAccessUsb = dbAccessUsb;
        this.zipAccessCompany = zipAccessCompany;
    }

    public void start(final Context context) {

        if (validate(context)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final List<FileDownloadTask.Downloadable> downloadables
                    = createDownloadables(context);

            builder.setMessage(R.string.alert_update_db)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FileDownloadTask.Downloadable[] array
                                    = downloadables.toArray(new FileDownloadTask.Downloadable[downloadables.size()]);
                            new FileDownloadTask(context, progressDialogControl).execute(array);
                        }
                    });

            builder.create().show();
        }
    }

    private boolean validate(final Context context) {
        final boolean valid;

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "^ SD card not available.");
            NotifyUser.notify(context, R.string.sd_not_available);
            valid = false;
        } else if (!NetworkUtils.isOnline(context)) {  // If we are not online, cancel everything
            DialogFactory.createOkDialog(
                    context,
                    R.string.text_device_offline,
                    R.string.text_device_offline_instructions)
                    .show();
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    private List<FileDownloadTask.Downloadable> createDownloadables(final Context context) {
        final List<FileDownloadTask.Downloadable> downloads = new ArrayList<FileDownloadTask.Downloadable>();

        downloads.add(new FileDownloadTask.Downloadable(
                dbAccessUsb.getUrl(),
                dbAccessUsb.getFilePath()));

        downloads.add(new FileDownloadTask.Downloadable(
                dbAccessCompany.getUrl(),
                dbAccessCompany.getFilePath()));

        downloads.add(new FileDownloadTask.Downloadable(
                zipAccessCompany.getUrl(),
                zipAccessCompany.getFilePath()));

        return downloads;
    }
}

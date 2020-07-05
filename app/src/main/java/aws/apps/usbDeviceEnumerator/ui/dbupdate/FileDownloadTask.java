package aws.apps.usbDeviceEnumerator.ui.dbupdate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

/*package*/ class FileDownloadTask extends AsyncTask<FileDownloadTask.Downloadable, Integer, Boolean> {
    private static final String TAG = FileDownloadTask.class.getSimpleName();
    private final MessageFormat form = new MessageFormat("Downloading file: {0} of {1}...");
    private final ProgressDialogControl progressDialogControl;
    private final Context context;

    public FileDownloadTask(final Context context,
                            final ProgressDialogControl progressDialogControl) {
        this.progressDialogControl = progressDialogControl;
        this.context = context.getApplicationContext();
    }

    @Override
    protected Boolean doInBackground(Downloadable... downloadables) {
        int count;

        URL url;
        String filePath;
        URLConnection connection;
        InputStream is;
        OutputStream os;
        Boolean bOK = true;

        int downloadCounter = 0;

        for (Downloadable download : downloadables) {
            try {
                url = new URL(download.getFrom());
                filePath = download.getTo();

                Log.d(TAG, "^ Downloading: " + url);
                Log.d(TAG, "^ To         : " + filePath);

                if (createDirStructure(filePath)) {
                    connection = url.openConnection();
                    connection.connect();
                    final int contentLength = connection.getContentLength();

                    // download the file
                    is = new BufferedInputStream(url.openStream());
                    os = new FileOutputStream(filePath);

                    final byte[] data = new byte[1024];

                    long total = 0;

                    while ((count = is.read(data)) != -1) {
                        total += count;
                        // The first number is the current file
                        // The second is the total number of files to download
                        // The third is the current progress
                        publishProgress(downloadCounter + 1, downloadables.length, (int) (total * 100 / contentLength));
                        os.write(data, 0, count);
                    }

                    os.flush();
                    os.close();
                    is.close();
                } else {
                    Log.e(TAG, "^ Failed to create directory structure");
                    bOK = false;
                }
            } catch (IOException e) {
                Log.e(TAG, "^ Error while downloading.", e);
                bOK = false;
                e.printStackTrace();
            }

            downloadCounter += 1;
        }

        return bOK;
    }

    private boolean createDirStructure(final String filePath) {
        final File file = new File(filePath);
        final File parent = file.getParentFile();

        final boolean createFile;

        if (parent.exists()) {
            createFile = false;
        } else {
            createFile = true;
        }

        Log.d(TAG, "^ Need to create path for '" + file.getAbsolutePath() + "'? " + createFile);

        if (createFile) {
            Log.d(TAG, "^ Creating path for: " + file.getAbsolutePath());
            return parent.mkdirs();
        } else {
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) { // The download is ok.
            NotifyUser.notify(context, R.string.download_ok);
        } else {     // There was an error.
            NotifyUser.notify(context, R.string.download_error);
        }

        progressDialogControl.dismiss();
    }

    @Override
    protected void onPreExecute() {
        progressDialogControl.show();
    }

    @Override
    public void onProgressUpdate(Integer... args) {
        Object[] testArgs = {args[0], args[1]};
        progressDialogControl.updateProgress(form.format(testArgs), args[2]);
    }

    public static class Downloadable {
        private final String from;
        private final String to;

        public Downloadable(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}

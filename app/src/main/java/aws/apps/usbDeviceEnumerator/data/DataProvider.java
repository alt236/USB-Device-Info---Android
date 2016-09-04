package aws.apps.usbDeviceEnumerator.data;

public interface DataProvider {
    String getUrl();

    String getDataFilePath();

    boolean isDataAvailable();
}

package aws.apps.usbDeviceEnumerator.util;

public final class StringUtils {

    public static String padLeft(String string,
                                 char padding,
                                 int size) {
        final StringBuilder pad = new StringBuilder();
        while (pad.length() + string.length() < size) {
            pad.append(padding);
        }
        return pad + string;
    }

}

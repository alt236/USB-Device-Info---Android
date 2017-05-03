package uk.co.alt236.usbdeviceenumerator.sysbususb;

import java.util.Locale;

import javax.annotation.Nonnull;

/*package*/ class ShellSysBusDumper {
    private static final String DEVICE_START = "-- DEVICE START--";
    private static final String DEVICE_END = "-- DEVICE END--";
    private static final String INDIVIDUAL_COMMAND = " [ -f $DEVICE/%s ] && echo %s: $(cat $DEVICE/%s);";
    private static final String COMMAND_GET_USB_INFO = "for DEVICE in %s*; do " +
            " echo " + DEVICE_START + ";" +
            " echo PATH: $DEVICE;" +
            "%s" +
            " echo " + DEVICE_END + ";" +
            " done";

    @Nonnull
    public static String getDump(final String usbDevicesPath) {
        final StringBuilder sb = new StringBuilder();
        for (final UsbProperty usbProperty : UsbProperty.values()) {
            sb.append(String.format(Locale.US, INDIVIDUAL_COMMAND,
                    usbProperty.getFileName(),
                    usbProperty.name(),
                    usbProperty.getFileName()));
        }

        final String command = String.format(Locale.US, COMMAND_GET_USB_INFO, usbDevicesPath, sb.toString());
        final String dump = (new ExecTerminal()).exec(command);
        return dump.replace(DEVICE_START + "\n" + DEVICE_END + "\n", "");
    }
}

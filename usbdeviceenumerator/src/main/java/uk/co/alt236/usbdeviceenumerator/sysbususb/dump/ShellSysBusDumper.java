package uk.co.alt236.usbdeviceenumerator.sysbususb.dump;

import java.util.Locale;

import javax.annotation.Nonnull;

import uk.co.alt236.usbdeviceenumerator.sysbususb.Constants;
import uk.co.alt236.usbdeviceenumerator.sysbususb.UsbProperty;

public class ShellSysBusDumper {
    public static final String DEVICE_START = "-- DEVICE START--";
    public static final String DEVICE_END = "-- DEVICE END--";

    private static final String INDIVIDUAL_COMMAND = " [ -f $DEVICE/%s ] && echo %s: $(cat $DEVICE/%s);";
    private static final String COMMAND_GET_USB_INFO = "for DEVICE in %s*; do " +
            " echo " + DEVICE_START + ";" +
            " echo PATH: $DEVICE;" +
            "%s" +
            " echo " + DEVICE_END + ";" +
            " done";

    @Nonnull
    public static String getDump() {
        return getDump(Constants.PATH_SYS_BUS_USB);
    }

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
        return clean(dump);
    }

    private static String clean(final String dump) {
        final String clean = dump.replace(DEVICE_START + "\n" + DEVICE_END + "\n", "");
        final String[] lines = clean.split(System.getProperty("line.separator"));

        final String retVal;
        if (lines.length == 3 && lines[1].endsWith("*")) {
            // This is to avoid the case where we have
            // [DEVICE_START]
            // PATH: /sys/bus/usb/devices/*
            // [DEVICE_END]
            // This happens if we failed to read the directory
            retVal = "";
        } else {
            retVal = clean;
        }

        return retVal;
    }
}

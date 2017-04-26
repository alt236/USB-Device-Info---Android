package uk.co.alt236.usbdeviceenumerator.sysbususb;

import java.util.Locale;

import javax.annotation.Nonnull;

/*package*/ class ShellSysBusDumper {
    private static final String DEVICE_START = "-- DEVICE START--";
    private static final String DEVICE_END = "-- DEVICE END--";
    private static final String COMMAND_GET_USB_INFO = "for DEVICE in %s*; do " +
            " echo " + DEVICE_START + ";" +
            " [ -f $DEVICE/idProduct ] && echo PID: $(cat $DEVICE/idProduct);" +
            " [ -f $DEVICE/idVendor ] && echo BUSNUM: $(cat $DEVICE/busnum);" +
            " [ -f $DEVICE/idVendor ] && echo DEVCLASS: $(cat $DEVICE/bDeviceClass);" +
            " [ -f $DEVICE/idVendor ] && echo DEVNUM: $(cat $DEVICE/devnum);" +
            " [ -f $DEVICE/idVendor ] && echo DEVPROTOCOL: $(cat $DEVICE/bDeviceProtocol);" +
            " [ -f $DEVICE/idVendor ] && echo DEVSUBCLASS: $(cat $DEVICE/bDeviceSubClass);" +
            " [ -f $DEVICE/idVendor ] && echo MAXPOWER: $(cat $DEVICE/bMaxPower);" +
            " [ -f $DEVICE/idVendor ] && echo SERIAL: $(cat $DEVICE/serial);" +
            " [ -f $DEVICE/idVendor ] && echo SPEED: $(cat $DEVICE/speed);" +
            " [ -f $DEVICE/idVendor ] && echo VERSION: $(cat $DEVICE/version);" +
            " [ -f $DEVICE/idVendor ] && echo VID: $(cat $DEVICE/idVendor);" +
            " [ -f $DEVICE/product ] && echo MANUFACTURER: $(cat $DEVICE/manufacturer);" +
            " [ -f $DEVICE/product ] && echo PRODUCT: $(cat $DEVICE/product);" +
            " echo " + DEVICE_END + ";" +
            " done";

    @Nonnull
    public static String getDump(final String usbDevicesPath) {
        final String command = String.format(Locale.US, COMMAND_GET_USB_INFO, usbDevicesPath);
        final String dump = (new ExecTerminal()).exec(command);
        return dump.replace(DEVICE_START + "\n" + DEVICE_END + "\n", "");
    }
}

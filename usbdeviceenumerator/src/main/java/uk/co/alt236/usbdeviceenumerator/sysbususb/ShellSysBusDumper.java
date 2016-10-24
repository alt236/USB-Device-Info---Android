package uk.co.alt236.usbdeviceenumerator.sysbususb;

import javax.annotation.Nonnull;

/*package*/ class ShellSysBusDumper {
    private static final String DEVICE_START = "__DEV_START__";
    private static final String DEVICE_END = "__DEV_END__";
    private static final String COMMAND_GET_USB_INFO = "for DEVICE in /sys/bus/usb/devices/*; do " +
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
    public static String getDump() {
        final String dump = (new ExecTerminal()).exec(COMMAND_GET_USB_INFO);
        return dump.replace(DEVICE_START + "\n" + DEVICE_END + "\n", "");
    }
}

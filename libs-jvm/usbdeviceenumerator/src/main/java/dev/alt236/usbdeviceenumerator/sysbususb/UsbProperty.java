package dev.alt236.usbdeviceenumerator.sysbususb;

public enum UsbProperty {
    PID("idProduct"),
    VID("idVendor"),
    MANUFACTURER("manufacturer"),
    PRODUCT("product"),
    VERSION("version"),
    DEVICE_CLASS("bDeviceClass"),
    DEVICE_SUBCLASS("bDeviceSubClass"),
    DEVICE_NUMBER("devnum"),
    DEVICE_PROTOCOL("bDeviceProtocol"),
    MAX_POWER("bMaxPower"),
    BUS_NUMBER("busnum"),
    SERIAL("serial"),
    SPEED("speed"),
    SUPPORTS_AUTOSUSPEND("supports_autosuspend"),
    AUTHORIZED("authorized"),
    MODALIAS("modalias"),
    ALTERNATIVE_SETTING("bAlternateSetting"),
    NUM_INTERFACES("bNumInterfaces"),
    NUM_ENDPOINTS("bNumEndpoints"),
    INTERFACE("interface"),
    INTERFACE_CLASS("bInterfaceClass"),
    INTERFACE_NUMBER("bInterfaceNumber"),
    INTERFACE_PROTOCOL("bInterfaceProtocol"),
    INTERFACE_SUBCLASS("bInterfaceSubClass");

    private final String fileName;

    UsbProperty(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

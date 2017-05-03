package uk.co.alt236.usbdeviceenumerator.linuxapp;

import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbManager;

public class Main {
    public static void main(String[] args) {
        final SysBusUsbManager manager = new SysBusUsbManager();
        System.out.println(SysBusUsbManager.getUsbInfoViaShell());
    }
}

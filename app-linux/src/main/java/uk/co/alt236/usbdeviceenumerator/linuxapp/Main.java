package uk.co.alt236.usbdeviceenumerator.linuxapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dev.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice;
import dev.alt236.usbdeviceenumerator.sysbususb.SysBusUsbManager;
import dev.alt236.usbdeviceenumerator.sysbususb.dump.ShellSysBusDumper;

public class Main {
    public static void main(String[] args) {
        final SysBusUsbManager manager = new SysBusUsbManager();
        System.out.println(ShellSysBusDumper.getDump());

        final Map<String, SysBusUsbDevice> deviceMap = new SysBusUsbManager().getUsbDevices();
        final List<String> keys = new ArrayList<>(deviceMap.keySet());
        Collections.sort(keys);

        for (final String key : keys) {
            final SysBusUsbDevice device = deviceMap.get(key);
            System.out.println(
                    "Device: " + key +
                            ", PID: " + device.getPid() +
                            ", VID: " + device.getVid() +
                            ", Vendor: '" + device.getReportedVendorName() + "'" +
                            ", Product: '" + device.getReportedProductName() + "'");
        }
    }
}

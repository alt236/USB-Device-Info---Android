/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.co.alt236.usbdeviceenumerator.sysbususb;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class SysBusUsbManager {
    private final HashMap<String, SysBusUsbDevice> myUsbDevices;
    private final SysBusUsbDeviceFactory sysBusUsbDeviceFactory;
    private final Validation validation;
    private final String usbSysPath;

    public SysBusUsbManager() {
        this(Constants.PATH_SYS_BUS_USB);
    }

    public SysBusUsbManager(final String usbSysPath) {
        this.usbSysPath = usbSysPath;
        this.myUsbDevices = new HashMap<>();
        this.sysBusUsbDeviceFactory = new SysBusUsbDeviceFactory();
        this.validation = new Validation();
    }

    @Nonnull
    public Map<String, SysBusUsbDevice> getUsbDevices() {
        populateList(usbSysPath);
        return Collections.unmodifiableMap(myUsbDevices);
    }

    private void populateList(@Nonnull String path) {
        myUsbDevices.clear();

        final File pathAsFile = new File(path);
        final File[] children = validation.getListOfChildren(pathAsFile);

        SysBusUsbDevice usb;
        for (File child : children) {
            if (validation.isValidUsbDeviceCandidate(child)) {
                usb = sysBusUsbDeviceFactory.create(child.getAbsoluteFile());

                if (usb != null) {
                    final String key = child.getName();
                    myUsbDevices.put(key, usb);
                }
            }
        }
    }
}

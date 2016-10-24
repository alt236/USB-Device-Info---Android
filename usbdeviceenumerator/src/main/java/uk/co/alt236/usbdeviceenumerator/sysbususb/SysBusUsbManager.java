/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.usbdeviceenumerator.sysbususb;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class SysBusUsbManager {
    private static final String PATH_SYS_BUS_USB = "/sys/bus/usb/devices/";

    private final HashMap<String, SysBusUsbDevice> myUsbDevices;
    private final SysBusUsbDeviceFactory mSysBusUsbDeviceFactory;

    public SysBusUsbManager() {
        myUsbDevices = new HashMap<>();
        mSysBusUsbDeviceFactory = new SysBusUsbDeviceFactory();
    }

    @Nonnull
    public Map<String, SysBusUsbDevice> getUsbDevices() {
        populateList(PATH_SYS_BUS_USB);
        return Collections.unmodifiableMap(myUsbDevices);
    }

    private void populateList(@Nonnull String path) {
        myUsbDevices.clear();

        final File[] children = getListOfChildren(path);

        SysBusUsbDevice usb;
        for (File child : children) {
            if (isValidUsbDeviceCandidate(child)) {
                usb = mSysBusUsbDeviceFactory.create(child.getAbsoluteFile());

                if (usb != null) {
                    final String key = child.getName();
                    myUsbDevices.put(key, usb);
                }
            }
        }
    }

    private boolean isValidUsbDeviceCandidate(@Nonnull final File file) {
        final boolean retVal;

        if (!file.exists()) {
            retVal = false;
        } else if (!file.isDirectory()) {
            retVal = false;
        } else if (".".equals(file.getName()) || "..".equals(file.getName())) {
            retVal = false;
        } else {
            retVal = true;
        }

        return retVal;
    }

    @Nonnull
    private File[] getListOfChildren(@Nonnull final String path) {
        final File[] retVal;

        final File tmp = new File(path);

        if (tmp.exists()
                && tmp.isDirectory()
                && tmp.listFiles() != null) {
            retVal = tmp.listFiles();
        } else {
            retVal = new File[0];
        }

        return retVal;
    }

    @Nonnull
    public static String getUsbInfoViaShell() {
        return ShellSysBusDumper.getDump();
    }
}

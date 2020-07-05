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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*package*/ class SysBusUsbDeviceFactory {

    @Nullable
    public SysBusUsbDevice create(final File usbDeviceDir) {
        final String devicePath = usbDeviceDir.getAbsolutePath() + File.separator;
        final ContentsReader reader = new ContentsReader(devicePath);

        final String busNumber = reader.read(UsbProperty.BUS_NUMBER);
        final String deviceNumber = reader.read(UsbProperty.DEVICE_NUMBER);

        final SysBusUsbDevice retVal;

        if (!busNumber.isEmpty() && !deviceNumber.isEmpty()) {
            retVal = new SysBusUsbDevice.Builder()
                    .withDevicePath(devicePath)
                    .withBusNumber(busNumber)
                    .withDeviceNumber(deviceNumber)
                    .withServiceClass(reader.read(UsbProperty.DEVICE_CLASS))
                    .withDeviceProtocol(reader.read(UsbProperty.DEVICE_PROTOCOL))
                    .withDeviceSubClass(reader.read(UsbProperty.DEVICE_SUBCLASS))
                    .withMaxPower(reader.read(UsbProperty.MAX_POWER))
                    .withPid(reader.read(UsbProperty.PID))
                    .withReportedProductName(reader.read(UsbProperty.PRODUCT))
                    .withReportedVendorName(reader.read(UsbProperty.MANUFACTURER))
                    .withSerialNumber(reader.read(UsbProperty.SERIAL))
                    .withSpeed(reader.read(UsbProperty.SPEED))
                    .withVid(reader.read(UsbProperty.VID))
                    .withUsbVersion(reader.read(UsbProperty.VERSION))
                    .build();
        } else {
            retVal = null;
        }

        return retVal;
    }


    private static class ContentsReader {

        private final String basePath;

        ContentsReader(final String basePath) {
            this.basePath = basePath;
        }

        @Nonnull
        private String read(final UsbProperty property) {
            final String filePath = basePath + property.getFileName();

            final File file = new File(filePath);
            final StringBuilder fileContents = new StringBuilder(1000);
            final int bufferSize = 1024;

            if (file.exists() && !file.isDirectory()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));

                    char[] buf = new char[bufferSize];
                    int numRead = 0;

                    while ((numRead = reader.read(buf)) != -1) {
                        String readData = String.valueOf(buf, 0, numRead);
                        fileContents.append(readData);
                        buf = new char[bufferSize];
                    }
                } catch (IOException e) {
                    fileContents.setLength(0);
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return fileContents.toString().trim();
        }
    }
}

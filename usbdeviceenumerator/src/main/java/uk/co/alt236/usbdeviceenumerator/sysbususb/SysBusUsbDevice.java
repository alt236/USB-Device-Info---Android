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

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nonnull;

public final class SysBusUsbDevice implements Parcelable {
    public static final Creator<SysBusUsbDevice> CREATOR = new Creator<SysBusUsbDevice>() {
        public SysBusUsbDevice createFromParcel(Parcel in) {
            return new SysBusUsbDevice(in);
        }

        public SysBusUsbDevice[] newArray(int size) {
            return new SysBusUsbDevice[size];
        }
    };
    private final String vid;
    private final String pid;
    private final String reportedProductName;
    private final String reportedVendorName;
    private final String serialNumber;
    private final String speed;
    private final String serviceClass;
    private final String deviceProtocol;
    private final String maxPower;
    private final String deviceSubClass;
    private final String busNumber;
    private final String deviceNumber;
    private final String usbVersion;
    private final String devicePath;

    public SysBusUsbDevice(Parcel in) {
        this.vid = in.readString();
        this.pid = in.readString();
        this.reportedProductName = in.readString();
        this.reportedVendorName = in.readString();
        this.serialNumber = in.readString();
        this.speed = in.readString();
        this.serviceClass = in.readString();
        this.deviceProtocol = in.readString();
        this.maxPower = in.readString();
        this.deviceSubClass = in.readString();
        this.busNumber = in.readString();
        this.deviceNumber = in.readString();
        this.usbVersion = in.readString();
        this.devicePath = in.readString();
    }

    private SysBusUsbDevice(final Builder builder) {
        this.vid = builder.vid;
        this.pid = builder.pid;
        this.reportedProductName = builder.reportedProductName;
        this.reportedVendorName = builder.reportedVendorName;
        this.serialNumber = builder.serialNumber;
        this.speed = builder.speed;
        this.serviceClass = builder.serviceClass;
        this.deviceProtocol = builder.deviceProtocol;
        this.maxPower = builder.maxPower;
        this.deviceSubClass = builder.deviceSubClass;
        this.busNumber = builder.busNumber;
        this.deviceNumber = builder.deviceNumber;
        this.usbVersion = builder.usbVersion;
        this.devicePath = builder.devicePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public String getDeviceProtocol() {
        return deviceProtocol;
    }

    public String getDeviceSubClass() {
        return deviceSubClass;
    }

    public String getMaxPower() {
        return maxPower;
    }

    public String getPid() {
        return pid;
    }

    public String getReportedProductName() {
        return reportedProductName;
    }

    public String getReportedVendorName() {
        return reportedVendorName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSpeed() {
        return speed;
    }

    public String getUsbVersion() {
        return usbVersion;
    }

    public String getVid() {
        return vid;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vid);
        dest.writeString(pid);
        dest.writeString(reportedProductName);
        dest.writeString(reportedVendorName);
        dest.writeString(serialNumber);
        dest.writeString(speed);
        dest.writeString(serviceClass);
        dest.writeString(deviceProtocol);
        dest.writeString(maxPower);
        dest.writeString(deviceSubClass);
        dest.writeString(busNumber);
        dest.writeString(deviceNumber);
        dest.writeString(usbVersion);
        dest.writeString(devicePath);
    }

    public static final class Builder {
        private String vid;
        private String pid;
        private String reportedProductName;
        private String reportedVendorName;
        private String serialNumber;
        private String speed;
        private String serviceClass;
        private String deviceProtocol;
        private String maxPower;
        private String deviceSubClass;
        private String busNumber;
        private String deviceNumber;
        private String usbVersion;
        private String devicePath;

        public Builder() {
        }

        @Nonnull
        public Builder withVid(@Nonnull final String val) {
            vid = val;
            return this;
        }

        @Nonnull
        public Builder withPid(@Nonnull final String val) {
            pid = val;
            return this;
        }

        @Nonnull
        public Builder withReportedProductName(@Nonnull final String val) {
            reportedProductName = val;
            return this;
        }

        @Nonnull
        public Builder withReportedVendorName(@Nonnull final String val) {
            reportedVendorName = val;
            return this;
        }

        @Nonnull
        public Builder withSerialNumber(@Nonnull final String val) {
            serialNumber = val;
            return this;
        }

        @Nonnull
        public Builder withSpeed(@Nonnull final String val) {
            speed = val;
            return this;
        }

        @Nonnull
        public Builder withServiceClass(@Nonnull final String val) {
            serviceClass = val;
            return this;
        }

        @Nonnull
        public Builder withDeviceProtocol(@Nonnull final String val) {
            deviceProtocol = val;
            return this;
        }

        @Nonnull
        public Builder withMaxPower(@Nonnull final String val) {
            maxPower = val;
            return this;
        }

        @Nonnull
        public Builder withDeviceSubClass(@Nonnull final String val) {
            deviceSubClass = val;
            return this;
        }

        @Nonnull
        public Builder withBusNumber(@Nonnull final String val) {
            busNumber = val;
            return this;
        }

        @Nonnull
        public Builder withDeviceNumber(@Nonnull final String val) {
            deviceNumber = val;
            return this;
        }

        @Nonnull
        public Builder withUsbVersion(@Nonnull final String val) {
            usbVersion = val;
            return this;
        }

        @Nonnull
        public Builder withDevicePath(@Nonnull final String val) {
            devicePath = val;
            return this;
        }

        @Nonnull
        public SysBusUsbDevice build() {
            return new SysBusUsbDevice(this);
        }
    }
}

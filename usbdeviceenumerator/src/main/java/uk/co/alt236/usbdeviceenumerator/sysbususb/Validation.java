package uk.co.alt236.usbdeviceenumerator.sysbususb;

import java.io.File;

import javax.annotation.Nonnull;

/*package*/ class Validation {

    public boolean isValidUsbDeviceCandidate(@Nonnull final File file) {
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
    public File[] getListOfChildren(@Nonnull final File path) {
        final File[] retVal;

        if (path.exists()
                && path.isDirectory()
                && path.listFiles() != null) {
            retVal = path.listFiles();
        } else {
            retVal = new File[0];
        }

        return retVal;
    }
}

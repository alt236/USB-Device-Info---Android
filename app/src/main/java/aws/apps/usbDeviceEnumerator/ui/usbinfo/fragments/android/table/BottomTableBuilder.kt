package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android;

import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.view.LayoutInflater;
import android.widget.TableLayout;

import androidx.annotation.StringRes;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.TableWriter;
import aws.apps.usbDeviceEnumerator.util.StringUtils;
import uk.co.alt236.usbdeviceenumerator.UsbConstantResolver;

public class BottomTableBuilder {

    private final LayoutInflater inflater;
    private final Resources resources;

    public BottomTableBuilder(final Resources resources, final LayoutInflater inflater) {
        this.resources = resources;
        this.inflater = inflater;
    }

    void build(TableLayout table, UsbDevice device) {
        final TableWriter tableWriter = new TableWriter(inflater, table);

        UsbInterface iFace;
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            iFace = device.getInterface(i);
            if (iFace != null) {
//                final TableLayout bottomTable = viewHolder.getBottomTable();
                final String usbClass = UsbConstantResolver.resolveUsbClass((iFace.getInterfaceClass()));

                tableWriter.addDataRow(getString(R.string.interface_) + i, "");
                tableWriter.addDataRow(getString(R.string.class_), usbClass);

                if (iFace.getEndpointCount() > 0) {
                    String endpointText;
                    for (int j = 0; j < iFace.getEndpointCount(); j++) {
                        endpointText = getEndpointText(iFace.getEndpoint(j), j);
                        tableWriter.addDataRow(getString(R.string.endpoint_), endpointText);
                    }
                } else {
                    tableWriter.addDataRow("\tEndpoints:", "none");
                }
            }
        }
    }

    private String getEndpointText(final UsbEndpoint endpoint, final int index) {
        final String addressInBinary = StringUtils.padLeft(Integer.toBinaryString(endpoint.getAddress()), '0', 8);
        final String addressInHex = StringUtils.padLeft(Integer.toHexString(endpoint.getAddress()), '0', 2);
        final String attributesInBinary = StringUtils.padLeft(Integer.toBinaryString(endpoint.getAttributes()), '0', 8);

        String endpointText = "#" + index + "\n";
        endpointText += getString(R.string.address_) + "0x" + addressInHex + " (" + addressInBinary + ")\n";
        endpointText += getString(R.string.number_) + endpoint.getEndpointNumber() + "\n";
        endpointText += getString(R.string.direction_) + UsbConstantResolver.resolveUsbEndpointDirection(endpoint.getDirection()) + "\n";
        endpointText += getString(R.string.type_) + UsbConstantResolver.resolveUsbEndpointType(endpoint.getType()) + "\n";
        endpointText += getString(R.string.poll_interval_) + endpoint.getInterval() + "\n";
        endpointText += getString(R.string.max_packet_size_) + endpoint.getMaxPacketSize() + "\n";
        endpointText += getString(R.string.attributes_) + attributesInBinary;

        return endpointText;
    }

    private String getString(@StringRes int id) {
        return resources.getString(id);
    }
}

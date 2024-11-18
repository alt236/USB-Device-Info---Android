package aws.apps.usbDeviceEnumerator.ui.main.list

import android.content.Context
import android.widget.ArrayAdapter
import aws.apps.usbDeviceEnumerator.R

class UsbDeviceListAdapter(
    context: Context,
    data: MutableList<String>
) : ArrayAdapter<String>(
    context,
    R.layout.list_item_usb_device,
    R.id.text,
    data
)
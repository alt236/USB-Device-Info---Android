package aws.apps.usbDeviceEnumerator.ui.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.common.IntExt.formatVidPid
import aws.apps.usbDeviceEnumerator.ui.common.ViewExt.setTextOrHide
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper.ApiConditionalResultMapper
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice


class UsbDeviceListAdapter(
    private val context: Context,
    private val data: MutableList<UsbDevice>,
    private val mapper: ApiConditionalResultMapper,
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount() = data.size

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = getItem(position).hashCode().toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: createView(parent)

        with(view.tag as ViewHolder) {
            val device = getItem(position)
            val title = device.key
            val line1: String
            val line2: String

            when (device) {
                is UsbDevice.AndroidUsb -> {
                    val vid = combineNullableStrings(
                        device.device.vendorId.formatVidPid(),
                        mapper.map(device.device.manufacturerName)
                    )
                    val pid = combineNullableStrings(
                        device.device.productId.formatVidPid(),
                        mapper.map(device.device.productName)
                    )
                    line1 = context.getString(R.string.device_list_vid_template, vid)
                    line2 = context.getString(R.string.device_list_pid_template, pid)
                }

                is UsbDevice.SysUsb -> {
                    line1 = context.getString(R.string.device_list_vid_template, device.device.vid)
                    line2 = context.getString(R.string.device_list_pid_template, device.device.pid)
                }
            }

            this.title.setTextOrHide(title)
            this.line1.setTextOrHide(line1)
            this.line2.setTextOrHide(line2)
        }

        return view
    }

    private fun createView(parent: ViewGroup): View {
        val view = inflater.inflate(LAYOUT_ID, parent, false)

        val holder = ViewHolder(
            title = view.findViewById(R.id.title),
            line1 = view.findViewById(R.id.line1),
            line2 = view.findViewById(R.id.line2),
        )

        view.tag = holder

        return view
    }

    private data class ViewHolder(
        val title: TextView,
        val line1: TextView,
        val line2: TextView
    )

    private companion object {
        val LAYOUT_ID = R.layout.list_item_usb_device
    }

    private fun combineNullableStrings(vararg args: String?): String {
        val nonNull = args.filterNotNull().filter { it.isNotEmpty() }
        return nonNull.joinToString(separator = " | ")
    }

    sealed interface UsbDevice {
        val key: String

        data class AndroidUsb(override val key: String, val device: AndroidUsbDevice) : UsbDevice
        data class SysUsb(override val key: String, val device: SysBusUsbDevice) : UsbDevice
    }
}
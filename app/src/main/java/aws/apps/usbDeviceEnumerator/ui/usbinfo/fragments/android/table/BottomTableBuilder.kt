package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.table

import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.TableLayout
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper.ApiConditionalResultMapper
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.TableWriter
import aws.apps.usbDeviceEnumerator.util.StringUtils
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.androidusbmanager.model.AndroidUsbEndpoint
import uk.co.alt236.androidusbmanager.model.AndroidUsbInterface
import uk.co.alt236.usbdeviceenumerator.UsbConstantResolver

class BottomTableBuilder(
    resources: Resources,
    private val inflater: LayoutInflater
) : TableBuilder(resources) {
    private val configurationTableBuilder = ConfigurationTableBuilder(resources)

    private val resultMapper = ApiConditionalResultMapper(resources)

    fun build(table: TableLayout, device: AndroidUsbDevice) {
        val tableWriter = TableWriter(inflater, table)

        configurationTableBuilder.addConfigurations(tableWriter, device.configurations)
        tableWriter.addEmptyRow()
        addInterfaces(tableWriter, device.interfaces)
    }


    private fun addInterfaces(tableWriter: TableWriter, iFaces: List<AndroidUsbInterface>) {
        for ((index, iFace) in iFaces.withIndex()) {
            val usbClass = UsbConstantResolver.resolveUsbClass((iFace.interfaceClass))
            val subClass = UsbConstantResolver.resolveUsbInterfaceSubClass(iFace.interfaceSubclass)

            tableWriter.addTitleRow(getString(R.string.interface_) + index)
            tableWriter.addDataRow(R.string.id_, iFace.id.toString())
            tableWriter.addDataRow(R.string.name_, resultMapper.map(iFace.name))
            tableWriter.addDataRow(
                R.string.alternate_setting_,
                resultMapper.map(iFace.alternateSetting)
            )
            tableWriter.addDataRow(R.string.class_, usbClass)
            tableWriter.addDataRow(R.string.subclass_, subClass)
            tableWriter.addDataRow(R.string.protocol_, iFace.interfaceProtocol.toString())

            addEndpoints(tableWriter, iFace)
        }
    }

    private fun addEndpoints(tableWriter: TableWriter, iFace: AndroidUsbInterface) {
        val title = R.string.endpoint_
        if (iFace.endpoints.isEmpty()) {
            tableWriter.addDataRow(title, "no endpoints")
        } else {
            var endpointText: String
            for ((index, endpoint) in iFace.endpoints.withIndex()) {
                endpointText = getEndpointText(endpoint, index)
                tableWriter.addDataRow(title, endpointText)
            }
        }
    }

    private fun getEndpointText(endpoint: AndroidUsbEndpoint, index: Int): String {
        val addressInBinary = StringUtils.padLeft(Integer.toBinaryString(endpoint.address), '0', 8)
        val addressInHex = StringUtils.padLeft(Integer.toHexString(endpoint.address), '0', 2)
        val attributesInBinary =
            StringUtils.padLeft(Integer.toBinaryString(endpoint.attributes), '0', 8)

        var endpointText = "#$index\n"
        endpointText += getString(R.string.address_) + "0x" + addressInHex + " (" + addressInBinary + ")\n"
        endpointText += getString(R.string.number_) + endpoint.endpointNumber + "\n"
        endpointText += getString(R.string.direction_) + UsbConstantResolver.resolveUsbEndpointDirection(
            endpoint.direction
        ) + "\n"
        endpointText += getString(R.string.type_) + UsbConstantResolver.resolveUsbEndpointType(
            endpoint.type
        ) + "\n"
        endpointText += getString(R.string.poll_interval_) + endpoint.interval + "\n"
        endpointText += getString(R.string.max_packet_size_) + endpoint.maxPacketSize + "\n"
        endpointText += getString(R.string.attributes_) + attributesInBinary

        return endpointText
    }
}

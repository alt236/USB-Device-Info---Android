package uk.co.alt236.androidusbmanager.model

import android.hardware.usb.UsbEndpoint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AndroidUsbEndpoint(val rawEndpoint: UsbEndpoint) : Parcelable {

    val address: Int get() = rawEndpoint.address
    val attributes: Int get() = rawEndpoint.attributes
    val maxPacketSize: Int get() = rawEndpoint.maxPacketSize
    val interval: Int get() = rawEndpoint.interval
    val direction: Int get() = rawEndpoint.direction
    val type: Int get() = rawEndpoint.type
    val endpointNumber: Int get() = rawEndpoint.endpointNumber

}
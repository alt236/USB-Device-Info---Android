package uk.co.alt236.androidusbmanager.model

import android.hardware.usb.UsbConfiguration
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi

import kotlinx.parcelize.Parcelize

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@Parcelize
data class AndroidUsbConfiguration(
    @Suppress("MemberVisibilityCanBePrivate") val rawConfiguration: UsbConfiguration
) : Parcelable {

    val id: Int get() = rawConfiguration.id
    val name: String? get() = rawConfiguration.name
    val maxPower: Int get() = rawConfiguration.maxPower

    val isSelfPowered get() = rawConfiguration.isSelfPowered
    val isRemoteWakeup get() = rawConfiguration.isRemoteWakeup

    val interfaces: List<AndroidUsbInterface> by lazy {
        val result = ArrayList<AndroidUsbInterface>(rawConfiguration.interfaceCount)
        for (i in 0 until rawConfiguration.interfaceCount) {
            result.add(AndroidUsbInterface(rawConfiguration.getInterface(i)))
        }
        result
    }

}
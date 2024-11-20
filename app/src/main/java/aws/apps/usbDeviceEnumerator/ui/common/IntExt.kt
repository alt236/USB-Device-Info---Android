package aws.apps.usbDeviceEnumerator.ui.common

object IntExt {
    fun Int.toHex() = "%x".format(this)
    fun Int.formatVidPid(addHexPrefix: Boolean = false): String {
        val hex = this.toHex().padStart(4, '0')
        return if (addHexPrefix) {
            "0x$hex"
        } else {
            hex
        }
    }
}
package uk.co.alt236.usbinfo.database.providers

interface DataProvider {
    val url: String

    val dataFilePath: String

    fun isDataAvailable(): Boolean
}

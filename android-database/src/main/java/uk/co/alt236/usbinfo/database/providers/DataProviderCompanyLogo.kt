/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.co.alt236.usbinfo.database.providers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import uk.co.alt236.usbinfo.database.BuildConfig
import uk.co.alt236.usbinfo.database.ext.ClosableExt.closeSafe
import uk.co.alt236.usbinfo.database.ext.ContextExt.getDatabaseRoot
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DataProviderCompanyLogo(context: Context) : AbstractDataProvider(context),
    DataProvider {
    override var dataFilePath: String = ""

    init {
        val baseDir: File = context.getDatabaseRoot()
        this.dataFilePath = File(baseDir, BuildConfig.VENDOR_LOGO_ZIP_FILE_NAME).absolutePath
    }

    override val url: String
        get() = BuildConfig.VENDOR_LOGO_ZIP_URL

    fun getLogoBitmap(logoName: String?): Bitmap? {
        Log.d(logTag, "^ Getting logo '$logoName' from '$dataFilePath'")

        return if (logoName.isNullOrEmpty()) {
            null
        } else {
            getBytesFromZip(logoName)
        }
    }

    private fun getBytesFromZip(logoName: String): Bitmap? {
        var result: Bitmap? = null

        var zipInputStream: ZipInputStream? = null
        try {
            val fis = FileInputStream(dataFilePath)
            zipInputStream = ZipInputStream(fis)
            var ze: ZipEntry?

            // Until we find their map, or we run out of options
            if (zipInputStream.nextEntry != null) ze = zipInputStream.nextEntry

            while ((zipInputStream.nextEntry.also { ze = it }) != null) {
                if (ze!!.name == logoName) {
                    Log.d(logTag, "^ Found it!")
                    result = BitmapFactory.decodeStream(zipInputStream)
                    break
                }
            }

            zipInputStream.closeSafe()
        } catch (e: FileNotFoundException) {
            Log.e(logTag, "^ Error opening zip file: ", e)
        } catch (e: IOException) {
            Log.e(logTag, "^ Error opening zip file: ", e)
        } finally {
            zipInputStream?.closeSafe()
        }

        return result
    }
}

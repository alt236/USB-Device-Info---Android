package aws.apps.usbDeviceEnumerator.di

import android.content.Context
import android.content.res.Resources
import android.hardware.usb.UsbManager
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.DataFetcher
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.alt236.usbdeviceenumerator.sysbususb.SysBusUsbManager
import uk.co.alt236.androidusbmanager.AndroidUsbManager
import uk.co.alt236.usbinfo.database.model.LinuxUsbPath
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyInfo
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyLogo
import uk.co.alt236.usbinfo.database.providers.DataProviderUsbInfo

@Module
@InstallIn(ActivityComponent::class)
object DataModule {

    @Provides
    @Reusable
    fun provideDataProviderUsbInfo(@ApplicationContext context: Context): DataProviderUsbInfo {
        return DataProviderUsbInfo(context)
    }

    @Provides
    @Reusable
    fun provideDataProviderCompanyInfo(@ApplicationContext context: Context): DataProviderCompanyInfo {
        return DataProviderCompanyInfo(context)
    }

    @Provides
    @Reusable
    fun provideDataProviderCompanyLogo(@ApplicationContext context: Context): DataProviderCompanyLogo {
        return DataProviderCompanyLogo(context)
    }

    @Provides
    @Reusable
    fun provideSysBusUsbManager(path: LinuxUsbPath): SysBusUsbManager {
        return SysBusUsbManager(path.path)
    }

    @Provides
    @Reusable
    fun provideAndroidUsbManager(@ApplicationContext context: Context): AndroidUsbManager {
        val usbman = context.getSystemService(Context.USB_SERVICE) as UsbManager?
        return AndroidUsbManager(usbman)
    }

    @Provides
    @Reusable
    fun provideDataFetcher(
        resources: Resources,
        usbInfo: DataProviderUsbInfo,
        companyInfo: DataProviderCompanyInfo,
        logoInfo: DataProviderCompanyLogo,
    ): DataFetcher {
        return DataFetcher(
            resources,
            companyInfo,
            usbInfo,
            logoInfo
        )
    }

}

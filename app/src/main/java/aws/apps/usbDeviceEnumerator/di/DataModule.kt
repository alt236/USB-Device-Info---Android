package aws.apps.usbDeviceEnumerator.di

import android.content.Context
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyInfo
import aws.apps.usbDeviceEnumerator.data.DataProviderCompanyLogo
import aws.apps.usbDeviceEnumerator.data.DataProviderUsbInfo
import aws.apps.usbDeviceEnumerator.data.LinuxUsbPath
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.DataFetcher
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbManager

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
    fun provideDataFetcher(
        usbInfo: DataProviderUsbInfo,
        companyInfo: DataProviderCompanyInfo,
        logoInfo: DataProviderCompanyLogo,
    ): DataFetcher {
        return DataFetcher(
            companyInfo,
            usbInfo,
            logoInfo
        )
    }

}

package aws.apps.usbDeviceEnumerator.di

import aws.apps.usbDeviceEnumerator.data.LinuxUsbPath
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideLinuxUsbPath(): LinuxUsbPath {
        val path = uk.co.alt236.usbdeviceenumerator.sysbususb.Constants.PATH_SYS_BUS_USB
        return LinuxUsbPath(path)
    }

}

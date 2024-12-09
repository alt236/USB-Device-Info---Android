package aws.apps.usbDeviceEnumerator.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.alt236.usbdeviceenumerator.sysbususb.Constants
import uk.co.alt236.usbinfo.database.model.LinuxUsbPath
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideLinuxUsbPath(): LinuxUsbPath {
        val path = Constants.PATH_SYS_BUS_USB
        return LinuxUsbPath(path)
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

}

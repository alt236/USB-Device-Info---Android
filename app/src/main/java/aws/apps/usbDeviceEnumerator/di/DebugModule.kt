package aws.apps.usbDeviceEnumerator.di

import android.content.Context
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.device.DeviceDebugInfoDumper
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.directory.DirectoryDebugInfoDumper
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.directorynative.DirectoryNativeDebugInfoDumper
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.alt236.usbinfo.database.model.LinuxUsbPath

@Module
@InstallIn(ActivityComponent::class)
object DebugModule {

    @Provides
    @Reusable
    fun provideDeviceDebugInfoDumper(
        @ApplicationContext context: Context,
        path: LinuxUsbPath
    ): DeviceDebugInfoDumper {
        return DeviceDebugInfoDumper(context, path.path)
    }

    @Provides
    @Reusable
    fun provideDirectoryDebugInfoDumper(
        @ApplicationContext context: Context,
        path: LinuxUsbPath
    ): DirectoryDebugInfoDumper {
        return DirectoryDebugInfoDumper(context, path.path)
    }

    @Provides
    @Reusable
    fun provideDirectoryNativeDebugInfoDumper(path: LinuxUsbPath): DirectoryNativeDebugInfoDumper {
        return DirectoryNativeDebugInfoDumper(path.path)
    }
}

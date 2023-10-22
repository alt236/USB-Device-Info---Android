package aws.apps.usbDeviceEnumerator.ui.debug.fragments.device

import aws.apps.usbDeviceEnumerator.ui.debug.fragments.BaseDebugFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DeviceDumpFragment : BaseDebugFragment() {

    @Inject
    internal lateinit var dumper: DeviceDebugInfoDumper

    override fun getData(): CharSequence {
        return dumper.dump()
    }
}
package aws.apps.usbDeviceEnumerator.ui.debug.fragments.directorynative

import aws.apps.usbDeviceEnumerator.ui.debug.fragments.BaseDebugFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DirectoryDumpNativeFragment : BaseDebugFragment() {

    @Inject
    internal lateinit var dumper: DirectoryNativeDebugInfoDumper
    override fun getData(): CharSequence {
        return dumper.dump()
    }
}
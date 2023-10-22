package aws.apps.usbDeviceEnumerator.ui.debug.fragments.directory

import aws.apps.usbDeviceEnumerator.ui.debug.fragments.BaseDebugFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DirectoryDumpFragment : BaseDebugFragment() {

    @Inject
    internal lateinit var dumper: DirectoryDebugInfoDumper
    override fun getData(): CharSequence {
        return dumper.dump()
    }
}
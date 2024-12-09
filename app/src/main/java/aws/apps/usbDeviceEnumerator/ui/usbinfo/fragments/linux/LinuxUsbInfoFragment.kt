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
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.linux

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.AndroidUsbInfoFragment.Companion.DEFAULT_STRING
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.BaseInfoFragment
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing.SharePayloadFactory
import aws.apps.usbDeviceEnumerator.util.StringUtils
import dagger.hilt.android.AndroidEntryPoint
import dev.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice
import javax.inject.Inject

@AndroidEntryPoint
class LinuxUsbInfoFragment : BaseInfoFragment() {

    @Inject
    lateinit var sharePayloadFactory: SharePayloadFactory

    @Inject
    lateinit var dataBinder: SysUsbInfoDataBinder

    private var device: SysBusUsbDevice? = null
    private var viewHolder: ViewHolder? = null

    private fun hasValidData() = device != null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View? {
        device = requireArguments().getSerializable(EXTRA_DATA) as SysBusUsbDevice?

        return if (hasValidData()) {
            inflater.inflate(LAYOUT_ID, container, false)
        } else {
            inflater.inflate(R.layout.fragment_error, container, false)
        }
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        if (hasValidData()) {
            viewHolder = ViewHolder(view)
            populateDataTable(LayoutInflater.from(context))
        } else {
            val textView = view.findViewById<TextView>(R.id.errorText)
            textView.setText(R.string.error_loading_device_info_unknown)
        }
    }

    override fun onDestroyView() {
        viewHolder = null
        super.onDestroyView()
    }

    private fun populateDataTable(inflater: LayoutInflater) {
        val device = device!!
        val vid = StringUtils.padLeft(device.vid, '0', 4)
        val pid = StringUtils.padLeft(device.pid, '0', 4)

        dataBinder.bind(inflater, viewHolder!!, device)

        loadAsyncData(viewHolder, vid, pid, device.reportedVendorName)
    }


    override fun getSharePayload(): String {
        return viewHolder?.let {
            sharePayloadFactory.getSharePayload(it)
        } ?: DEFAULT_STRING
    }

    companion object {
        private val EXTRA_DATA = LinuxUsbInfoFragment::class.java.name + ".BUNDLE_DATA"
        private val LAYOUT_ID = R.layout.fragment_usb_info

        fun create(usbDevice: SysBusUsbDevice): Fragment {
            val fragment: Fragment = LinuxUsbInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_DATA, usbDevice)
            fragment.arguments = bundle
            return fragment
        }
    }
}

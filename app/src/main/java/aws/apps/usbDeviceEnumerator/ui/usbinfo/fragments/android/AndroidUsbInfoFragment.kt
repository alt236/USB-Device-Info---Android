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
package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.common.IntExt.formatVidPid
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.BaseInfoFragment
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing.SharePayloadFactory
import dagger.hilt.android.AndroidEntryPoint
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import javax.inject.Inject

@AndroidEntryPoint
class AndroidUsbInfoFragment : BaseInfoFragment() {
    @Inject
    lateinit var binder: AndroidUsbInfoDataBinder

    @Inject
    lateinit var sharePayloadFactory: SharePayloadFactory

    private var viewHolder: ViewHolder? = null
    private var device: AndroidUsbDevice? = null

    private fun hasValidData() = device != null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View {
        device = requireArguments().getParcelable(EXTRA_DATA)

        return if (hasValidData()) {
            inflater.inflate(LAYOUT_ID, container, false)
        } else {
            inflater.inflate(R.layout.fragment_error, container, false)
        }
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        viewHolder = ViewHolder(view)

        if (hasValidData()) {
            viewHolder = ViewHolder(view)
            populateDataTable(LayoutInflater.from(requireContext()))
        } else {
            setErrorState()
        }
    }

    override fun onDestroyView() {
        viewHolder = null
        super.onDestroyView()
    }

    private fun setErrorState() {
        requireView()
            .findViewById<TextView>(R.id.errorText)
            .setText(R.string.error_loading_device_info_unknown)
    }

    private fun populateDataTable(inflater: LayoutInflater) {
        binder.bind(inflater, viewHolder!!, device!!)
        val manufacturerName = device!!.manufacturerName.getValueOrNull()
        loadAsyncData(
            viewHolder,
            device!!.vendorId.formatVidPid(false),
            device!!.productId.formatVidPid(false),
            manufacturerName
        )
    }

    override fun getSharePayload(): String {
        return viewHolder?.let {
            sharePayloadFactory.getSharePayload(it)
        } ?: DEFAULT_STRING
    }

    companion object {
        const val DEFAULT_STRING: String = "???"
        private val EXTRA_DATA = AndroidUsbInfoFragment::class.java.name + ".BUNDLE_DATA"
        private val LAYOUT_ID = R.layout.fragment_usb_info

        fun create(device: AndroidUsbDevice): Fragment {
            val fragment = AndroidUsbInfoFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_DATA, device)
            fragment.arguments = bundle
            return fragment
        }
    }
}

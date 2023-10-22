package aws.apps.usbDeviceEnumerator.ui.debug.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.debug.Reloadable

abstract class BaseDebugFragment : Fragment(), Reloadable {
    private var textView: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saved: Bundle?
    ): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onDestroyView() {
        textView = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        val textView: TextView = view.findViewById(TEXT_VIEW_ID)
        textView.maxLines = Int.MAX_VALUE
        this.textView = textView
    }

    override fun reload() {
        if (isAdded && activity != null && view != null) {
            textView?.text = getData() ?: ""
        }
    }

    abstract fun getData(): CharSequence?

    private companion object {
        private val LAYOUT_ID: Int = R.layout.fragment_monospace_textview
        private const val TEXT_VIEW_ID: Int = android.R.id.content
    }
}
package aws.apps.usbDeviceEnumerator.ui.common

import android.widget.TextView
import androidx.core.view.isVisible

object ViewExt {

    fun TextView.setTextOrHide(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            this.text = ""
            this.isVisible = false
        } else {
            this.text = text
            this.isVisible = true
        }
    }

}
package aws.apps.usbDeviceEnumerator.ui.main.list

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.core.view.children
import androidx.core.view.isVisible

class CheckableRelativeLayout(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context, attrs), Checkable {
    private var isChecked = false

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
        checkCheckbox(isChecked)
    }

    override fun toggle() {
        this.isChecked = !this.isChecked
        checkCheckbox(this.isChecked)
    }

    private fun checkCheckbox(isChecked: Boolean) {
        val checkBox = findCheckbox()
        checkBox.isVisible = isChecked // We only want a single check to be visible
        checkBox.isChecked = isChecked
    }

    private fun findCheckbox(): CompoundButton {
        return children.find { it is CompoundButton } as CompoundButton
    }
}
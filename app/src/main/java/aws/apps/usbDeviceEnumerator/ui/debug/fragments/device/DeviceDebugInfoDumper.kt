package aws.apps.usbDeviceEnumerator.ui.debug.fragments.device

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DebugInfoDumper
import uk.co.alt236.usbdeviceenumerator.sysbususb.dump.ShellSysBusDumper
import java.util.regex.Pattern
import javax.inject.Inject

class DeviceDebugInfoDumper @Inject constructor(
    private val context: Context,
    private val directory: String
) : DebugInfoDumper {

    override fun dump(): CharSequence {
        val color = ContextCompat.getColor(context, R.color.colorAccent)
        val sb = SpannableStringBuilder()
        sb.append("Directory '$directory':\n\n")
        val rawDump = ShellSysBusDumper.getDump(directory)
        if (rawDump.isEmpty()) {
            sb.append("No data.\n\n")
            sb.append(context.getString(R.string.debug_unexpected_result_explanation))
        } else {
            sb.append(rawDump)
            colorize(sb, ShellSysBusDumper.DEVICE_START, color)
            colorize(sb, ShellSysBusDumper.DEVICE_END, color)
        }
        return sb
    }

    private fun colorize(
        sb: SpannableStringBuilder,
        pattern: String,
        @ColorInt color: Int
    ) {
        val p = Pattern.compile(pattern)
        val m = p.matcher(sb.toString())
        while (m.find()) {
            val span: CharacterStyle = ForegroundColorSpan(color)
            sb.setSpan(span, m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
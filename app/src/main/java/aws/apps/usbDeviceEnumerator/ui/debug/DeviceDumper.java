package aws.apps.usbDeviceEnumerator.ui.debug;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aws.apps.usbDeviceEnumerator.R;
import uk.co.alt236.usbdeviceenumerator.sysbususb.dump.ShellSysBusDumper;

/*package*/ class DeviceDumper {
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public static CharSequence getDump(@NonNull final Context context,
                                       @NonNull final String directory) {
        final int color = ContextCompat.getColor(context, R.color.colorAccent);
        final SpannableStringBuilder sb = new SpannableStringBuilder();

        sb.append("Directory '" + directory + "':\n\n");

        final String rawDump = ShellSysBusDumper.getDump(directory);
        if (rawDump.isEmpty()) {
            sb.append("No data.\n\n");
            sb.append(context.getString(R.string.debug_unexpected_result_explanation));
        } else {
            sb.append(rawDump);
            colorize(sb, ShellSysBusDumper.DEVICE_START, color);
            colorize(sb, ShellSysBusDumper.DEVICE_END, color);
        }
        return sb;
    }

    private static void colorize(final SpannableStringBuilder sb,
                                 final String pattern,
                                 @ColorInt final int color) {
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(sb.toString());

        while (m.find()) {
            final CharacterStyle span = new ForegroundColorSpan(color);
            sb.setSpan(span, m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }
}

package aws.apps.usbDeviceEnumerator.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import aws.apps.usbDeviceEnumerator.BuildConfig;
import aws.apps.usbDeviceEnumerator.R;


/*package*/ final class AboutDialogFactory {

    private AboutDialogFactory() {
        // NOOP
    }

    private static String constructAboutText(final Context context) {
        String title = context.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME;

        final StringBuilder sb = new StringBuilder();

        sb.append(context.getString(R.string.app_changelog));
        sb.append("\n\n");
        sb.append(context.getString(R.string.app_notes));
        sb.append("\n\n");
        sb.append(context.getString(R.string.app_acknowledgements));
        sb.append("\n\n");
        sb.append(context.getString(R.string.app_copyright));

        return sb.toString();
    }

    public static Dialog createAboutDialog(final Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null);
        final TextView textView = view.findViewById(R.id.text);

        final SpannableString text = new SpannableString(constructAboutText(context));

        textView.setText(text);
        textView.setAutoLinkMask(Activity.RESULT_OK);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(text, Linkify.ALL);

        final DialogInterface.OnClickListener listener = (dialog, id) -> {
        };

        return new AlertDialog.Builder(context)
                .setTitle(R.string.label_menu_about)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener)
                .setView(view)
                .create();
    }
}
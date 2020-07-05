package aws.apps.usbDeviceEnumerator.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import aws.apps.usbDeviceEnumerator.R;

public class DialogFactory {

    public static Dialog createOkDialog(final Context context, final int title, final int message) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null);
        final TextView textView = view.findViewById(R.id.text);

        textView.setText(message);

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setView(view)
                .create();
    }

}

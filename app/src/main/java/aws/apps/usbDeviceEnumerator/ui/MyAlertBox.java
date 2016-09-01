/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.usbDeviceEnumerator.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyAlertBox {

    public static AlertDialog create(Context context, String text, String title, String button) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(button, null)
                .setView(LinkifyText(context, text))
                .create();
    }

    public static ScrollView LinkifyText(Context context, String message) {
        ScrollView svMessage = new ScrollView(context);
        TextView tvMessage = new TextView(context);

        SpannableString spanText = new SpannableString(message);

        Linkify.addLinks(spanText, Linkify.ALL);
        tvMessage.setText(spanText);
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

        svMessage.setPadding(14, 2, 10, 12);
        svMessage.addView(tvMessage);

        return svMessage;
    }
}

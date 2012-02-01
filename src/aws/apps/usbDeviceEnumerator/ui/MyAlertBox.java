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

		public static ScrollView LinkifyText(Context context, String message) 
		{
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
package aws.apps.usbDeviceEnumerator.ui.progress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String message = getArguments().getString("message");

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(title);

        if (message != null) {
            dialog.setMessage(message);
        }
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void setMessage(String message) {
        ((ProgressDialog) this.getDialog()).setMessage(message);
    }

    public void setTitle(String message) {
        (this.getDialog()).setTitle(message);
    }

    public void setProgress(int progress) {
        ((ProgressDialog) this.getDialog()).setProgress(progress);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    protected static ProgressDialogFragment newInstance(int title, String message) {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }
}

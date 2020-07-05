package aws.apps.usbDeviceEnumerator.ui.progress;

import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import aws.apps.usbDeviceEnumerator.R;

public class ProgressDialogControl {
    private static final String TAG = ProgressDialogControl.class.getSimpleName();
    private static final String DIALOG_FRAGMENT_TAG = "progress_dialog";

    private final FragmentManager fragmentManager;

    public ProgressDialogControl(final FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void dismiss() {
        Log.d(TAG, "^ Dimissing Fragment : " + DIALOG_FRAGMENT_TAG);

        DialogFragment dialog = (DialogFragment) fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG);
        if (dialog != null) {
            Log.d(TAG, "^ Dismissing Fragment!");
            dialog.dismissAllowingStateLoss();
        }
    }

    public void updateProgress(final String title,
                               final Integer progress) {

        DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG);
        if (dialogFragment != null) {
            if (title != null) {
                ((ProgressDialogFragment) dialogFragment).setTitle(title);
            }
            if (progress != null) {
                ((ProgressDialogFragment) dialogFragment).setProgress(progress);
            }
        }
    }

    public void show() {
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        final Fragment prev = fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG);

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        final DialogFragment newFragment
                = ProgressDialogFragment.newInstance(R.string.text_downloading_files, null);

        ft.add(newFragment, DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
    }
}

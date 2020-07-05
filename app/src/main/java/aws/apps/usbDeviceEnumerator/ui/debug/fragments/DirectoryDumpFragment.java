package aws.apps.usbDeviceEnumerator.ui.debug.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.debug.Reloadable;
import aws.apps.usbDeviceEnumerator.util.Constants;

public class DirectoryDumpFragment extends Fragment implements Reloadable {
    private static final int LAYOUT_ID = R.layout.fragment_monospace_textview;

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        return inflater.inflate(LAYOUT_ID, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        textView = view.findViewById(android.R.id.content);
        textView.setMaxLines(Integer.MAX_VALUE);
    }

    @Override
    public void reload() {
        if (isAdded() && getActivity() != null && getView() != null) {
            final String directory = Constants.PATH_SYS_BUS_USB;
            textView.setText(DirectoryDump.getDump(getContext(), directory));
        }
    }
}

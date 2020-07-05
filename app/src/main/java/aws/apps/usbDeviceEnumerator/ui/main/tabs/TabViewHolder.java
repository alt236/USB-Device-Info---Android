package aws.apps.usbDeviceEnumerator.ui.main.tabs;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

public class TabViewHolder {

    private final View rootView;
    private final ListView list;
    private final View empty;
    private final TextView count;

    public TabViewHolder(final View rootView) {
        this.rootView = rootView;

        list = rootView.findViewById(android.R.id.list);
        empty = rootView.findViewById(android.R.id.empty);
        count = rootView.findViewById(R.id.count);

        list.setEmptyView(empty);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public ListView getList() {
        return list;
    }

    public TextView getCount() {
        return count;
    }
}

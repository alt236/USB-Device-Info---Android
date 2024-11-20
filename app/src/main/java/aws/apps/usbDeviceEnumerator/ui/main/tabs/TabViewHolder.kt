package aws.apps.usbDeviceEnumerator.ui.main.tabs

import android.view.View
import android.widget.ListView
import android.widget.TextView
import aws.apps.usbDeviceEnumerator.R

class TabViewHolder(rootView: View) {
    private val empty: View = rootView.findViewById(android.R.id.empty)

    val list: ListView = rootView.findViewById(android.R.id.list)
    val count: TextView = rootView.findViewById(R.id.count)

    init {
        list.emptyView = empty
        list.choiceMode = ListView.CHOICE_MODE_SINGLE
    }
}

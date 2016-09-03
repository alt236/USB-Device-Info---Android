package aws.apps.usbDeviceEnumerator.ui.main;

import android.app.Activity;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import aws.apps.usbDeviceEnumerator.R;

public class TabSetup {
    public final static String TAB_ANDROID_INFO = "tab_android";
    public final static String TAB_LINUX_INFO = "tab_linux";

    private final Activity activity;
    private final TabHost mTabHost;
    private TabWidget mTabWidget;

    public TabSetup(final Activity activity) {
        this.activity = activity;
        this.mTabHost = (TabHost) activity.findViewById(android.R.id.tabhost);
    }

    public void setup(final TabHost.OnTabChangeListener listener) {
        mTabHost.setup(); // you must call this before adding your tabs!

        mTabHost.addTab(newTab(TAB_ANDROID_INFO, R.string.label_tab_api, R.id.tab_1));
        mTabHost.addTab(newTab(TAB_LINUX_INFO, R.string.label_tab_linux, R.id.tab_2));

        mTabWidget = mTabHost.getTabWidget();

        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
            final TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(activity.getResources().getColorStateList(R.drawable.tab_text_selector));
        }

        mTabHost.setOnTabChangedListener(listener);
    }

    private TabHost.TabSpec newTab(String tag, int labelId, int tabContentId) {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(tag);
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }
}

package aws.apps.usbDeviceEnumerator.ui.main.tabs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import aws.apps.usbDeviceEnumerator.R;

public class TabController {
    public final static String TAB_ANDROID_INFO = "tab_android";
    public final static String TAB_LINUX_INFO = "tab_linux";

    private static final int[] TAB_LABELS = {R.string.label_tab_api, R.string.label_tab_linux};
    private static final String[] TAB_TAGS = {TAB_ANDROID_INFO, TAB_LINUX_INFO};

    private final Activity activity;
    private final TabLayout tabLayout;
    private final ViewPager viewPager;

    private TabViewHolder[] tabViewHolders;

    public TabController(final Activity activity) {
        this.activity = activity;
        viewPager = activity.findViewById(R.id.pager);
        tabLayout = activity.findViewById(R.id.tabs);
    }

    public void setup(final OnTabChangeListener listener) {
        // These should really be fragments

        final View[] pages = createPages();
        final TabPagerAdapter adapter = new TabPagerAdapter(activity, pages, TAB_LABELS, TAB_TAGS);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // NOOP
            }

            @Override
            public void onPageSelected(int position) {
                final String tag = adapter.getTabTag(position);
                final TabViewHolder holder = tabViewHolders[position];

                listener.onTabChangeListener(tag, holder);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // NOOP
            }
        });
    }

    private View[] createPages() {
        final View[] views = new View[TAB_TAGS.length];
        tabViewHolders = new TabViewHolder[TAB_TAGS.length];

        for (int i = 0; i < TAB_TAGS.length; i++) {
            views[i] = LayoutInflater.from(activity).inflate(R.layout.tab_device_list, viewPager, false);
            tabViewHolders[i] = new TabViewHolder(views[i]);
        }

        return views;
    }

    public TabViewHolder getHolderForTag(final String tag) {
        for (int i = 0; i < TAB_TAGS.length; i++) {
            if (TAB_TAGS[i].equals(tag)) {
                return tabViewHolders[i];
            }
        }

        return null;
    }

    public interface OnTabChangeListener {
        void onTabChangeListener(final String tag, final TabViewHolder holder);
    }
}

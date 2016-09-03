package aws.apps.usbDeviceEnumerator.ui.main.tabs;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*package*/ class TabPagerAdapter extends PagerAdapter {

    private final Context context;
    private final int[] labels;
    private final String[] tabTags;
    private final View[] pages;

    public TabPagerAdapter(final Context context,
                           final View[] pages,
                           final int[] labels,
                           final String[] tabTags) {
        this.context = context.getApplicationContext();
        this.labels = labels;
        this.tabTags = tabTags;
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        collection.addView(pages[position], position);
        return pages[position];
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((TextView) view);
    }

    @Override
    public int getCount() {
        return labels.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getText(labels[position]);
    }

    public String getTabTag(final int position) {
        return tabTags[position];
    }

}

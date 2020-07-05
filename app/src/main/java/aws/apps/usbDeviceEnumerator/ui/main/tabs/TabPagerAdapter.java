package aws.apps.usbDeviceEnumerator.ui.main.tabs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

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

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        collection.addView(pages[position], 0);
        return pages[position];
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
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

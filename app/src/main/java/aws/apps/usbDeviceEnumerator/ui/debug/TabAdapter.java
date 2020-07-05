package aws.apps.usbDeviceEnumerator.ui.debug;

import android.content.Context;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DeviceDumpFragment;
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DirectoryDumpFragment;
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DirectoryDumpNativeFragment;

/*package*/ class TabAdapter extends FragmentPagerAdapter {
    private static final Class<?>[] FRAGMENT_ARRAY = {
            DirectoryDumpFragment.class,
            DirectoryDumpNativeFragment.class,
            DeviceDumpFragment.class,
    };
    private static final int[] TITLE_ARRAY = {
            R.string.label_tab_directory_dump,
            R.string.label_tab_directory_dump_native,
            R.string.label_tab_device_dump,
    };

    private final Set<Fragment> registeredFragments = new HashSet<>();
    private final Context context;

    public TabAdapter(Context context,
                      FragmentManager fm) {
        super(fm);
        this.context = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return FRAGMENT_ARRAY.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TITLE_ARRAY[position]);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        final Fragment fragment = Fragment.instantiate(context, FRAGMENT_ARRAY[position].getName());
        registeredFragments.add(fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        registeredFragments.remove(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Object fragment = super.instantiateItem(container, position);
        registeredFragments.add((Fragment) fragment);
        return fragment;
    }

    public Set<Fragment> getItems() {
        return registeredFragments;
    }
}
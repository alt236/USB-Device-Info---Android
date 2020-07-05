package aws.apps.usbDeviceEnumerator.ui.debug;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import aws.apps.usbDeviceEnumerator.R;

public class DebugActivity extends AppCompatActivity {
    private static final String TAG = DebugActivity.class.getSimpleName();
    private static final int LAYOUT_ID = R.layout.act_viewpager;
    private static final int MENU_ID = R.menu.debug_menu;

    private TabAdapter tabAdapter;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(LAYOUT_ID);

        final ViewPager viewPager = findViewById(R.id.pager);
        final TabLayout tabLayout = findViewById(R.id.tabs);
        tabAdapter = new TabAdapter(this, getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(MENU_ID, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            final Set<Fragment> fragments = tabAdapter.getItems();
            for (final Fragment fragment : fragments) {
                if (fragment instanceof Reloadable) {
                    Log.d(TAG, "Reloading: " + fragment);
                    ((Reloadable) fragment).reload();
                }
            }
            return true;
        }
        return false;
    }
}

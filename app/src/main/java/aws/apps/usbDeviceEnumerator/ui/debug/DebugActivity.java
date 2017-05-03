package aws.apps.usbDeviceEnumerator.ui.debug;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Set;

import aws.apps.usbDeviceEnumerator.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DebugActivity extends AppCompatActivity {
    private static final String TAG = DebugActivity.class.getSimpleName();
    private static final int LAYOUT_ID = R.layout.act_viewpager;
    private static final int MENU_ID = R.menu.debug_menu;

    @BindView(R.id.tabs)
    protected TabLayout tabLayout;

    @BindView(R.id.pager)
    protected ViewPager viewPager;

    private TabAdapter tabAdapter;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(LAYOUT_ID);
        ButterKnife.bind(this);

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
        switch (item.getItemId()) {
            case R.id.menu_refresh:
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

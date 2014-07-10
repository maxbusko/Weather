package com.test.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.codeslap.persistence.Persistence;
import com.codeslap.persistence.SqlAdapter;
import com.test.weather.fragments.AddFragment;
import com.test.weather.fragments.DetailsFragment;
import com.test.weather.fragments.MainFragment;
import com.test.weather.models.Database;


public class MainActivity extends FragmentActivity implements AddFragment.OnSearchItemClickListener, MainFragment.OnCityItemClickListener {

    private MainFragment mainFragment;
    private DrawerLayout mDrawerLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        AddFragment addFragment = (AddFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);
        addFragment.setUp(mDrawerLayout);


        mainFragment = new MainFragment();

        setFragment(mainFragment);

        if (Utils.isFirstStart(this)) {
            onFirstStart();
        }
    }

    private void setFragment(Fragment fr) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fr)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                fragmentManager.popBackStack();
            }
        }
    }

    private void onFirstStart() {
        Database record = new Database();
        SqlAdapter adapter = Persistence.getAdapter(this);

        record.setCityName("Moscow");
        record.setCityId(524901);
        adapter.store(record);

        record = new Database();
        record.setCityName("Saint Petersburg");
        record.setCityId(498817);
        adapter.store(record);

        Utils.setFirstStart(this);
    }

    @Override
    public void onSearchItemClick() {
        if (mainFragment != null) {
            mainFragment.updateList();
        }
    }

    @Override
    public void onCityItemClick(int cityId) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Bundle b = new Bundle();
        b.putInt("id", cityId);
        Fragment fr = new DetailsFragment();
        fr.setArguments(b);
        setFragment(fr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_city:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.findItem(R.id.add_city) != null){
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
            menu.findItem(R.id.add_city).setVisible(!drawerOpen);
            menu.findItem(R.id.refresh).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}

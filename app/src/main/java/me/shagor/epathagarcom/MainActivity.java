package me.shagor.epathagarcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tab_layout;
    ViewPager view_pager;
    JobCircular jobCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Type casting element id
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        view_pager = (ViewPager) findViewById(R.id.view_pager);

        jobCircular = new JobCircular();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Tab
        tab_layout.setupWithViewPager(view_pager);
        adding_fragment(view_pager);
        view_pager.setOffscreenPageLimit(4);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    //Adding Fragment
    public void adding_fragment(ViewPager vp) {
        Myviewpager mypater = new Myviewpager(getSupportFragmentManager());
        mypater.addFragment(new JobCircular(),"Job Circular");
        mypater.addFragment(new Education(),"Education");
        mypater.addFragment(new Books(),"Books");
        mypater.addFragment(new Result(),"All Result");

        vp.setAdapter(mypater);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_share).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dev) {
            Intent intent = new Intent(getApplicationContext(),Aboutus.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.edu_update) {
            showTitle("16","Education Update");
        }else if (id == R.id.books) {
            showTitle("17","Books");
        } else if (id == R.id.result) {
            showTitle("41","Result");
        } else if (id == R.id.job_circular) {
            showTitle("66","Job Circular");
        } else if (id == R.id.nav_tutorial) {
            showTitle("15","Tutorial");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Tab layout class
    class Myviewpager extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        List<String> fragmentName = new ArrayList<String>();

        public Myviewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment_list, String fragment_name) {
            fragmentList.add(fragment_list);
            fragmentName.add(fragment_name);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentName.get(position);
        }
    }

    // Intent set data catagories
    public void showTitle(String catId,String appName){
        Intent intent = new Intent(getApplication(), Category.class);
        intent.putExtra("catId", catId);
        intent.putExtra("app_name", appName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

}

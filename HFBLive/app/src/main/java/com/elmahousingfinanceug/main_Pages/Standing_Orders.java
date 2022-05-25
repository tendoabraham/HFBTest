package com.elmahousingfinanceug.main_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.main_Pages.Fragments.CreateStandingOrdersFr;
import com.elmahousingfinanceug.main_Pages.Fragments.StopStandingOrdersFr;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Standing_Orders extends BaseAct implements ResponseListener, VolleyResponse {
    private ViewPager viewPager;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    public String quest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standing_orders);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    setTitle(getString(R.string.createSOdr));
                }
                else if (tab.getPosition()==1){
                    setTitle(getString(R.string.stopSOdr));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    setTitle(getString(R.string.createSOdr));
                }
                else if (tab.getPosition()==1){
                    setTitle(getString(R.string.stopSOdr));
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    setTitle(getString(R.string.createSOdr));
                }
                else if (tab.getPosition()==1){
                    setTitle(getString(R.string.stopSOdr));
                }
            }
        });

        setTitle(getString(R.string.createSOdr));

        int[] imageResId = {R.drawable.contract_so, R.drawable.stop_so};
        for (int i = 0; i < imageResId.length; i++) {
            //noinspection ConstantConditions
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        tabLayout.setSmoothScrollingEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFrag(new CreateStandingOrdersFr());
        adapter.addFrag(new StopStandingOrdersFr());
        viewPager.setAdapter(adapter);
    }

    public void error() {
        Fragment stopFragment = adapter.getItem(1);
        ((StopStandingOrdersFr)stopFragment).accInfo.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu) {
            finish();
            startActivity(new Intent(getApplicationContext(), Main_Menu.class));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), Contact_Us.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(String response, String step) {
        if ("W".equals(step)) {
            Fragment stopFragment = adapter.getItem(1);
            ((StopStandingOrdersFr) stopFragment).show(response);
        } else {
            finish();
            startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
        }
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}

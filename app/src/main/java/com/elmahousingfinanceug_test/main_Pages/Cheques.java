package com.elmahousingfinanceug_test.main_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Fragments.ChequeRequest_Fragment;
import com.elmahousingfinanceug_test.main_Pages.Fragments.ChequeStop_Fragment;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Cheques extends BaseAct {
    TabLayout tab_layout;
    ViewPager viewpager;
    public String quest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheques);

        gToolBar(getString(R.string.cheques));

        tab_layout = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.viewpager);
        setupViewPager(viewpager);
        tab_layout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ChequeRequest_Fragment(), getString(R.string.request));
        adapter.addFrag(new ChequeStop_Fragment(), getString(R.string.stop));
        viewPager.setAdapter(adapter);
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

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

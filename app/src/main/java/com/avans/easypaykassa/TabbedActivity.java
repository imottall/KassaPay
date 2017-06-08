package com.avans.easypaykassa;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.DomainModel.Product;

import java.util.ArrayList;

public class TabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView totalProductsView, totalPriceView, category;
    private ArrayList<Product> products;
    protected static ProductAdapter adapter;
    public static final String PRODUCTS = "products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_drink_white_24dp);
        //ProductAdapter product_adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), productList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    DrinksTab tab1 = new DrinksTab();
                    //tab1.setProductAdapter(product_adapter);
                    return tab1;
                case 1:
                    FoodTab tab2 = new FoodTab();
                    //tab2.setFoodAdapter(food_adapter);
                    return tab2;
                case 2:
                    SodaTab tab3 = new SodaTab();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Drinken";
                case 1:
                    return "Eten";
                case 2:
                    return "Frisdrank";
            }
            return null;
        }
    }
}

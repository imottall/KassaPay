package com.avans.easypaykassa;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import com.avans.easypaykassa.DomainModel.Product;
import java.util.ArrayList;

public class TabbedRemoveProductsActivity extends AppCompatActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private ArrayList<ArrayList<Product>> products;
    private ArrayList<Product> productList;
    protected static DeleteProductAdapter adapter;
//    private final ProductsTotal.OnTotalChanged totalListener = this;
    private DeleteProductAdapter product_adapter;

    private Button btn_confirm, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabbed_alter_amounts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        mSectionsPagerAdapter = new TabbedRemoveProductsActivity.SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_drink_white_24dp);
        product_adapter = new DeleteProductAdapter(getApplicationContext(), getLayoutInflater(), productList);
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
                    DrinksDeleteTab tab1 = new DrinksDeleteTab();
                    //tab1.setProductAdapter(product_adapter);
                    return tab1;
                case 1:
                    FoodDeleteTab tab2 = new FoodDeleteTab();
                    //tab2.setFoodAdapter(food_adapter);
                    return tab2;
                case 2:
                    SodaDeleteTab tab3 = new SodaDeleteTab();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alter_amount_confirm:
                finish();
                break;

            case R.id.alter_amount_cancel:
                finish();
                break;

        }

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.product_delete_checkbox:
                if (checked);
                // Put the product on the list for deletion
                break;
            default:
                //do nothin
                break;

        }
    }
}

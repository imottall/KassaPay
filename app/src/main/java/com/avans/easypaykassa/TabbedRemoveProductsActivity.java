package com.avans.easypaykassa;

import android.content.Context;
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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabbed_alter_amounts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        productList = new ArrayList<>();
        createTestProducts();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);

        context = getApplicationContext();
        btn_confirm = (Button) findViewById(R.id.alter_amount_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.alter_amount_cancel);
        btn_cancel.setOnClickListener(this);

        product_adapter = new DeleteProductAdapter(getApplicationContext(), getLayoutInflater(), productList);
    }

    private void createTestProducts() {
        for (int i = 0; i < 20; i++) {
            Product product = new Product("Product "+i,"" ,i);
            productList.add(product);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Laden van de juiste klasse als je op een tab klikt
         switch(position) {
             case 0:
                 DrinksDeleteTab tab1 = new DrinksDeleteTab();
                 //tab1.setTotalListener(totalListener);
                 tab1.setProductAdapter(product_adapter);
                 return tab1;
             case 1:
                 FoodDeleteTab tab2 = new FoodDeleteTab();
                 //tab2.setTotalListener(totalListener);
                 tab2.setProductAdapter(product_adapter);
                 return tab2;
             default:
                 return null;

         }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Drinken";
                case 1:
                    return "Eten";
                case 2:
                    return "SECTION 3";
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

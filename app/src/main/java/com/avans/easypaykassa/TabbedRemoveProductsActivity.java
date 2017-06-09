package com.avans.easypaykassa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import com.avans.easypaykassa.DomainModel.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class TabbedRemoveProductsActivity extends AppCompatActivity implements View.OnClickListener, ProductInterface {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private ArrayList<ArrayList<Product>> products;
    private ArrayList<Product> productList;
    private ArrayList<Product> mergedProducts = new ArrayList<>();
    private ArrayList<Product> selectedItems;
    protected static DeleteProductAdapter adapter;
    private final ProductInterface listener = this;
    private DeleteProductAdapter product_adapter;
    private EasyPayAPIDELETEConnector putReq;
    private Button btn_confirm, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tab_delete_drinks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        btn_confirm = (Button) findViewById(R.id.alter_amount_confirm);
        mSectionsPagerAdapter = new TabbedRemoveProductsActivity.SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        RemoveProductOnClick();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_drink_white_24dp);
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

    @Override
    public void SelectedItemsListener(ArrayList<Product> selectedItems) {
        this.selectedItems = selectedItems;
        mergedProducts.addAll(selectedItems);

        Iterator<Product> iter = mergedProducts.iterator();

        while (iter.hasNext()) {
            Product product  = iter.next();

            if (!product.isChecked()) {
                iter.remove();
            }
            Log.i("", "Products in all lists: " + mergedProducts);
        }
    }

    public void RemoveProductOnClick() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Product product: mergedProducts) {
                    int id = product.getProductId();
                    Log.i("", "ProductID: " + id);
                    deleteProducts(id);
                }
                finish();
            }
        });
    }

    public void deleteProducts(int id) {

        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/delproduct/" + id
                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
        };

        putReq = new EasyPayAPIDELETEConnector();
        putReq.execute(URL);
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
                    tab1.setTotalListener(listener);
                    return tab1;
                case 1:
                    FoodDeleteTab tab2 = new FoodDeleteTab();
                    tab2.setTotalListener(listener);
                    return tab2;
                case 2:
                    SodaDeleteTab tab3 = new SodaDeleteTab();
                    tab3.setTotalListener(listener);
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

    public ArrayList<Product> combineLists() {
        ArrayList<Product> mergedProducts = new ArrayList<>();

        mergedProducts.addAll(selectedItems);
        Log.i("", "combineLists: " + mergedProducts);
        return mergedProducts;
    }
}

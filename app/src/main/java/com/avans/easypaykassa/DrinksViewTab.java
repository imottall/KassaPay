package com.avans.easypaykassa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.avans.easypaykassa.DomainModel.Product;


import java.util.ArrayList;

public class DrinksViewTab extends Fragment {
    private ArrayList<ArrayList<Product>> product;
    private ArrayList<Product> productList;
    private ListView listview_drinks;
    private TextView amount_products, total_price;
    private ArrayList<ArrayList<Product>> products;
    //private ProductsTotal.OnTotalChanged totalListener = null;
    private ProductViewAdapter adapter;

//    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
//        this.totalListener = totalListener;
//    }
    public void setProductAdapter(ProductViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_view_drinks, container, false);
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);
        listview_drinks.setAdapter(adapter);

        return rootView;
    }


}

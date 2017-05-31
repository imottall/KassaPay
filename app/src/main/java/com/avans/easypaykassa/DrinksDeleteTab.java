package com.avans.easypaykassa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Product;

import java.util.ArrayList;

public class DrinksDeleteTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> drinksList;

    private ListView listview_drinks;
    private ArrayList<ArrayList<Product>> products;
    private ProductsTotal.OnTotalChanged totalListener = null;
    private DeleteProductAdapter adapter;

    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
        this.totalListener = totalListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drinksList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_tab_delete_drinks, container, false);
        getProductItems();
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);

        //amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        //total_price = (TextView) rootView.findViewById(R.id.subtotaal);



        adapter = new DeleteProductAdapter(this.getActivity(), inflater, drinksList);
        listview_drinks.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        drinksList.add(product);
        adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/drank"
                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
        };

        new EasyPayAPIConnector(this).execute(URL);
    }
}

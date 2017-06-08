package com.avans.easypaykassa;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.DomainModel.Product;

import java.util.ArrayList;

public class DrinksDeleteTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> drinksList;

    private ListView listview_drinks;
    private Button confirm;
    private TextView prdctname;
    private ArrayList<ArrayList<Product>> products;
    private ProductInterface listener = null;
    private DeleteProductAdapter adapter;
    ArrayList<String> selectedItems;

    public void setTotalListener(ProductInterface listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drinksList = new ArrayList<>();
        final View rootView = inflater.inflate(R.layout.fragment_tab_delete_drinks, container, false);
        getProductItems();
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);

        selectedItems= new ArrayList<>();

        adapter = new DeleteProductAdapter(listener, this.getActivity(), inflater, drinksList);
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

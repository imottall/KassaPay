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

public class FoodDeleteTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> foodList;

    private ListView listview_food;
    private ArrayList<ArrayList<Product>> products;
    private ProductInterface listener = null;
    private DeleteProductAdapter adapter;

    public void setTotalListener(ProductInterface listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foodList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_tab_delete_food, container, false);
        getProductItems();
        listview_food = (ListView) rootView.findViewById(R.id.foodListView);

        //amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        //total_price = (TextView) rootView.findViewById(R.id.subtotaal);



        adapter = new DeleteProductAdapter(listener, this.getActivity(), inflater, foodList);
        listview_food.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        foodList.add(product);
        adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/food"
                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
        };

        new EasyPayAPIConnector(this).execute(URL);
    }
}

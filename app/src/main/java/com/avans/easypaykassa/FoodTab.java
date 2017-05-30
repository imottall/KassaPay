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

public class FoodTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> foodList;
    private ListView listview_food;
    private ArrayList<ArrayList<Product>> products;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        foodList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_food, container, false);
        getProductItems();
        listview_food = (ListView) rootView.findViewById(R.id.foodListView);

        adapter = new ProductAdapter(this.getActivity(), inflater, foodList);
        listview_food.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        Log.i("", "ProductAvailable: " + product);
        foodList.add(product);
        Log.i("", "onProductAvailable: " + foodList);
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

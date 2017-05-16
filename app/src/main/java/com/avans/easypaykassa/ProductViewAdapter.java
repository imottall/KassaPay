package com.avans.easypaykassa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sander on 5/2/2017.
 */

public class ProductViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;
    private ArrayList<ArrayList<Product>> products = new ArrayList<>();
    //protected ArrayList<Product> chosenProducts = new ArrayList<>();

    private ProductsTotal.OnTotalChanged listener;

    private ProductsTotal total;

    public ProductViewAdapter(ProductsTotal.OnTotalChanged listener,Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;
        this.listener = listener;
        System.out.println("prodList size: "+productsList.size());
        for (int i = 0; i < productsList.size(); i++) {

            products.add(new ArrayList<Product>());
        }

        this.total = new ProductsTotal(context, products);


    }
    public ProductViewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;

    }

    @Override
    public int getCount() {
        int size = productsList.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_view_product_row, null);

            viewHolder = new ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.view_product_image);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.view_product_name);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.view_product_price);
            viewHolder.productAmount = (TextView) convertView.findViewById(R.id.view_product_amount);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //placeholder code
        final Product p = productsList.get(position);
        viewHolder.productImage.setImageResource(R.drawable.ic_local_dining_black_24dp);


        viewHolder.productName.setText(p.getProductName());
        viewHolder.productPrice.setText("€" + p.getProductPrice());

        viewHolder.productAmount.setText("50");
//        viewHolder.productName.setText("Product Name123");
//        viewHolder.productPrice.setText("€1,200");
        return convertView;
    }


    private static class ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice, productAmount;
    }
}

package com.avans.easypaykassa;

/**
 * Created by TB on 5/6/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sander on 5/2/2017.
 */

public class DeleteProductAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;
    private int amountOfProducts = 0;

    public DeleteProductAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;
    }

    @Override
    public int getCount() {
        return productsList.size();
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

            convertView = layoutInflater.inflate(R.layout.listview_delete_product_row,null);
            viewHolder = new ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_delete_image);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.product_delete_name);
            viewHolder.productcheck = (CheckBox) convertView.findViewById(R.id.product_delete_checkbox);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.product_delete_price);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product p = (Product) productsList.get(position);
        String amount = p.getAmount() + "X";
        String price = ""+p.getProductPrice();
        viewHolder.productImage.setImageResource(R.drawable.ic_local_dining_black_24dp);
        viewHolder.productName.setText(p.getProductName());
        viewHolder.productPrice.setText(price);

        return convertView;
    }


    private static class ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice;
        private CheckBox productcheck;

    }
}



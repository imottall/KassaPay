package com.avans.easypaykassa;

/**
 * Created by TB on 5/6/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.avans.easypaykassa.DomainModel.Product;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by Sander on 5/2/2017.
 */

public class DeleteProductAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;
    private ArrayList<Product> selectedItems = new ArrayList<>();
    private ProductInterface listener;

    public DeleteProductAdapter(ProductInterface listener, Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.listener = listener;
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
            viewHolder.productcheck = (ImageView) convertView.findViewById(R.id.product_delete_checkbox);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.product_delete_price);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product p = (Product) productsList.get(position);
        String amount = p.getAmount() + "X";
        String price = ""+p.getProductPrice();
        Picasso.with(convertView.getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        viewHolder.productName.setText(p.getProductName());
        viewHolder.productPrice.setText(price);

        if(productsList.get(position).isChecked()) {
            viewHolder.productcheck.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            viewHolder.productcheck.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        while (p.isChecked()) {
            viewHolder.productcheck.setImageResource(R.drawable.ic_check_box_black_24dp);
        }

        viewHolder.productcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsList.get(position).setChecked(!productsList.get(position).isChecked());
                if (productsList.get(position).isChecked()) {
                    viewHolder.productcheck.setImageResource(R.drawable.ic_check_box_black_24dp);
                    selectedItems.add(productsList.get(position));
                } else {
                    viewHolder.productcheck.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    selectedItems.remove(productsList.get(position));
                }
                listener.SelectedItemsListener(selectedItems);
            }
        });

        return convertView;
    }


    private static class ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice;
        private ImageView productcheck;
    }
}



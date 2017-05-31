package com.avans.easypaykassa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Order;

import java.util.ArrayList;

/**
 * Created by Felix on 9-3-2017.
 */

public class OrderOverviewAdapter extends ArrayAdapter<Order> {

    public OrderOverviewAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        //create an order item
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_overview_list_item, parent, false);
        }

        //initialise xml elements
        TextView orderNumberOutput = (TextView) convertView.findViewById(R.id.order_number_textview);
        TextView orderLocationOutput = (TextView) convertView.findViewById(R.id.order_location_textview);
        TextView orderDateOutput = (TextView) convertView.findViewById(R.id.order_date_textview);
        CheckBox orderStatusCheckbox = (CheckBox) convertView.findViewById(R.id.order_status_checkbox);

        //add content to the xml elements
        orderNumberOutput.setText(order.getOrderNumber());
        orderLocationOutput.setText(order.getOrderNumber());
//        orderDateOutput.setText(order.get());
        orderStatusCheckbox.setText(order.getOrderNumber());

        return convertView;
    }
}

package com.avans.easypaykassa;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.avans.easypaykassa.LoginActivity.PREFERENCELOCATION;

/**
 * Created by Felix on 9-3-2017.
 */

public class OrderOverviewAdapter extends ArrayAdapter<Order> {

    private SharedPreferences locationPref;
    private CheckBox checkbox;
    private ImageView xCheckbox;

    public OrderOverviewAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        locationPref = context.getSharedPreferences(PREFERENCELOCATION, context.MODE_PRIVATE);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        long dateInMillis = order.getDate().getTime() + Double.valueOf(2.16e+7).longValue();
        Date date = new Date(dateInMillis);

        //create an order item
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_overview_list_item, parent, false);
        }

        //initialise xml elements
        TextView orderNumberOutput = (TextView) convertView.findViewById(R.id.order_number_textview);
        TextView orderLocationOutput = (TextView) convertView.findViewById(R.id.order_location_textview);
        TextView orderDateOutput = (TextView) convertView.findViewById(R.id.order_date_textview);
        checkbox = (CheckBox) convertView.findViewById(R.id.status_checkbox);
        xCheckbox = (ImageView) convertView.findViewById(R.id.status_imageview);

        //add content to the xml elements
        orderNumberOutput.setText((order.getOrderNumber()+"").toString());
        orderLocationOutput.setText(locationPref.getString(order.getLocation(), "Geen Locatie"));
        orderDateOutput.setText(formatDate(date));
        checkStatusForCheckbox(order.getStatus());

        return convertView;
    }

    public void checkStatusForCheckbox(String status) {
        switch (status) {
            case "PAID":
                checkbox.setChecked(true);
                checkbox.setVisibility(View.VISIBLE);
                xCheckbox.setVisibility(View.GONE);
                break;
            case "WAITING":
                checkbox.setChecked(false);
                checkbox.setVisibility(View.VISIBLE);
                xCheckbox.setVisibility(View.GONE);
                break;
            case "CANCELED":
                checkbox.setVisibility(View.GONE);
                xCheckbox.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    public String formatDate(Date date) {
        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return sdf.format(date);
    }
}

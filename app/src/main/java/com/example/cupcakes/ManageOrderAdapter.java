package com.example.cupcakes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ManageOrderAdapter extends ArrayAdapter<OrderModel> {

    public ManageOrderAdapter(Activity context, ArrayList<OrderModel> orders) {
        super(context, 0, orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manageorder_item, parent, false);
        }

        OrderModel currentOrder = getItem(position);

        TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
        TextView tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvPaymentMethod = convertView.findViewById(R.id.tvPaymentMethod);
        //TextView tvItems = convertView.findViewById(R.id.tvItems);
        Button btnConfirmDelivery = convertView.findViewById(R.id.btnConfirmDelivery);

        tvCustomerName.setText(currentOrder.getCustomerName());
        tvTotalAmount.setText("Total Amount: Rs. " + currentOrder.getTotalAmount());
        tvPaymentMethod.setText("Payment Method: " + currentOrder.getPaymentMethod());

        StringBuilder itemsStringBuilder = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : currentOrder.getItems().entrySet()) {
            String itemName = (String) entry.getValue().get("itemName");
            int quantity = ((Long) entry.getValue().get("quantity")).intValue();
            String price = (String) entry.getValue().get("price");
            itemsStringBuilder.append(itemName)
                    .append(" - Quantity: ").append(quantity)
                    .append(", Price: ").append(price).append("\n");
        }
        //tvItems.setText(itemsStringBuilder.toString());

        btnConfirmDelivery.setOnClickListener(v -> {
            if (getContext() instanceof ManageOrderActivity) {
                ((ManageOrderActivity) getContext()).confirmDelivery(currentOrder.getOrderId());
            }
        });

        return convertView;
    }
}

package com.example.cupcakes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel,OrderAdapter.MyViewHolder> {

    private boolean isAdmin;

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options, boolean isAdmin) {
        super(options);
        this.isAdmin = isAdmin;
    }

    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull OrderModel orderModel) {
        holder.tvCustomerName.setText(orderModel.getCustomerName());
        holder.tvAddress.setText(orderModel.getAddress());
        holder.tvPhone.setText(orderModel.getPhone());
        holder.tvPaymentMethod.setText(orderModel.getPaymentMethod());
        holder.tvTotalAmount.setText(String.format("Total Amount: Rs.%.2f", orderModel.getTotalAmount()));

        StringBuilder itemsString = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : orderModel.getItems().entrySet()) {
            Map<String, Object> item = entry.getValue();
            itemsString.append(item.get("itemName"))
                    .append(" - ")
                    .append(item.get("quantity"))
                    .append(" @ Rs.")
                    .append(item.get("price"))
                    .append("\n");
        }
        holder.tvItems.setText(itemsString.toString().trim());

        if(isAdmin){

            holder.confirmDelivery.setVisibility(View.VISIBLE);
            holder.confirmDelivery.setOnClickListener(v -> {
                // Get the order ID
                String orderId = getRef(holder.getAdapterPosition()).getKey();
                if (orderId != null) {
                    // Reference to the order in the database
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("order").child(orderId);
                    // Delete the order
                    orderRef.removeValue().addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Order confirmed and deleted", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(), "Failed to delete order", Toast.LENGTH_SHORT).show();
                    });
                }
            });


        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvAddress, tvPhone, tvPaymentMethod, tvTotalAmount,tvItems;
        Button confirmDelivery;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvItems = itemView.findViewById(R.id.tvItems);
            confirmDelivery=itemView.findViewById(R.id.BTN_confirmdelivery);
        }
    }



}

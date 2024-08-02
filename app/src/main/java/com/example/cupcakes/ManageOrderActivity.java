package com.example.cupcakes;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageOrderActivity extends AppCompatActivity {

    private ListView orderListView;
    private ManageOrderAdapter orderAdapter;
    private ArrayList<OrderModel> orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        orderListView = findViewById(R.id.orderListView);
        orderList = new ArrayList<>();
        orderAdapter = new ManageOrderAdapter(this, orderList);
        orderListView.setAdapter(orderAdapter);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("order");

        fetchOrders();
    }

    private void fetchOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderModel order = orderSnapshot.getValue(OrderModel.class);
                    if (order != null) {
                        order.setOrderId(orderSnapshot.getKey());
                        orderList.add(order);
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageOrderActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void confirmDelivery(String orderId) {
        ordersRef.child(orderId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageOrderActivity.this, "Order confirmed and removed", Toast.LENGTH_SHORT).show();
                    fetchOrders();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageOrderActivity.this, "Failed to remove order", Toast.LENGTH_SHORT).show();
                });
    }
}

package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreditcardpaymentActivity extends AppCompatActivity {

    private EditText etCardNumber, etExpiryDate, etCvv;
    private Button btnConfirmPayment,btnBack;

    private String fullName, address, phone;
    private double totalAmount;
    private ArrayList<CartModel> selectedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creditcardpayment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCvv = findViewById(R.id.etCVV);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        btnBack = findViewById(R.id.btnBack);


        Intent intent = getIntent();
        if (intent != null) {
            fullName = intent.getStringExtra("fullName");
            address = intent.getStringExtra("address");
            phone = intent.getStringExtra("phone");
            totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
            selectedItems = intent.getParcelableArrayListExtra("selectedItems");
        }

        btnConfirmPayment.setOnClickListener(v -> confirmPayment());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditcardpaymentActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });


    }

    private void confirmPayment() {

        saveOrderToFirebase("Credit Card");
    }

    private void saveOrderToFirebase(String paymentMethod) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("order");
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = ordersRef.push().getKey();

        if (orderId != null) {
            Map<String, Object> order = new HashMap<>();
            order.put("orderId", orderId);
            order.put("userId", userId);
            order.put("customerName", fullName);
            order.put("address", address);
            order.put("phone", phone);
            order.put("paymentMethod", paymentMethod);
            order.put("totalAmount", totalAmount);

            Map<String, Object> itemsMap = new HashMap<>();
            for (int i = 0; i < selectedItems.size(); i++) {
                CartModel item = selectedItems.get(i);
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("itemName", item.getName());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("price", item.getPrice());
                itemsMap.put("item_" + (i + 1), itemMap);
            }
            order.put("items", itemsMap);

            ordersRef.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        for (CartModel item : selectedItems) {
                            cartRef.child(item.getKey()).removeValue()
                                    .addOnSuccessListener(aVoid1 -> {
                                        Toast.makeText(CreditcardpaymentActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(CreditcardpaymentActivity.this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
                                    });
                        }
                        Toast.makeText(CreditcardpaymentActivity.this, "Order saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreditcardpaymentActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(CreditcardpaymentActivity.this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
        }
    }




}
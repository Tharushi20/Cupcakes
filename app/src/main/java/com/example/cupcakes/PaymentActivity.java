package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {

    private EditText etFullName, etAddress, etPhone;
    private RadioGroup paymentMethodRadioGroup;
    private Button btnPay,btnBack;
    private TextView tvTotalAmount;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);



        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        btnPay = findViewById(R.id.btnPay);
        btnBack = findViewById(R.id.btnBack);

        btnPay.setOnClickListener(v -> processPayment());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
            displayTotalAmount(totalAmount);
        } else {
            Toast.makeText(this, "Error: Total amount not found", Toast.LENGTH_SHORT).show();
            finish(); // Finish activity if total amount is not passed
        }

    }

    private void displayTotalAmount(double amount) {
        tvTotalAmount.setText(String.format("Total Amount: Rs.%.2f", amount));
    }

    private void processPayment() {

        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        int selectedPaymentId = paymentMethodRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentMethod = findViewById(selectedPaymentId);
        String paymentMethod = selectedPaymentMethod.getText().toString();


        if (paymentMethod.equals("Credit Card")) {

            startCreditCardPayment();
            clearAll();
        } else if (paymentMethod.equals("Cash on Delivery")) {

            confirmCashOnDeliveryOrder();
            clearAll();
        }
    }

    private void startCreditCardPayment() {

        Intent intent = new Intent(this, CreditcardpaymentActivity.class);

        intent.putExtra("fullName", etFullName.getText().toString().trim());
        intent.putExtra("address", etAddress.getText().toString().trim());
        intent.putExtra("phone", etPhone.getText().toString().trim());
        intent.putExtra("totalAmount", totalAmount);

        // Pass the selected items to the credit card payment activity
        Intent receivedIntent = getIntent();
        ArrayList<CartModel> selectedItems = receivedIntent.getParcelableArrayListExtra("selectedItems");
        intent.putParcelableArrayListExtra("selectedItems", selectedItems);

        startActivity(intent);


    }

    private void confirmCashOnDeliveryOrder() {

        Toast.makeText(this, "Order Confirmed (Cash on Delivery)", Toast.LENGTH_SHORT).show();

        saveOrderToFirebase("Cash on Delivery");

    }

//    private void saveOrderToFirebase(String paymentMethod) {
//
//        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("order");
//        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart");
//
//        Map<String, Object> order = new HashMap<>();
//        order.put("customerName", etFullName.getText().toString().trim());
//        order.put("address", etAddress.getText().toString().trim());
//        order.put("phone", etPhone.getText().toString().trim());
//        order.put("paymentMethod", paymentMethod);
//        order.put("totalAmount", totalAmount);
//
//        Intent intent = getIntent();
//        ArrayList<CartModel> selectedItems = intent.getParcelableArrayListExtra("selectedItems");
//
//        Map<String, Object> itemsMap = new HashMap<>();
//        for (int i = 0; i < selectedItems.size(); i++) {
//            CartModel item = selectedItems.get(i);
//            Map<String, Object> itemMap = new HashMap<>();
//            itemMap.put("itemName", item.getName());
//            itemMap.put("quantity", item.getQuantity());
//            itemMap.put("price", item.getPrice());
//            itemsMap.put("item_" + (i + 1), itemMap);
//        }
//        order.put("items", itemsMap);
//
//
//        ordersRef.push().setValue(order)
//                .addOnSuccessListener(aVoid -> {
//                    // Remove items from the cart
//                    for (CartModel item : selectedItems) {
//                        cartRef.child(item.getKey()).removeValue()
//                                .addOnSuccessListener(aVoid1 -> {
//                                    Toast.makeText(PaymentActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(PaymentActivity.this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
//                                });
//                    }
//                    Toast.makeText(PaymentActivity.this, "Order saved successfully", Toast.LENGTH_SHORT).show();
//
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(PaymentActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
//                });
//    }

    public void saveOrderToFirebase(String paymentMethod) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("order");
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = ordersRef.push().getKey();

        if (orderId != null) {
            Map<String, Object> order = new HashMap<>();
            order.put("orderId", orderId);
            order.put("userId", userId);
            order.put("customerName", etFullName.getText().toString().trim());
            order.put("address", etAddress.getText().toString().trim());
            order.put("phone", etPhone.getText().toString().trim());
            order.put("paymentMethod", paymentMethod);
            order.put("totalAmount", totalAmount);

            Intent intent = getIntent();
            ArrayList<CartModel> selectedItems = intent.getParcelableArrayListExtra("selectedItems");

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
                                        Toast.makeText(PaymentActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(PaymentActivity.this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
                                    });
                        }
                        Toast.makeText(PaymentActivity.this, "Order saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PaymentActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(PaymentActivity.this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
        }
    }

    private  void clearAll(){
        etFullName.setText("");
        etAddress.setText("");
        etPhone.setText("");
        tvTotalAmount.setText("");
    }

}





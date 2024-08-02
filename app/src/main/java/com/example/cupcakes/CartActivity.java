package com.example.cupcakes;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Set;

public class CartActivity extends AppCompatActivity implements CartAdapter.TotalAmountListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private DatabaseReference cartRef;
    private TextView totalAmountTextView;
    private Button checkoutButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalAmountTextView = findViewById(R.id.totalAmount);
        checkoutButton = findViewById(R.id.checkoutButton);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);

        cartRef = FirebaseDatabase.getInstance().getReference().child("cart");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(cartRef.orderByChild("userId").equalTo(currentUserId), CartModel.class)
                        .build();

        cartAdapter = new CartAdapter(options);
        cartAdapter.setTotalAmountListener(this);
        recyclerView.setAdapter(cartAdapter);

        checkoutButton.setOnClickListener(v -> {
            Set<CartModel> selectedItems = cartAdapter.getSelectedItems();
            if (!selectedItems.isEmpty()) {
                double totalAmount = cartAdapter.calculateTotalAmount();
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putParcelableArrayListExtra("selectedItems", new ArrayList<>(selectedItems));
                intent.putExtra("totalAmount", totalAmount);
                startActivity(intent);
            } else {

                Toast.makeText(CartActivity.this, "Please select items to checkout", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(CartActivity.this, HomePage.class));
                    return true;
                } else if (id == R.id.nav_categories) {
                    startActivity(new Intent(CartActivity.this, UserCategory.class));
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(CartActivity.this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_logout) {
                    LogoutUtil.showLogoutDialog(CartActivity.this);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }

    @Override
    public void onTotalAmountChanged(double totalAmount) {
        totalAmountTextView.setText(String.format("Total: Rs.%.2f", totalAmount));
    }
}

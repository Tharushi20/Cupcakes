package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class UserCategory extends AppCompatActivity {

    RecyclerView recyclerView;

    CategoryAdapter categoryAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_categories);

        FirebaseRecyclerOptions<CategoryModel> options =
                new FirebaseRecyclerOptions.Builder<CategoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("category"), CategoryModel.class)
                        .build();

        categoryAdapter = new CategoryAdapter(options,false);
        recyclerView.setAdapter(categoryAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(UserCategory.this, HomePage.class));
                    return true;
                } else if (id == R.id.nav_categories) {
                    startActivity(new Intent(UserCategory.this, UserCategory.class));
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(UserCategory.this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_logout) {
                    LogoutUtil.showLogoutDialog(UserCategory.this);

                    return true;
                }
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        categoryAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        categoryAdapter.stopListening();
    }
}
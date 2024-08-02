// CupcakeListActivity.java

package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class CupcakeListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CupcakeAdapter cupcakeAdapter;
    private String selectedCategory;
    private boolean isAdmin;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupcake_list);

        selectedCategory = getIntent().getStringExtra("category");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton=findViewById(R.id.floatingActionButton2);

        if (!isAdmin) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }


        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("cupcake").orderByChild("category").equalTo(selectedCategory), MainModel.class)
                        .build();


        cupcakeAdapter = new CupcakeAdapter(options, isAdmin,this);
        recyclerView.setAdapter(cupcakeAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        cupcakeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cupcakeAdapter.stopListening();
    }
    public void goToCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

}

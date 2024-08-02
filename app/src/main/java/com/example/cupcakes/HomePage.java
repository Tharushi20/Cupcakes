package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapter mainAdepter;
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("cupcake"), MainModel.class)
                        .build();

        mainAdepter = new MainAdapter(options,false);
        recyclerView.setAdapter(mainAdepter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);



       floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomePage", "Floating action button clicked");
                startActivity(new Intent(getApplicationContext(), CustomerOrderActivity.class));
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(HomePage.this, HomePage.class));
                    return true;
                } else if (id == R.id.nav_categories) {
                    startActivity(new Intent(HomePage.this, UserCategory.class));
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(HomePage.this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_logout) {
                    LogoutUtil.showLogoutDialog(HomePage.this);

                    return true;
                }
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        mainAdepter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mainAdepter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String query) {
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("cupcake")
                        .orderByChild("name").startAt(query).endAt(query + "\uf8ff"), MainModel.class)
                .build();

        mainAdepter = new MainAdapter(options,false);
        mainAdepter.startListening();
        recyclerView.setAdapter(mainAdepter);
    }

}
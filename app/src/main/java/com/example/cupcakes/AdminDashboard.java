package com.example.cupcakes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    CardView addCupcakes,viewOrders,addCategory,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        addCupcakes = findViewById(R.id.CDV_AddCupcakes_id);
        viewOrders=findViewById(R.id.CDV_ViewOrders_id);
        addCategory=findViewById(R.id.CDV_AddCategory_id);
        logout=findViewById(R.id.CDV_Logout_id);

        addCupcakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InvokeAddCupcakes();
            }
        });

        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeViewOrders();
            }
        });



        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvokeAddCategory();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });


    }

    public void InvokeAddCupcakes(){
        Intent invokeManageFlw= new Intent(getApplicationContext(), AddCupcake.class);
        startActivity(invokeManageFlw);
    }

    public void InvokeViewOrders(){
        Intent invokeViewOrders=new Intent(getApplicationContext(), AdminorderActivity.class);
        startActivity(invokeViewOrders);
    }



    public void InvokeAddCategory(){
        Intent invokeAddcategory=new Intent(getApplicationContext(), AddCategory.class);
        startActivity(invokeAddcategory);
    }

    private void logoutUser() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
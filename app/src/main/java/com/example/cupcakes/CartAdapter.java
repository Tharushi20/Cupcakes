package com.example.cupcakes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends FirebaseRecyclerAdapter<CartModel, CartAdapter.CartViewHolder> {

    private Set<CartModel> selectedItems = new HashSet<>();
    private TotalAmountListener totalAmountListener;



    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartModel> options) {
        super(options);
    }

    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {
        model.setKey(getRef(position).getKey());
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(model.getCurl())
                .into(holder.imageView);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedItems.contains(model));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(model);
            } else {
                selectedItems.remove(model);
            }
            calculateTotalAmount();
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = model.getQuantity() + 1;
            model.setQuantity(newQuantity);
            getRef(position).child("quantity").setValue(newQuantity);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = model.getQuantity() - 1;
            if (newQuantity >= 1) {
                model.setQuantity(newQuantity);
                getRef(position).child("quantity").setValue(newQuantity);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            getRef(position).removeValue().addOnSuccessListener(aVoid -> {
                selectedItems.remove(model);
                calculateTotalAmount();
                Toast.makeText(holder.itemView.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(holder.itemView.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
            });
        });

    }

    public double calculateTotalAmount() {
        double total = 0.0;
        for (CartModel item : selectedItems) {
            try {

                String priceString = item.getPrice().replace("Rs.", "").trim();
                double itemPrice = Double.parseDouble(priceString);
                total += itemPrice * item.getQuantity();
            } catch (NumberFormatException e) {
                // Log the error and continue
                Log.e("CartAdapter", "Invalid price format for item: " + item.getName(), e);
            } catch (Exception e) {
                // Log any other unexpected errors
                Log.e("CartAdapter", "Unexpected error calculating total amount for item: " + item.getName(), e);
            }
        }
        // Notify the activity of the total amount change
        if (totalAmountListener != null) {
            totalAmountListener.onTotalAmountChanged(total);
        }
        return total;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView imageView;
        Button btnIncrease, btnDecrease,btnDelete;
        CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cartItemName);
            price = itemView.findViewById(R.id.cartItemPrice);
            quantity = itemView.findViewById(R.id.cartItemQuantity);
            imageView = itemView.findViewById(R.id.cartItemImage);
            btnIncrease = itemView.findViewById(R.id.cartIncreaseQuantity);
            btnDecrease = itemView.findViewById(R.id.cartDecreaseQuantity);
            checkBox = itemView.findViewById(R.id.cartItemCheckbox);
            btnDelete=itemView.findViewById(R.id.cartDeleteItem);
        }
    }


    public void setTotalAmountListener(TotalAmountListener listener) {
        this.totalAmountListener = listener;
    }

    public interface TotalAmountListener {
        void onTotalAmountChanged(double totalAmount);
    }

    public Set<CartModel> getSelectedItems() {
        return selectedItems;
    }
}

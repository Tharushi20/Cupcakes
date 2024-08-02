// CupcakeAdapter.java

package com.example.cupcakes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class CupcakeAdapter extends FirebaseRecyclerAdapter<MainModel, CupcakeAdapter.myViewHolder> {

    private boolean isAdmin;
    private Context context;

    public CupcakeAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, boolean isAdmin,Context context) {

        super(options);
        this.isAdmin = isAdmin;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CupcakeAdapter.myViewHolder holder, int position, @NonNull MainModel mainModel) {
        holder.name.setText(mainModel.getName());
        holder.description.setText(mainModel.getDescription());
        holder.price.setText(mainModel.getPrice());

        Glide.with(holder.img.getContext())
                .load(mainModel.getCurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.img);

        if(!isAdmin){
            holder.addtoCart.setVisibility(View.VISIBLE);
           // holder.floatingActionButton.setVisibility(View.VISIBLE);
            holder.addtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    CartModel cartItem = new CartModel(mainModel.getName(), mainModel.getPrice(), 1, mainModel.getCurl(),currentUserId);
                    FirebaseDatabase.getInstance().getReference().child("cart")
                            .push().setValue(cartItem);
                    Toast.makeText(holder.name.getContext(), "Added to the cart " , Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            holder.addtoCart.setVisibility(View.GONE);
           // holder.floatingActionButton.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cupcake, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name, description, price;
        Button addtoCart;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.cupcake_name);
            description = itemView.findViewById(R.id.cupcake_description);
            price = itemView.findViewById(R.id.cupcake_price);
            addtoCart=itemView.findViewById(R.id.btn_add_to_cart);


        }
    }
}

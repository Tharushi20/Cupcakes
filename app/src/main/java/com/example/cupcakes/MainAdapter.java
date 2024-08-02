package com.example.cupcakes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHoldler> {

    private boolean isAdmin;

//    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
//        super(options);
//    }

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, boolean isAdmin) {
        super(options);
        this.isAdmin = isAdmin;
    }

    protected void onBindViewHolder(@NonNull myViewHoldler holdler, final int position, @NonNull MainModel mainModel) {
        holdler.name.setText(mainModel.getName());
        holdler.price.setText(mainModel.getPrice());
        holdler.category.setText(mainModel.getCategory());
        holdler.description.setText(mainModel.getDescription());

        Glide.with(holdler.img.getContext())
                .load(mainModel.getCurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .into(holdler.img);

        if(isAdmin){

            holdler.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DialogPlus dialogPlus=DialogPlus.newDialog(holdler.img.getContext())
                            .setContentHolder(new ViewHolder(R.layout.edit_popup))
                            .setExpanded(true,2000)
                            .create();



                    View view=dialogPlus.getHolderView();

                    EditText name = view.findViewById(R.id.edtName);
                    EditText category= view.findViewById(R.id.edtCategory);
                    EditText description = view.findViewById(R.id.edtDes);
                    EditText price = view.findViewById(R.id.edtPrice);
                    EditText curl=view.findViewById(R.id.edtimg);

                    Button btnUpdate =view.findViewById(R.id.btnUpdate);

                    name.setText(mainModel.getName());
                    category.setText(mainModel.getCategory());
                    description.setText(mainModel.getDescription());
                    price.setText(mainModel.getPrice());
                    curl.setText(mainModel.getCurl());

                    dialogPlus.show();

                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("name",name.getText().toString());
                            map.put("category",category.getText().toString());
                            map.put("description",description.getText().toString());
                            map.put("price",price.getText().toString());
                            map.put("curl",curl.getText().toString());

                            int currentPosition = holdler.getAbsoluteAdapterPosition();

                            FirebaseDatabase.getInstance().getReference().child("cupcake")
                                    .child(getRef(currentPosition).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(holdler.name.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(holdler.name.getContext(), "Error while updating.", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    });
                }
            });
            holdler.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(holdler.name.getContext());
                    builder.setTitle("Are you sure? ");
                    builder.setMessage("Delete data can't be undo. ");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int currentPosition = holdler.getAbsoluteAdapterPosition();

                            FirebaseDatabase.getInstance().getReference().child("cupcake")
                                    .child(getRef(currentPosition).getKey()).removeValue();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(holdler.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    });

                    builder.show();
                }
            });

        }else {
           holdler.btnAddCart.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                   CartModel cartItem = new CartModel(mainModel.getName(), mainModel.getPrice(), 1, mainModel.getCurl(),currentUserId);
                   FirebaseDatabase.getInstance().getReference().child("cart")
                           .push().setValue(cartItem);
                   Toast.makeText(holdler.name.getContext(), "Added to the cart " , Toast.LENGTH_SHORT).show();
               }
           });
            holdler.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = Integer.parseInt(holdler.quantityText.getText().toString());
                    int newQuantity = currentQuantity + 1;
                    holdler.quantityText.setText(String.valueOf(newQuantity));
                    updateCartItemQuantity(mainModel.getName(), newQuantity);
                }
            });

            holdler.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = Integer.parseInt(holdler.quantityText.getText().toString());
                    if (currentQuantity > 1) {
                        int newQuantity = currentQuantity - 1;
                        holdler.quantityText.setText(String.valueOf(newQuantity));
                        updateCartItemQuantity(mainModel.getName(), newQuantity);
                    }
                }
            });
        }


    }
    private void updateCartItemQuantity(String itemName, int quantity) {
        FirebaseDatabase.getInstance().getReference().child("cart")
                .orderByChild("name").equalTo(itemName)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().child("quantity").setValue(quantity);
                        }
                    }
                });
    }

    public myViewHoldler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (isAdmin) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupcake_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
        }
        return new myViewHoldler(view);
    }

    class myViewHoldler extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name, price, category,description,quantityText;
        Button btnEdit,btnDelete,btnAddCart,btnIncreaseQuantity,btnDecreaseQuantity;

        public myViewHoldler(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView)itemView.findViewById(R.id.nameText_id);
            price = (TextView)itemView.findViewById(R.id.CuPrice);
            category = (TextView)itemView.findViewById(R.id.CuCate_id);
            description=(TextView)itemView.findViewById(R.id.CuDes_id);




            if (isAdmin) {
                btnEdit = itemView.findViewById(R.id.BTN_edit_id);
                btnDelete = itemView.findViewById(R.id.BTN_delete_id);
            } else {
                btnAddCart = itemView.findViewById(R.id.BTN_addtocart);
                quantityText = itemView.findViewById(R.id.quantityText);
                btnIncreaseQuantity = itemView.findViewById(R.id.BTN_increase_quantity);
                btnDecreaseQuantity = itemView.findViewById(R.id.BTN_decrease_quantity);
            }


        }

    }
}

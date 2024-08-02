package com.example.cupcakes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends FirebaseRecyclerAdapter<CategoryModel,CategoryAdapter.myViewHolder> {

    private boolean isAdmin;

    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<CategoryModel> options, boolean isAdmin) {
        super(options);
        this.isAdmin = isAdmin;
    }

    protected void onBindViewHolder(@NonNull CategoryAdapter.myViewHolder holdler, final int position, @NonNull CategoryModel categoryModel) {

        holdler.name.setText(categoryModel.getName());

        Glide.with(holdler.img.getContext())
                .load(categoryModel.getCaturl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
               // .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .into(holdler.img);

        if(isAdmin){
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

                            FirebaseDatabase.getInstance().getReference().child("category")
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


        }
        holdler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CupcakeListActivity.class);
                intent.putExtra("category", categoryModel.getName());
                intent.putExtra("isAdmin", isAdmin);
                v.getContext().startActivity(intent);
            }
        });


    }

    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
//        return new myViewHolder(view);

        View view;
        if (isAdmin) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercat_items, parent, false);
        }
        return new CategoryAdapter.myViewHolder(view);
    }

     class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name;
        Button btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.catnameText_id);
            if (isAdmin){
                btnDelete = itemView.findViewById(R.id.BTN_delete_id);
            }
        }
    }
}

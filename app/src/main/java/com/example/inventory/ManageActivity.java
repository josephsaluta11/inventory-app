package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageActivity extends AppCompatActivity {

    private Button bOpen, bEdit;
    private FirebaseAuth firebaseAuth;
    DatabaseReference inventorydatabaseReference;
    DatabaseReference databaseReference;
    private RecyclerView inventoryrecyclerview;
    //inventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        //setup auth reference to access database
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");
        inventorydatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventory");

        //setup recyclerview
        inventoryrecyclerview = findViewById(R.id.inventoryrv);
        inventoryrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        //setup adapter
        /**
         FirebaseRecyclerOptions<Inventory> options = new FirebaseRecyclerOptions.Builder<Inventory>().setQuery(inventorydatabaseReference, Inventory.class).build();
         adapter = new inventoryAdapter(options);
         inventoryrecyclerview.setAdapter(adapter);

         bOpen = findViewById(R.id.bOpen);
         bEdit = findViewById(R.id.bEdit);

         bOpen.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) { openInventory(); }
        });

         bEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) { editInventory(); }
        });
         **/
    }
    /**
     public void openInventory() {
     Intent intent = new Intent(this, InvCurrent.class);
     startActivity(intent);
     }

     public void editInventory() {
     Intent intent = new Intent(this, InvCurrent.class);
     startActivity(intent);
     }
     **/

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
        //Trying another method of recycler view to

        FirebaseRecyclerOptions<Inventory> options =
                new FirebaseRecyclerOptions.Builder<Inventory>()
                        .setQuery(inventorydatabaseReference, Inventory.class)
                        .build();

        FirebaseRecyclerAdapter<Inventory,inventoryViewholder> adapter =
                new FirebaseRecyclerAdapter<Inventory, inventoryViewholder>(options) {
                    @Override
                    protected void onBindViewHolder (@NonNull inventoryViewholder holder, final int position, @NonNull Inventory model)
                    {
                        holder.inventoryname.setText(model.getInventoryname());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String inventory_name = model.getInventoryname();
                                Intent intent = new Intent(ManageActivity.this, InvCurrent.class);
                                intent.putExtra("Inventory Name", inventory_name);
                                startActivity(intent);

                            }
                        });
                    }
                    @NonNull
                    @Override
                    public inventoryViewholder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType){
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ilist_layout, viewGroup, false);
                        inventoryViewholder viewHolder = new inventoryViewholder(view);
                        return viewHolder;
                    }
                };
        inventoryrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    public static class inventoryViewholder extends RecyclerView.ViewHolder {
        TextView inventoryname;

        public inventoryViewholder(@NonNull View itemView) {
            super(itemView);
            inventoryname = itemView.findViewById(R.id.inventorynametv);
        }
    }
}
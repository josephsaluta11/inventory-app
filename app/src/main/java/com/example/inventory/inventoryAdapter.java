package com.example.inventory;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// This class currently not used. I don't know why I can't start InvCurrent activity in this.
// So I incorporated the FirebaseRecyclerAdapter in the ManageActivity class and it worked.
public class inventoryAdapter extends FirebaseRecyclerAdapter<
        Inventory, inventoryAdapter.inventoryViewholder> {

    String inventory_name;
    Context c;



    public inventoryAdapter(
            FirebaseRecyclerOptions<Inventory> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull inventoryViewholder holder, int position, @NonNull Inventory model)
    {
        holder.inventoryname.setText(model.getInventoryname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_name = model.getInventoryname();
                Intent intent = new Intent(c, InvCurrent.class);
                intent.putExtra("Inventory Name", inventory_name);
                c.startActivity(intent);



            }
        });
    }

    @NonNull
    @Override
    public inventoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ilist_layout, parent, false);
        return new inventoryAdapter.inventoryViewholder(view);

    }

    class inventoryViewholder extends RecyclerView.ViewHolder {
        TextView inventoryname;

        public inventoryViewholder(View itemView) {

            super(itemView);
            inventoryname = itemView.findViewById(R.id.inventorynametv);
        }
    }
}


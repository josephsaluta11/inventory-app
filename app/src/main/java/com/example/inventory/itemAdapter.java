package com.example.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class itemAdapter extends FirebaseRecyclerAdapter<
        Items, itemAdapter.itemViewholder> {

    public itemAdapter(
            FirebaseRecyclerOptions<Items> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull itemViewholder holder, int position, @NonNull Items model)
    {
        holder.itemname.setText(model.getItemname());
        holder.itemcategory.setText(model.getItemcategory());
        holder.itemquantity.setText(model.getItemquantity());
        holder.itemunit.setText(model.getItemunit());

    }

    @NonNull
    @Override
    public itemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new itemAdapter.itemViewholder(view);
    }

    class itemViewholder extends RecyclerView.ViewHolder {
        TextView itemname, itemcategory, itemquantity, itemunit;

        public itemViewholder(View itemView) {

            super(itemView);
            itemname = itemView.findViewById(R.id.itemnametv);
            itemcategory = itemView.findViewById(R.id.itemcategorytv);
            itemquantity = itemView.findViewById(R.id.itemquantitytv);
            itemunit = itemView.findViewById(R.id.itemunittv);
        }
    }
}

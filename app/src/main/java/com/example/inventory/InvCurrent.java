package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InvCurrent extends AppCompatActivity {

    private static final String TAG = "ITEMNAME";
    public static EditText resultsearchview;
    Context ctx;
    boolean automaticChanged = false;


    private AlertDialog.Builder dialogBuilder, dialogBuilder_;
    private AlertDialog dialog;
    private TextInputEditText itemname, itemcategory, itemquantity, itemunit;
    private TextView inventorynametv;
    private Button itemSave, itemCancel, bAddItem, searchbtn, itemUpdate, itemDelete;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;
    DatabaseReference itemdatabaseReference;
    private RecyclerView itemrecyclerview;
    FirebaseRecyclerAdapter<Items, itemViewholder_> adapter_;
    FirebaseRecyclerOptions<Items> options_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invcurrent);

        Intent intent = getIntent();
        String inventorynameValue = intent.getStringExtra("Inventory Name");
        inventorynametv = findViewById(R.id.textView10);
        inventorynametv.setText(inventorynameValue);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");

        resultsearchview = findViewById(R.id.searchfield);
        //searchbtn = findViewById(R.id.searchbutton);

        itemdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventory").child(inventorynameValue).child("ByName");
        itemrecyclerview = findViewById(R.id.itemrv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        itemrecyclerview.setLayoutManager(manager);
        itemrecyclerview.setHasFixedSize(true);
        itemrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        options_ =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(itemdatabaseReference, Items.class)
                        .build();
        adapter_ =
                new FirebaseRecyclerAdapter<Items, itemViewholder_>(options_) {
                    @Override
                    protected void onBindViewHolder(@NonNull itemViewholder_ holder, int position, @NonNull Items model) {
                        holder.itemname.setText(model.getItemname());
                        holder.itemcategory.setText(model.getItemcategory());
                        holder.itemquantity.setText(model.getItemquantity());
                        holder.itemunit.setText(model.getItemunit());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String item_name = model.getItemname();
                                String item_category = model.getItemcategory();
                                String item_quantity = model.getItemquantity();
                                String item_unit = model.getItemunit();
                                subtractItemDialog(item_category, item_name, item_quantity, item_unit);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public itemViewholder_ onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                        return new itemViewholder_(view);
                    }
                };


        bAddItem = findViewById(R.id.bAddItem);

        bAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchtext = resultsearchview.getText().toString();
                firebasesearch(searchtext);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!automaticChanged) {

                } else {
                    automaticChanged = false;
                }
            }
        };

        resultsearchview.addTextChangedListener(textWatcher);
        /**
         searchbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        automaticChanged = true;
        String searchtext = resultsearchview.getText().toString();
        firebasesearch(searchtext);
        }
        });
         **/
    }

    public void firebasesearch(String searchtext) {
        Query firebaseSearchQuery = itemdatabaseReference.orderByChild("itemname").startAt(searchtext).endAt(searchtext+"\uf8ff");

        FirebaseRecyclerOptions<Items> option =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(firebaseSearchQuery, Items.class)
                        .build();

        FirebaseRecyclerAdapter<Items, itemViewholder_> adapter=
                new FirebaseRecyclerAdapter<Items, itemViewholder_>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull itemViewholder_ holder, int position, @NonNull Items model) {
                        holder.itemname.setText(model.getItemname());
                        holder.itemcategory.setText(model.getItemcategory());
                        holder.itemquantity.setText(model.getItemquantity());
                        holder.itemunit.setText(model.getItemunit());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String item_name = model.getItemname();
                                String item_category = model.getItemcategory();
                                String item_quantity = model.getItemquantity();
                                String item_unit = model.getItemunit();
                                subtractItemDialog(item_category, item_name, item_quantity, item_unit);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public itemViewholder_ onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                        return new itemViewholder_(view);
                    }
                };

        itemrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    public void addItemDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View newitemPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        itemcategory = newitemPopupView.findViewById(R.id.newitempopup_category);
        itemname = newitemPopupView.findViewById(R.id.newitempopup_description);
        itemquantity = newitemPopupView.findViewById(R.id.newitempopup_quantity);
        itemunit = newitemPopupView.findViewById(R.id.newitempopup_unit);

        itemSave = (Button) newitemPopupView.findViewById(R.id.saveButton);
        itemCancel = (Button) newitemPopupView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newitemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        itemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define save button here!
                addItem();
                adapter_.startListening();
                dialog.dismiss();
            }
        });

        itemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define cancel button here!
                dialog.dismiss();
            }
        });
    }

    public void subtractItemDialog(String itemcat_, String itemn_, String itemquant_, String itemu_) {
        dialogBuilder_ = new AlertDialog.Builder(this);
        final View newitemPopupView_ = getLayoutInflater().inflate(R.layout.popupdelete, null);
        itemcategory = newitemPopupView_.findViewById(R.id.newitempopup_category_);
        itemname = newitemPopupView_.findViewById(R.id.newitempopup_description_);
        itemquantity = newitemPopupView_.findViewById(R.id.newitempopup_quantity_);
        itemunit = newitemPopupView_.findViewById(R.id.newitempopup_unit_);

        itemcategory.setEnabled(false);
        itemname.setEnabled(false);
        itemunit.setEnabled(false);

        itemcategory.setText(itemcat_);
        itemname.setText(itemn_);
        itemquantity.setText(itemquant_);
        itemunit.setText(itemu_);

        itemUpdate = (Button) newitemPopupView_.findViewById(R.id.updateButton_);
        itemCancel = (Button) newitemPopupView_.findViewById(R.id.cancelButton_);
        itemDelete = (Button) newitemPopupView_.findViewById(R.id.deleteButton_);

        dialogBuilder_.setView(newitemPopupView_);
        dialog = dialogBuilder_.create();
        dialog.show();

        itemUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define save button here!
                updateItem();
                adapter_.startListening();
                dialog.dismiss();
            }
        });

        itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
                adapter_.startListening();
                dialog.dismiss();
            }
        });

        itemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define cancel button here!
                dialog.dismiss();
            }
        });
    }

    public void updateItem() {
        String inventorynameValue = inventorynametv.getText().toString();
        String itemnameValue = itemname.getText().toString().replace("\\","");
        String itemnamVal = itemnameValue.replaceAll("[^a-zA-Z0-9]","");
        String itemcategoryValue = itemcategory.getText().toString().replace("\\","");
        String itemcatVal = itemcategoryValue.replaceAll("[^a-zA-Z0-9]","");
        String itemquantityValue = itemquantity.getText().toString();
        String itemunitValue = itemunit.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");
        Log.d(TAG, itemnameValue);

        if(!TextUtils.isEmpty(itemnameValue)&&!TextUtils.isEmpty(itemcategoryValue)&&!TextUtils.isEmpty(itemquantityValue)&&!TextUtils.isEmpty(itemunitValue)) {
            Items items = new Items(itemnameValue, itemcategoryValue, itemquantityValue, itemunitValue);

            databaseReference.child(resultemail).child("Inventory")
                    .child(inventorynameValue).child("ByName").child(itemnameValue)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!(snapshot.exists())) {
                                databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByName").child(itemnamVal).setValue(items);
                                databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByCategory").child(itemcatVal).child(itemnamVal).setValue(items);
                                itemname.setText("");
                                itemcategory.setText("");
                                itemquantity.setText("");
                                itemunit.setText("");
                                Toast.makeText(InvCurrent.this, itemnameValue + " updated", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(InvCurrent.this, itemnameValue + " updated", Toast.LENGTH_SHORT).show();
                                databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByName").child(itemnamVal).setValue(items);
                                databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByCategory").child(itemcategoryValue).child(itemnamVal).setValue(items);
                                itemname.setText("");
                                itemcategory.setText("");
                                itemquantity.setText("");
                                itemunit.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else {
            Toast.makeText(InvCurrent.this, "Fill all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem() {
        String inventorynameValue = inventorynametv.getText().toString();
        String itemnameValue = itemname.getText().toString().replace("\\","");
        String itemnamVal = itemnameValue.replaceAll("[^a-zA-Z0-9]","");
        String itemcategoryValue = itemcategory.getText().toString().replace("\\","");
        String itemcatVal = itemcategoryValue.replaceAll("[^a-zA-Z0-9]","");
        String itemquantityValue = itemquantity.getText().toString();
        String itemunitValue = itemunit.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");
        Log.d(TAG, itemnameValue);

        if(!TextUtils.isEmpty(itemnameValue)&&!TextUtils.isEmpty(itemcategoryValue)&&!TextUtils.isEmpty(itemquantityValue)&&!TextUtils.isEmpty(itemunitValue)) {
            Items items = new Items(itemnameValue, itemcategoryValue, itemquantityValue, itemunitValue);
            databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByName").child(itemnamVal).setValue(items);
            databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByCategory").child(itemcatVal).child(itemnamVal).setValue(items);
            itemname.setText("");
            itemcategory.setText("");
            itemquantity.setText("");
            itemunit.setText("");
            Toast.makeText(InvCurrent.this, itemnameValue + " Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(InvCurrent.this, "Fill all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem() {
        String inventorynameValue = inventorynametv.getText().toString();
        String itemnameValue = itemname.getText().toString().replace("\\","");
        String itemnamVal = itemnameValue.replaceAll("[^a-zA-Z0-9]","");
        String itemcategoryValue = itemcategory.getText().toString().replace("\\","");
        String itemcatVal = itemcategoryValue.replaceAll("[^a-zA-Z0-9]","");
        String itemquantityValue = itemquantity.getText().toString();
        String itemunitValue = itemunit.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");
        Log.d(TAG, itemnameValue);

        if(!TextUtils.isEmpty(itemnameValue)&&!TextUtils.isEmpty(itemcategoryValue)&&!TextUtils.isEmpty(itemquantityValue)&&!TextUtils.isEmpty(itemunitValue)) {
            Items items = new Items(itemnameValue, itemcategoryValue, itemquantityValue, itemunitValue);
            databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByName").child(itemnamVal).removeValue();
            databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).child("ByCategory").child(itemcatVal).child(itemnamVal).removeValue();
            itemname.setText("");
            itemcategory.setText("");
            itemquantity.setText("");
            itemunit.setText("");
            Toast.makeText(InvCurrent.this, itemnameValue + " Deleted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(InvCurrent.this, "Fill all details", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
/**
 FirebaseRecyclerOptions<Items> options_ =
 new FirebaseRecyclerOptions.Builder<Items>()
 .setQuery(itemdatabaseReference, Items.class)
 .build();

 FirebaseRecyclerAdapter<Items, itemViewholder_> adapter_ =
 new FirebaseRecyclerAdapter<Items, itemViewholder_>(options_) {
@Override
protected void onBindViewHolder(@NonNull itemViewholder_ holder, int position, @NonNull Items model) {
holder.itemname.setText(model.getItemname());
holder.itemcategory.setText(model.getItemcategory());
holder.itemquantity.setText(model.getItemquantity());
holder.itemunit.setText(model.getItemunit());
holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
String item_name = model.getItemname();
String item_category = model.getItemcategory();
String item_quantity = model.getItemquantity();
String item_unit = model.getItemunit();
}
});
}

@NonNull
@Override
public itemViewholder_ onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
return new itemViewholder_(view);
}
};
 **/
        itemrecyclerview.setAdapter(adapter_);
        adapter_.startListening();
    }

    public static class itemViewholder_ extends RecyclerView.ViewHolder {
        TextView itemname, itemcategory, itemquantity, itemunit;

        public itemViewholder_(View itemView) {

            super(itemView);
            itemname = itemView.findViewById(R.id.itemnametv);
            itemcategory = itemView.findViewById(R.id.itemcategorytv);
            itemquantity = itemView.findViewById(R.id.itemquantitytv);
            itemunit = itemView.findViewById(R.id.itemunittv);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_.stopListening();
    }

    /**FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>
     (Items.class,
     R.layout.list_layout,
     UsersViewHolder.class,
     itemdatabaseReference)
     {
     @Override
     protected void populateViewHolder(UsersViewHolder viewHolder, Items model, int position) {

     viewHolder.setDetails(getApplicationContext(),model.getItemname(), model.getItemcategory(), model.getItemquantity(), model.getItemunit());
     }
     };

     itemrecyclerview.setAdapter(firebaseRecyclerAdapter);
     }**/

    /**public static class UsersViewHolder extends RecyclerView.ViewHolder {
     View mView;
     public UsersViewHolder(View itemView) {
     super(itemView);
     mView = itemView;
     }
     public void setDetails(Context context, String itemname, String itemcategory, String itemquantity, String itemunit) {
     TextView item_name = (TextView) mView.findViewById(R.id.itemnametv);
     TextView item_category = (TextView) mView.findViewById(R.id.itemcategorytv);
     TextView item_quantity = (TextView) mView.findViewById(R.id.itemquantitytv);
     TextView item_unit = (TextView) mView.findViewById(R.id.itemunittv);

     item_name.setText(itemname);
     item_category.setText(itemcategory);
     item_quantity.setText(itemquantity);
     item_unit.setText(itemunit);
     }
     } **/

}
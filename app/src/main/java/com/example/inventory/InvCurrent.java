package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InvCurrent extends AppCompatActivity {

    private static final String TAG = "ITEMNAME";


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemname, itemcategory, itemquantity, itemunit;
    private TextView inventorynametv;
    private Button itemSave, itemCancel, bAddItem;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;
    DatabaseReference itemdatabaseReference;
    private RecyclerView itemrecyclerview;
    itemAdapter adapter;

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

        itemdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventory").child(inventorynameValue).child("ByName");
        itemrecyclerview = findViewById(R.id.itemrv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        itemrecyclerview.setLayoutManager(manager);
        itemrecyclerview.setHasFixedSize(true);
        itemrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().setQuery(itemdatabaseReference, Items.class).build();
        adapter = new itemAdapter(options);
        itemrecyclerview.setAdapter(adapter);

        bAddItem = findViewById(R.id.bAddItem);

        bAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog();
            }
        });


    }

    public void addItemDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View newitemPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        itemcategory = (EditText) newitemPopupView.findViewById(R.id.newitempopup_category);
        itemname = (EditText) newitemPopupView.findViewById(R.id.newitempopup_description);
        itemquantity = (EditText) newitemPopupView.findViewById(R.id.newitempopup_quantity);
        itemunit = (EditText) newitemPopupView.findViewById(R.id.newitempopup_unit);

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

                adapter.startListening();
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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
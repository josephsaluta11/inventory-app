package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewInventory extends AppCompatActivity {

    private Button bCreate;
    private TextInputEditText inventoryname;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inventory);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencecat = FirebaseDatabase.getInstance().getReference("Users");

        bCreate = findViewById(R.id.bCreate);
        inventoryname = findViewById(R.id.inventoryname);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createInventory();
            }
        });
    }

    public void createInventory() {

        String inventorynameValue = inventoryname.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".","");

        //check if there is an existing path

        if(!TextUtils.isEmpty(inventorynameValue)) {

            Inventory inventory = new Inventory(inventorynameValue);

            databaseReference.child(resultemail).child("Inventory")
                    .child(inventorynameValue)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(!(snapshot.exists())) {
                                databaseReference.child(resultemail).child("Inventory").child(inventorynameValue).setValue(inventory);
                                Toast.makeText(NewInventory.this, inventorynameValue + " Added", Toast.LENGTH_SHORT).show();
                                nextpage();
                            }

                            else {
                                Toast.makeText(NewInventory.this, inventorynameValue + " already exists", Toast.LENGTH_SHORT).show();
                                inventoryname.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                        }
                    });

        }

        else {
            Toast.makeText(NewInventory.this, "Please fill inventory name", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextpage() {
        String inventorynameValue = inventoryname.getText().toString();
        Intent intent = new Intent(this, InvCurrent.class);
        intent.putExtra("Inventory Name", inventorynameValue);
        inventoryname.setText("");
        startActivity(intent);
    }
}
package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button bCreateNewInv, bManageInv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bCreateNewInv = findViewById(R.id.bCreateNewInv);
        bManageInv = findViewById(R.id.bManageInv);

        bCreateNewInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { createNewInventory(); }
        });

        bManageInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { manageInventory(); }
        });
    }

    public void createNewInventory() {
        Intent intent = new Intent(this, NewInventory.class);
        startActivity(intent);
    }

    public void manageInventory() {
        Intent intent = new Intent(this, ManageActivity.class);
        startActivity(intent);
    }
}
package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageActivity extends AppCompatActivity {

    private Button bOpen, bEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

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
    }

    public void openInventory() {
        Intent intent = new Intent(this, InvCurrent.class);
        startActivity(intent);
    }

    public void editInventory() {
        Intent intent = new Intent(this, InvCurrent.class);
        startActivity(intent);
    }

}
package com.example.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InvCurrent extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newitempopup_category, newitempopup_description, newitempopup_quantity, newitempopup_unit;
    private Button newitempopup_save, newitempopup_cancel, bAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invcurrent);

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
        newitempopup_category = (EditText) newitemPopupView.findViewById(R.id.newitempopup_category);
        newitempopup_description = (EditText) newitemPopupView.findViewById(R.id.newitempopup_description);
        newitempopup_quantity = (EditText) newitemPopupView.findViewById(R.id.newitempopup_quantity);
        newitempopup_unit = (EditText) newitemPopupView.findViewById(R.id.newitempopup_unit);

        newitempopup_save = (Button) newitemPopupView.findViewById(R.id.saveButton);
        newitempopup_cancel = (Button) newitemPopupView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newitemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newitempopup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define save button here!
                dialog.dismiss();
            }
        });

        newitempopup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define cancel button here!
                dialog.dismiss();
            }
        });

    }
}
package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private Button bCreateNewInv, bManageInv;

    private TextInputLayout useremail_outlinedbox;
    private TextInputEditText user_email;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private MaterialButton logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bCreateNewInv = findViewById(R.id.bCreateNewInv);
        bManageInv = findViewById(R.id.bManageInv);

        useremail_outlinedbox = findViewById(R.id.useremail_outlinedbox);
        user_email = findViewById(R.id.user_email);
        logout_btn = findViewById(R.id.logout_btn);
        useremail_outlinedbox.setEnabled(false);
        user_email.setEnabled( false );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        user_email.setText(currentUser.getEmail());

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() ==null){
            startActivity(new Intent(Home.this,Login.class));
        }
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
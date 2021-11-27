package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private TextInputEditText userEmail, userPassword;
    private MaterialButton register_btn;
    private TextView login_tv;
    private FirebaseAuth mAuth;
    private String TAG;
    private ProgressBar register_progress;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userEmail = findViewById(R.id.email_edit_text);
        userPassword = findViewById(R.id.password_edit_text);
        register_btn = findViewById(R.id.register_btn);
        register_progress = findViewById(R.id.register_progress);
        login_tv = findViewById(R.id.login_tv);
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
                finish();
            }
        });


        mAuth = FirebaseAuth.getInstance();


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_btn.setVisibility(View.GONE);
                register_progress.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {

                    //display an error message
                    Toast.makeText(Signup.this,"Email and Password Shouldn't be Empty",Toast.LENGTH_LONG).show();
                    register_btn.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.GONE);


                }
                else {

                    RegisterUserAccount(email, password);
                }
            }
        });

    }


    private void RegisterUserAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Signup.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
                        }else {
                            Toast.makeText(Signup.this,"Failed to Create an Account" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            register_btn.setVisibility(View.VISIBLE);
                            register_progress.setVisibility(View.GONE);
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(Signup.this, Home.class));
        }
    }
}
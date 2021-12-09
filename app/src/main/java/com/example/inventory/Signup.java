package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private TextInputEditText userEmail, userPassword, cuserPassword;
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
        cuserPassword = findViewById(R.id.cpassword_edit_text);
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
                RegisterUserAccount();
            }

        });

    }



    private void RegisterUserAccount(){

        final String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String cpassword = cuserPassword.getText().toString();

        if (email.isEmpty()) {
            userEmail.setError("Email is empty");
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Not a valid email address");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError("Password is empty");
            userPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            userPassword.setError("Must be more than 6 characters");
            userPassword.requestFocus();
            return;
        }

        if (!password.equals(cpassword)) {
            userPassword.setError("Password did not match");
            userPassword.requestFocus();
            return;
        }

        register_btn.setVisibility(View.GONE);
        register_progress.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            final User user = new User(
                                    email
                            );

                            FirebaseUser usernameinfirebase = mAuth.getCurrentUser();
                            String UserID = usernameinfirebase.getEmail();

                            String resultemail = UserID.replace(".","");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(resultemail).child("UserDetails")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    register_progress.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(Signup.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signup.this, Home.class));

                                    } else {

                                    }
                                }
                            });

                        } else {
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
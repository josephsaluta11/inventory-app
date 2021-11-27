package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputEditText userEmail, userPassword;
    private MaterialButton login_btn;
    private TextView reset_tv;
    private FirebaseAuth mAuth;
    private String TAG;
    private ProgressBar login_progress;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.email_edit_text);
        userPassword = findViewById(R.id.password_edit_text);
        login_btn = findViewById(R.id.login_btn);
        login_progress = findViewById(R.id.login_progress);
        reset_tv = findViewById(R.id.reset_tv);

        mAuth = FirebaseAuth.getInstance();


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setVisibility(View.GONE);
                login_progress.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this,"Please Verify All Field",Toast.LENGTH_SHORT);
                    login_btn.setVisibility(View.VISIBLE);
                    login_progress.setVisibility(View.GONE);
                }
                else
                {
                    signIn(email,password);
                }
            }
        });

        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResetPassword();
            }
        });

    }


    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Login.this,"Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,Home.class));
                }
                else {
                    Toast.makeText(Login.this,"Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    login_btn.setVisibility(View.VISIBLE);
                    login_progress.setVisibility(View.GONE);
                }
            }
        });

    }


    private void ResetPassword(){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder( Login.this );

        View view = LayoutInflater.from(Login.this).inflate(R.layout.dialog_edit_text,null);
        TextInputEditText reg_email = view.findViewById(R.id.reset_email_edit_text);


        materialAlertDialogBuilder.setTitle("Reset Password")
                .setMessage("Reset Password Link will be Sent to your Registered Email Address")
                .setCancelable(true)
                .setView(view)
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String regemail = reg_email.getText().toString();
                        mAuth.sendPasswordResetEmail(regemail);
                        Toast.makeText(Login.this,"Kindly Please Check your Email", Toast.LENGTH_SHORT).show();

                    }
                }).show();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
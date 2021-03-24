package com.example.sanibmcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout emailEditText;
    private Button resetPasswordButton;
    private LinearLayout back;
    private ProgressBar progressBar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailEditText = findViewById(R.id.emailreset);
        resetPasswordButton = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBarReset);
        back = findViewById(R.id.back);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);

    }
    private  void resetPassword(){
        String email = emailEditText.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            emailEditText.setError("Email masukkan");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Masukkan Email Valid");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Cek email untuk reset password", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ResetPasswordActivity.this, "Coba Lagi", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPassword:
                resetPassword();
                Intent a = new Intent(ResetPasswordActivity.this, ConfirmResetActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
                break;
            case R.id.back:
                Intent b = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(b);
                finish();
        }
    }
}
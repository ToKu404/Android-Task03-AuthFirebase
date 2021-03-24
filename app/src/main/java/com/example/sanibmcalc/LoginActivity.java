package com.example.sanibmcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private TextInputLayout editTextEmail, editTextPassword;
    private Button signIn;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.signUpLogin);
        register.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.logInBtn);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        checkBox = findViewById(R.id.remember_me);
        progressBar = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.forgotPass);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpLogin:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.logInBtn:
                userLogin();
                break;
            case R.id.forgotPass:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = String.valueOf(editTextEmail.getEditText().getText()).trim();
        String password = String.valueOf(editTextPassword.getEditText().getText()).trim();
        if(email.isEmpty()){
            editTextEmail.setError("Email Requiered");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Masukkan Email Valid");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        progressBar.setVisibility(View.VISIBLE);
                        if(checkBox.isChecked()){
                            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("remember", "true");
                            editor.apply();
                        }else{
                            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("remember", "false");
                            editor.apply();
                        }
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class ));
                    }else{
                        user.sendEmailVerification();
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (android.content.ActivityNotFoundException e) {
                            Toast.makeText(LoginActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(LoginActivity.this, "Verifikasi Email", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}
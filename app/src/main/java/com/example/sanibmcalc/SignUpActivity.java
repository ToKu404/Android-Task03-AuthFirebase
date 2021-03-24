package com.example.sanibmcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //Inisialisasi Attribut
    private TextView registerUser, login;
    private TextInputLayout editTextFullName, editTextEmail, editTextPassword,editTextConfPassword;
    private TextInputEditText epass;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        //pick widget by id
        login = (TextView) findViewById(R.id.signIn);
        registerUser = (Button) findViewById(R.id.register);
        editTextFullName = findViewById(R.id.namaLengkap);
        editTextEmail = findViewById(R.id.emailSignUp);
        editTextPassword = findViewById(R.id.passwordSignUp);
        editTextConfPassword = findViewById(R.id.confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Yang Bisa Di Click
        //Meng Implement View.OnCLickListener
        login.setOnClickListener(this);
        registerUser.setOnClickListener(this);
        //Text Change
        epass = (TextInputEditText) findViewById(R.id.passwordTF);
        epass.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        //Terdapat dua onclick listener jadi digunakan switch
        switch (v.getId()){
            case R.id.register:
                registerUser();
                break;
            case R.id.signIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
    private void registerUser() {
        //Mengambil Inputan
        String email =  String.valueOf(editTextEmail.getEditText().getText()).trim();
        String password =  String.valueOf(editTextPassword.getEditText().getText()).trim();
        String confirm_password =  String.valueOf(editTextConfPassword.getEditText().getText()).trim();
        String fullname =  String.valueOf(editTextFullName.getEditText().getText()).trim();

        if(fullname.isEmpty()){
            editTextFullName.setError("FullName Is Required!");
            editTextFullName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Masukkan Email Yang Benar");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Password is not valid");
            editTextPassword.requestFocus();
            return;
        }
        if(confirm_password.isEmpty()){
            editTextConfPassword.setError("Input Pass Again");
            editTextConfPassword.requestFocus();
            return;
        }
        if(!confirm_password.toString().equals(password.toString())){
            editTextConfPassword.setError("Input Pass Again");
            editTextConfPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "User Has Been Register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent a = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(a);
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this, "Failed To REgister Try Again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Failed To Register Try Again", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    private void updatePasswordStrengthView(String password) {
        int complex = password.isEmpty()?0:1;
        complex += password.length()>=6?1:0;
        complex += isVeryComplex(password)?1:0;
        List<ProgressBar> progressBars = new ArrayList<>();
        progressBars.add((ProgressBar) findViewById(R.id.progressBar1));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar2));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar3));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar4));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar5));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar6));

        switch (complex) {
            case 0:
                for (ProgressBar var : progressBars)
                {
                    var.setProgress(0);
                    var.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

                }
            case 1:
                progressBars.get(0).setProgress(100);
                progressBars.get(0).getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                for (int i = 0; i < 4; i++) {
                    progressBars.get(i).setProgress(100);
                    progressBars.get(i).getProgressDrawable().setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
                }
                break;
            case 3:
                for (ProgressBar var : progressBars)
                {
                    var.setProgress(100);
                    var.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                }
                break;
        }

    }
    public boolean isVeryComplex(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
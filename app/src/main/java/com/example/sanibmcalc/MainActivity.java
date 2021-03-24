package com.example.sanibmcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(!(user == null)){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                }
            }
        };
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String checkbox = preferences.getString("remember", "false");
        if(!checkbox.equals("true")){
            if(isTaskRoot()) {
                firebaseAuth.signOut();
                editor.putString("remember", "false");
            }
        }
    }


    public void toLogin(View view) {
        Intent a = new Intent(MainActivity.this, LoginActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        overridePendingTransition(0, 0);
    }

    public void toSignUp(View view) {
        Intent a = new Intent(MainActivity.this, SignUpActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        overridePendingTransition(0, 0);
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
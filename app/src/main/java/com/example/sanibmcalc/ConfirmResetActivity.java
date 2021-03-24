package com.example.sanibmcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmResetActivity extends AppCompatActivity implements View.OnClickListener {
    private Button openEmail;
    private TextView skip, anotherEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reset);

        openEmail = findViewById(R.id.openEmail);
        skip = findViewById(R.id.skip);
        anotherEmail = findViewById(R.id.anotherEmail);

        openEmail.setOnClickListener(this);
        skip.setOnClickListener(this);
        anotherEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openEmail:
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);

                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(ConfirmResetActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.skip:
                Intent a = new Intent(ConfirmResetActivity.this, LoginActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                break;
                case R.id.anotherEmail:
                Intent b = new Intent(ConfirmResetActivity.this, ResetPasswordActivity.class);
                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(b);
                break;
        }
    }
}
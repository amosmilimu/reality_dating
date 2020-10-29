package com.reality.datingapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.reality.datingapp.R;

public class EmailConfirmation extends AppCompatActivity {
    private Button btnGoToEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        btnGoToEmail = findViewById(R.id.go_to_email);

        btnGoToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailConfirmation.this,LoginActivity.class));
            }
        });
    }
}

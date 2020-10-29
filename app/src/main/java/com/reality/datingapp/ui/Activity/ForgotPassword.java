package com.reality.datingapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.reality.datingapp.R;

public class ForgotPassword extends AppCompatActivity {

    private TextView backToLogin;
    private EditText etGetResetPass;
    private Button resetPass;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backToLogin = findViewById(R.id.backToSlots);
        etGetResetPass = findViewById(R.id.etResetEmail);
        resetPass = findViewById(R.id.btnResetPass);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        /////////////////////////////////////////////////onclick listeners//////////////////////////////////////
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                onBackPressed();
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Sending Link.");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.show();


                String email = etGetResetPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    etGetResetPass.setError("Email is required!");
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this,"Check your email for the reset link",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });
    }
}

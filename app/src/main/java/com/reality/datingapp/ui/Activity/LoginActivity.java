package com.reality.datingapp.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reality.datingapp.Application;
import com.reality.datingapp.R;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private FirebaseAuth userAuth;
    private EditText loginEmail;
    private EditText loginPass;
    private Button btnLoginPageLogin;
    private TextView btnLoginPageRegister;
    TextView forgotPassword;
    AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MobileAds.initialize(this, Application.ADDMOB_APP_ID);
        mAdview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        userAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmailText);
        loginPass = findViewById(R.id.loginPassText);
        btnLoginPageLogin = findViewById(R.id.btnLoginPageLogin);
        btnLoginPageRegister = findViewById(R.id.btnLoginPageRegister);
        forgotPassword = findViewById(R.id.tv_forgotPass);


        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading...");


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });
        btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = loginEmail.getText().toString();
                String userPass = loginPass.getText().toString();

                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty((userPass))) {

                    dialog.show();

                    userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if (userAuth.getCurrentUser().isEmailVerified()){
                                    sendToMain();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(LoginActivity.this,"Please verify your email first",Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLoginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

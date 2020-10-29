package com.reality.datingapp.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.anthonycr.progress.AnimatedProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.reality.datingapp.R;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;


public class StartActivity extends AppCompatActivity {

    Button buttonStartEmail;

    ImageView imageStart;
    AnimatedProgressBar progressBar;
    Handler handler;
    ProgressDialog dialog;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        buttonStartEmail = findViewById(R.id.buttonStartEmail);
        imageStart = findViewById(R.id.imageStart);


        firebaseFirestore.collection("admin")
                .document("settings")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            String start_image = documentSnapshot.getString("start_image");
                            Picasso.get().load(start_image).placeholder(R.drawable.account_bg).into(imageStart);
                        }
                    }
                });

        handler=new Handler();



        //animated progress bar
        int progressNum = 50;
        progressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);
        progressBar.setProgress(50);
        progressNum = progressBar.getProgress();

        //timer
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);





    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}

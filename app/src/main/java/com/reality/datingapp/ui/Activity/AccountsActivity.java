package com.reality.datingapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reality.datingapp.Application;
import com.reality.datingapp.CustomAdatpter.AccountsAdapter;
import com.reality.datingapp.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsActivity extends AppCompatActivity {


    CircleImageView circleImageViewProfileAvatar;
    ViewPager viewPagerAccount;
    TabLayout tabLayoutAccount;
    AccountsAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private InterstitialAd interstitialAd;

    private FloatingActionButton viewPagerBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        MobileAds.initialize(this, Application.ADDMOB_APP_ID);
        AdRequest adRequest = new AdRequest.Builder().build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        viewPagerAccount = findViewById(R.id.viewPagerAccount);
        viewPagerAccount.setOffscreenPageLimit(7);

        viewPagerBack = findViewById(R.id.viewPagerBack);

        tabLayoutAccount = findViewById(R.id.tabLayoutAccount);

        tabLayoutAccount.addTab(tabLayoutAccount.newTab().setText("Matches"));
        tabLayoutAccount.addTab(tabLayoutAccount.newTab().setText("Likes"));
        tabLayoutAccount.addTab(tabLayoutAccount.newTab().setText("Visits"));
        tabLayoutAccount.addTab(tabLayoutAccount.newTab().setText("Favorite"));


        //Setting up the page interstitial add TODO replace fake add with real add
                interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2961175322148657/5518420681");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener()
                                     {
                                         @Override
                                         public void onAdClosed() {
                                             backToProfileActivity();
                                             interstitialAd.loadAd(new AdRequest.Builder().build());
                                         }
                                     }
        );


        tabLayoutAccount.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new AccountsAdapter
                (getSupportFragmentManager(), tabLayoutAccount.getTabCount());
        viewPagerAccount.setAdapter(adapter);

        viewPagerAccount.setCurrentItem(0);


        String tabShow;
        tabShow = getIntent().getStringExtra("tab_show");
        if (tabShow != null) {
            switch (tabShow) {
                case "tab_matches":
                    viewPagerAccount.setCurrentItem(0);
                    break;
                case "tab_likes":
                    viewPagerAccount.setCurrentItem(1);
                    break;
                case "tab_visitors":
                    viewPagerAccount.setCurrentItem(2);
                    break;
                case "tab_favorites":
                    viewPagerAccount.setCurrentItem(3);
                    break;
            }
        }



        viewPagerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                }else {

                    backToProfileActivity();

                }

            }
        });

        viewPagerAccount.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutAccount));
        tabLayoutAccount.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAccount.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    private void UserStatus(String status) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        Map<String, Object> arrayUserStatus = new HashMap<>();
        arrayUserStatus.put("user_status", status);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users")
                .document(currentUser)
                .update(arrayUserStatus);
    }


    private void OnlineUser() {
        String currentUser = firebaseUser.getUid();

        Map<String, Object> arrayOnlineUser = new HashMap<>();
        arrayOnlineUser.put("user_online", Timestamp.now());

        firebaseFirestore.collection("users")
                .document(currentUser)
                .update(arrayOnlineUser);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UserStatus("online");
        OnlineUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserStatus("offline");
    }

    private void backToProfileActivity(){
        Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
        intent.putExtra("tab_show", "tab_profile");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

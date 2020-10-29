package com.reality.datingapp.ui.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reality.datingapp.Application;
import com.reality.datingapp.CustomAdatpter.UsersAdapter;
import com.reality.datingapp.R;
import com.reality.datingapp.model.Event_DataModel;
import com.reality.datingapp.model.Loves_DataModel;
import com.reality.datingapp.model.Profile_DataModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class UsersFragment extends Fragment {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUser;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    SharedPreferences prefs;
    ProgressBar progressBarUsersView;
    SwipeRefreshLayout swipeRefreshLayout;
    String stringCheckGender;
    String stringCheckAgesMin;
    String stringCheckAgesMax;
    String stringCheckLocation;
    String stringCheckMarital;
    String stringCheckSexual;
    String stringCheckSeeking;
    ArrayList<Loves_DataModel> listLovesDataModels;
    RelativeLayout relativeLayoutUsersContent;
    LinearLayout linearLayoutUsersEmpty;
    private RecyclerView recyclerViewUserView;
    private UsersAdapter usersAdapter;
    private ArrayList<Profile_DataModel> arrayUserClass;
    AdView mAdview;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        listLovesDataModels = new ArrayList<>();

        if (firebaseUser != null) {
            currentUser = firebaseUser.getUid();
        }
        prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        arrayUserClass = new ArrayList<>();
        progressBarUsersView = view.findViewById(R.id.progressBarUsersView);

        recyclerViewUserView = view.findViewById(R.id.recyclerViewUsersView);


        MobileAds.initialize(getContext(), Application.ADDMOB_APP_ID);
        mAdview = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        relativeLayoutUsersContent = view.findViewById(R.id.relativeLayoutUsersContent);
        relativeLayoutUsersContent.setVisibility(View.VISIBLE);
        linearLayoutUsersEmpty = view.findViewById(R.id.linearLayoutUsersEmpty);
        linearLayoutUsersEmpty.setVisibility(View.GONE);


        recyclerViewUserView.setHasFixedSize(true);
        recyclerViewUserView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        swipeRefreshLayout = view.findViewById(R.id.userSwipeRefreshLayout);


        UserRecyclerView();

        SwipeRefresh();

        return view;
    }


    private void SwipeRefresh() {
        int myColorBackground = Color.parseColor("#880e4f");
        int myColorForeground = Color.parseColor("#ffffff");
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(myColorForeground);
        swipeRefreshLayout.setColorSchemeColors(myColorBackground);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayout.setRefreshing(true);

                                                arrayUserClass.clear();

                                                UserRecyclerView();
                                            }
                                        }
                );
            }
        });

    }


    private void UserRecyclerView() {


        firebaseFirestore.collection("users")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        String stringShowMarital = documentSnapshot.getString("show_marital");
                        if (stringShowMarital != null) {
                            stringCheckMarital = stringShowMarital;
                        } else {
                            stringCheckMarital = "Any";
                        }

                        String stringShowSexual = documentSnapshot.getString("show_sexual");
                        if (stringShowSexual != null) {
                            stringCheckSexual = stringShowSexual;
                        } else {
                            stringCheckSexual = "Any";
                        }

                        String stringShowSeeking = documentSnapshot.getString("show_seeking");
                        if (stringShowSeeking != null) {
                            stringCheckSeeking = stringShowSeeking;
                        } else {
                            stringCheckSeeking = "Any";
                        }

                        String stringShowAges = documentSnapshot.getString("show_ages");
                        if (stringShowAges != null) {
                            String[] arrayCheckLookage = stringShowAges.trim().split("\\s*-\\s*");
                            stringCheckAgesMin = arrayCheckLookage[0];
                            stringCheckAgesMax = arrayCheckLookage[1];
                        } else {
                            stringCheckAgesMin = "16";
                            stringCheckAgesMax = "100000";
                        }

                        String stringShowLocation = documentSnapshot.getString("show_location");
                        if (stringShowLocation != null) {
                            stringCheckLocation = stringShowLocation;
                        } else {
                            String stringUserState = documentSnapshot.getString("user_state");

                            Map<String, Object> mapShowLocation = new HashMap<>();
                            mapShowLocation.put("show_location", stringUserState);
                            firebaseFirestore.collection("users")
                                    .document(currentUser)
                                    .update(mapShowLocation);

                            stringCheckLocation = stringUserState;
                        }

                        String stringShowGender = documentSnapshot.getString("show_gender");
                        if (stringShowGender != null) {
                            if (stringShowGender.equals("Man")) {
                                stringCheckGender = "Male";
                                UsersDisplay(stringCheckGender, stringCheckAgesMin, stringCheckAgesMax,
                                        stringCheckLocation, stringCheckMarital, stringCheckSexual, stringCheckSeeking);

                            } else if (stringShowGender.equals("Woman")) {
                                stringCheckGender = "Female";
                                UsersDisplay(stringCheckGender, stringCheckAgesMin, stringCheckAgesMax,
                                        stringCheckLocation, stringCheckMarital, stringCheckSexual, stringCheckSeeking);
                            } else if (stringShowGender.equals("Any")) {
                                stringCheckGender = "Any";
                                UsersDisplay(stringCheckGender, stringCheckAgesMin, stringCheckAgesMax,
                                        stringCheckLocation, stringCheckMarital, stringCheckSexual, stringCheckSeeking);
                            }
                        } else {
                            String stringUserGender = documentSnapshot.getString("user_gender");

                            if (stringUserGender.equals("Male")) {
                                stringCheckGender = "Female";

                                Map<String, Object> mapShowGender = new HashMap<>();
                                mapShowGender.put("show_gender", "Woman");
                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .update(mapShowGender);

                                UsersDisplay(stringCheckGender, stringCheckAgesMin, stringCheckAgesMax,
                                        stringCheckLocation, stringCheckMarital, stringCheckSexual, stringCheckSeeking);

                            } else {
                                stringCheckGender = "Male";

                                Map<String, Object> mapShowGender = new HashMap<>();
                                mapShowGender.put("show_gender", "Man");
                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .update(mapShowGender);

                                UsersDisplay(stringCheckGender, stringCheckAgesMin, stringCheckAgesMax,
                                        stringCheckLocation, stringCheckMarital, stringCheckSexual, stringCheckSeeking);
                            }

                        }


                    }
                });
    }

    private void UsersDisplay(final String stringCheckGender,
                              final String stringCheckAgesMin,
                              final String stringCheckAgesMax,
                              final String stringCheckLocation,
                              final String stringCheckMarital,
                              final String stringCheckSexual,
                              final String stringCheckSeeking) {

        final String currentUser = firebaseUser.getUid();

        firebaseFirestore.collection("users")
                .orderBy("user_online", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot querySnapshot : task.getResult()) {

                            Profile_DataModel profileDataModel = querySnapshot.toObject(Profile_DataModel.class);

                            if (!profileDataModel.getUser_uid().equals(currentUser)) {

                                if (stringCheckGender.equals("Any")) {

                                    int intBirthage = Integer.valueOf(profileDataModel.getUser_birthage());
                                    int intLookageMin = Integer.valueOf(stringCheckAgesMin);
                                    int intLookageMax = Integer.valueOf(stringCheckAgesMax);

                                    if (intBirthage >= intLookageMin && intBirthage <= intLookageMax) {

                                        if (stringCheckLocation.equals("Anywhere")) {

                                            if (stringCheckMarital.equals("Any")) {

                                                if (stringCheckSexual.equals("Any")) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);


                                                        }

                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }

                                                    }


                                                } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                }

                                            } else if (stringCheckMarital.equals(profileDataModel.getUser_marital())) {

                                                if (stringCheckSexual.equals("Any")) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                }
                                            }


                                        } else if (stringCheckLocation.equals(profileDataModel.getUser_city()) ||
                                                stringCheckLocation.equals(profileDataModel.getUser_state()) ||
                                                stringCheckLocation.equals(profileDataModel.getUser_country())) {

                                            if (stringCheckMarital.equals("Any")) {

                                                if (stringCheckSexual.equals("Any")) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                }
                                            } else if (stringCheckMarital.equals(profileDataModel.getUser_marital())) {

                                                if (stringCheckSexual.equals("Any")) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                    if (stringCheckSeeking.equals("Any")) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                        if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                            arrayUserClass.add(profileDataModel);

                                                        }
                                                    }
                                                }
                                            }

                                        }


                                    }

                                } else {

                                    if (profileDataModel.getUser_gender().equals(stringCheckGender)) {


                                        int intBirthage = Integer.valueOf(profileDataModel.getUser_birthage());
                                        int intLookageMin = Integer.valueOf(stringCheckAgesMin);
                                        int intLookageMax = Integer.valueOf(stringCheckAgesMax);

                                        if (intBirthage >= intLookageMin && intBirthage <= intLookageMax) {

                                            if (stringCheckLocation.equals("Anywhere")) {

                                                if (stringCheckMarital.equals("Any")) {

                                                    if (stringCheckSexual.equals("Any")) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }


                                                    } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    }

                                                } else if (stringCheckMarital.equals(profileDataModel.getUser_marital())) {

                                                    if (stringCheckSexual.equals("Any")) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    }
                                                }


                                            } else if (stringCheckLocation.equals(profileDataModel.getUser_city()) ||
                                                    stringCheckLocation.equals(profileDataModel.getUser_state()) ||
                                                    stringCheckLocation.equals(profileDataModel.getUser_country())) {

                                                if (stringCheckMarital.equals("Any")) {

                                                    if (stringCheckSexual.equals("Any")) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    }
                                                } else if (stringCheckMarital.equals(profileDataModel.getUser_marital())) {

                                                    if (stringCheckSexual.equals("Any")) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    } else if (stringCheckSexual.equals(profileDataModel.getUser_sexual())) {

                                                        if (stringCheckSeeking.equals("Any")) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        } else if (stringCheckSeeking.equals(profileDataModel.getUser_seeking())) {


                                                            if (profileDataModel.getShow_profile() == null || profileDataModel.getShow_profile().equals("yes")) {

                                                                arrayUserClass.add(profileDataModel);

                                                            }
                                                        }
                                                    }
                                                }

                                            }


                                        }
                                    }

                                }
                            }

                            usersAdapter = new UsersAdapter(arrayUserClass, getActivity());
                            recyclerViewUserView.setAdapter(usersAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            progressBarUsersView.setVisibility(View.GONE);

                            if (arrayUserClass.size() == 0) {

                                relativeLayoutUsersContent.setVisibility(View.GONE);
                                linearLayoutUsersEmpty.setVisibility(View.VISIBLE);

                            } else {

                                relativeLayoutUsersContent.setVisibility(View.VISIBLE);
                                linearLayoutUsersEmpty.setVisibility(View.GONE);
                            }
                        }


                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event_DataModel event) {

        arrayUserClass.clear();
        UserRecyclerView();

        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

}

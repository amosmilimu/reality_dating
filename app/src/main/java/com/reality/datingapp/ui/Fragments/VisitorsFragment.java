package com.reality.datingapp.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.reality.datingapp.Application;
import com.reality.datingapp.R;
import com.reality.datingapp.firestore.VisitorsFirestore;
import com.reality.datingapp.model.Visitors_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;

public class VisitorsFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    LinearLayout linearLayoutVisitorsContent;
    LinearLayout linearLayoutVisitorsEmpty;
    private RecyclerView recyclerViewVisitsView;
    private VisitorsFirestore visitorsFirestore;
    AdView mAdview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitores, container, false);

        recyclerViewVisitsView = view.findViewById(R.id.recyclerViewVisitsView);

        VisitsRecyclerView();


        MobileAds.initialize(getContext(), Application.ADDMOB_APP_ID);
        mAdview = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        linearLayoutVisitorsContent = view.findViewById(R.id.linearLayoutVisitorsContent);
        linearLayoutVisitorsContent.setVisibility(View.VISIBLE);
        linearLayoutVisitorsEmpty = view.findViewById(R.id.linearLayoutVisitorsEmpty);
        linearLayoutVisitorsEmpty.setVisibility(View.GONE);


        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("visits")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.size() == 0) {

                                linearLayoutVisitorsContent.setVisibility(View.GONE);
                                linearLayoutVisitorsEmpty.setVisibility(View.VISIBLE);

                            } else {

                                linearLayoutVisitorsContent.setVisibility(View.VISIBLE);
                                linearLayoutVisitorsEmpty.setVisibility(View.GONE);
                            }
                        }
                    }
                });


        return view;
    }

    private void VisitsRecyclerView() {

        Query query = firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("visits")
                .orderBy("user_visited", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Visitors_DataModel> options = new FirestoreRecyclerOptions.Builder<Visitors_DataModel>()
                .setQuery(query, Visitors_DataModel.class)
                .build();

        visitorsFirestore = new VisitorsFirestore(options);

        recyclerViewVisitsView.setHasFixedSize(true);
        recyclerViewVisitsView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewVisitsView.setAdapter(visitorsFirestore);

        visitorsFirestore.setOnItemClickListener(new VisitorsFirestore.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final Visitors_DataModel visitorsDataModel = documentSnapshot.toObject(Visitors_DataModel.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();

                final Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", visitorsDataModel.getUser_visitor());
                startActivity(intent);

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        visitorsFirestore.startListening();

    }

}

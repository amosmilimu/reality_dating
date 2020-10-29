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
import com.reality.datingapp.firestore.MatchesFirestore;
import com.reality.datingapp.model.Matches_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;

public class MatchesFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    LinearLayout linearLayoutMatchContent;
    LinearLayout linearLayoutMatchEmpty;
    private RecyclerView recyclerViewMatchesView;
    private MatchesFirestore matchesFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerViewMatchesView = view.findViewById(R.id.recyclerViewMatchesView);
        MatchesRecyclerView();


        linearLayoutMatchContent = view.findViewById(R.id.linearLayoutMatchContent);
        linearLayoutMatchContent.setVisibility(View.VISIBLE);
        linearLayoutMatchEmpty = view.findViewById(R.id.linearLayoutMatchEmpty);
        linearLayoutMatchEmpty.setVisibility(View.GONE);


        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("matches")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.size() == 0) {

                                linearLayoutMatchContent.setVisibility(View.GONE);
                                linearLayoutMatchEmpty.setVisibility(View.VISIBLE);

                            } else {

                                linearLayoutMatchContent.setVisibility(View.VISIBLE);
                                linearLayoutMatchEmpty.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        return view;
    }

    /**
     * get user machted profile using firbase query
     */
    private void MatchesRecyclerView() {

        Query query = firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("matches")
                .orderBy("user_matched", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Matches_DataModel> options = new FirestoreRecyclerOptions.Builder<Matches_DataModel>()
                .setQuery(query, Matches_DataModel.class)
                .build();

        matchesFirestore = new MatchesFirestore(options);
        recyclerViewMatchesView.setHasFixedSize(true);
        recyclerViewMatchesView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMatchesView.setAdapter(matchesFirestore);


        matchesFirestore.setOnItemClickListener(new MatchesFirestore.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final Matches_DataModel matchesDataModel = documentSnapshot.toObject(Matches_DataModel.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();

                final Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", matchesDataModel.getUser_matches());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        matchesFirestore.startListening();
    }
}

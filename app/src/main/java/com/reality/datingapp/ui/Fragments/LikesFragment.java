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
import com.reality.datingapp.firestore.LikesFirestore;
import com.reality.datingapp.model.Likes_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;

public class LikesFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    LinearLayout linearLayoutLikesContent;
    LinearLayout linearLayoutLikesEmpty;
    private RecyclerView recyclerViewLikesView;
    private LikesFirestore likesFirestore;
    AdView mAdview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_likes, container, false);


        MobileAds.initialize(getContext(), Application.ADDMOB_APP_ID);
        mAdview = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        recyclerViewLikesView = view.findViewById(R.id.recyclerViewLikesView);
        LikesRecyclerView();

        linearLayoutLikesContent = view.findViewById(R.id.linearLayoutLikesContent);
        linearLayoutLikesContent.setVisibility(View.VISIBLE);
        linearLayoutLikesEmpty = view.findViewById(R.id.linearLayoutLikesEmpty);
        linearLayoutLikesEmpty.setVisibility(View.GONE);



        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("likes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.size() == 0) {

                                linearLayoutLikesContent.setVisibility(View.GONE);
                                linearLayoutLikesEmpty.setVisibility(View.VISIBLE);

                            } else {

                                linearLayoutLikesContent.setVisibility(View.VISIBLE);
                                linearLayoutLikesEmpty.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        return view;
    }

    /**
     * show list of like from user using  firbase query
     */
    private void LikesRecyclerView() {

        Query query = firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("likes")
                .orderBy("user_liked", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Likes_DataModel> options = new FirestoreRecyclerOptions.Builder<Likes_DataModel>()
                .setQuery(query, Likes_DataModel.class)
                .build();

        likesFirestore = new LikesFirestore(options);
        recyclerViewLikesView.setHasFixedSize(true);
        recyclerViewLikesView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLikesView.setAdapter(likesFirestore);

        likesFirestore.setOnItemClickListener(new LikesFirestore.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final Likes_DataModel likesDataModel = documentSnapshot.toObject(Likes_DataModel.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();

                final Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", likesDataModel.getUser_likes());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        likesFirestore.startListening();
    }

}

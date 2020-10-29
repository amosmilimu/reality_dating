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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.reality.datingapp.R;
import com.reality.datingapp.firestore.FavoritesFirestore;
import com.reality.datingapp.model.Favorites_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;

public class FavoritesFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    LinearLayout linearLayoutFavoritesContent;
    LinearLayout linearLayoutFavoritesEmpty;
    private RecyclerView recyclerViewFavoritesView;
    private FavoritesFirestore favoritesFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favrites, container, false);
        recyclerViewFavoritesView = view.findViewById(R.id.recyclerViewFavoritesView);

        FavoritesRecyclerView();


        linearLayoutFavoritesContent = view.findViewById(R.id.linearLayoutFavoritesContent);
        linearLayoutFavoritesContent.setVisibility(View.VISIBLE);
        linearLayoutFavoritesEmpty = view.findViewById(R.id.linearLayoutFavoritesEmpty);
        linearLayoutFavoritesEmpty.setVisibility(View.GONE);


        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("favors")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.size() == 0) {

                                linearLayoutFavoritesContent.setVisibility(View.GONE);
                                linearLayoutFavoritesEmpty.setVisibility(View.VISIBLE);

                            } else {

                                linearLayoutFavoritesContent.setVisibility(View.VISIBLE);
                                linearLayoutFavoritesEmpty.setVisibility(View.GONE);
                            }
                        }
                    }
                });


        return view;
    }

    private void FavoritesRecyclerView() {

        Query query = firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("favors")
                .orderBy("user_favorited", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Favorites_DataModel> options = new FirestoreRecyclerOptions.Builder<Favorites_DataModel>()
                .setQuery(query, Favorites_DataModel.class)
                .build();

        favoritesFirestore = new FavoritesFirestore(options);

        recyclerViewFavoritesView.setHasFixedSize(true);
        recyclerViewFavoritesView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFavoritesView.setAdapter(favoritesFirestore);

        favoritesFirestore.setOnItemClickListener(new FavoritesFirestore.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final Favorites_DataModel favoritesDataModel = documentSnapshot.toObject(Favorites_DataModel.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();


                final Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", favoritesDataModel.getUser_favorite());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        favoritesFirestore.startListening();

    }

}

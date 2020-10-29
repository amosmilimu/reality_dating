package com.reality.datingapp.CustomAdatpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.reality.datingapp.R;
import com.reality.datingapp.model.Feeds_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    ArrayList<Feeds_DataModel> arrayFeedsDataModels;
    Context context;

    String user_keyz;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public FeedsAdapter(ArrayList<Feeds_DataModel> arrayFeedsDataModels, Context context) {
        this.arrayFeedsDataModels = arrayFeedsDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.feeds_item, viewGroup, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        return new ViewHolder(view, context, arrayFeedsDataModels);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Feeds_DataModel feedsDataModel = arrayFeedsDataModels.get(i);

        String currentUser = firebaseUser.getUid();

        firebaseFirestore.collection("users")
                .document(feedsDataModel.getFeed_user())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null) {
                            Picasso.get().load(documentSnapshot.getString("user_thumb")).into(viewHolder.imageViewFeedsItemFeedsThumb);

                            viewHolder.textViewFeedsItemFeedsUser.setText(documentSnapshot.getString("user_name"));
                        }
                    }
                });


        Picasso.get().load(feedsDataModel.getFeed_cover()).into(viewHolder.imageViewFeedsItemFeedsCover);


        viewHolder.textViewFeedsItemFeedsLikes.setText(String.valueOf(feedsDataModel.getFeed_like()));

        viewHolder.relativeTimeFeedsItemFeedsDate.setReferenceTime(feedsDataModel.feed_date.getTime());

        firebaseFirestore.collection("feeds")
                .document(feedsDataModel.getFeed_uid())
                .collection("likes")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            Picasso.get().load(R.drawable.tab_feed_like_on).into(viewHolder.imageViewFeedsItemFeedsLikes);
                        } else {
                            Picasso.get().load(R.drawable.tab_feed_like_off).into(viewHolder.imageViewFeedsItemFeedsLikes);
                        }
                    }
                });

        viewHolder.imageViewFeedsItemFeedsLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedLike(viewHolder, viewHolder.imageViewFeedsItemFeedsLikes, viewHolder.textViewFeedsItemFeedsLikes, feedsDataModel);
            }
        });

    }


    private void FeedLike(final ViewHolder viewHolder, ImageView imageView, TextView textView, final Feeds_DataModel feedsDataModel) {

        final String currentUser = firebaseUser.getUid();

        firebaseFirestore.collection("feeds")
                .document(feedsDataModel.getFeed_uid())
                .collection("likes")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {

                            firebaseFirestore.collection("feeds")
                                    .document(feedsDataModel.getFeed_uid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                String stringLikes = task.getResult().getString("feed_like");
                                                long longLikes = Long.valueOf(stringLikes) - 1;
                                                final String addLikes = String.valueOf(longLikes);

                                                HashMap<String, Object> hashMapUpdate = new HashMap<>();
                                                hashMapUpdate.put("feed_like", addLikes);

                                                firebaseFirestore.collection("feeds")
                                                        .document(feedsDataModel.getFeed_uid())
                                                        .update(hashMapUpdate)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    firebaseFirestore.collection("feeds")
                                                                            .document(feedsDataModel.getFeed_uid())
                                                                            .collection("likes")
                                                                            .document(currentUser)
                                                                            .delete()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        viewHolder.textViewFeedsItemFeedsLikes.setText(addLikes);
                                                                                        Picasso.get().load(R.drawable.tab_feed_like_off).into(viewHolder.imageViewFeedsItemFeedsLikes);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });


                                            }
                                        }
                                    });

                        } else {
                            firebaseFirestore.collection("feeds")
                                    .document(feedsDataModel.getFeed_uid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                String stringLikes = task.getResult().getString("feed_like");
                                                long longLikes = Long.valueOf(stringLikes) + 1;
                                                final String addLikes = String.valueOf(longLikes);

                                                HashMap<String, Object> hashMapUpdate = new HashMap<>();
                                                hashMapUpdate.put("feed_like", addLikes);

                                                firebaseFirestore.collection("feeds")
                                                        .document(feedsDataModel.getFeed_uid())
                                                        .update(hashMapUpdate)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    HashMap<String, Object> hashMapUser = new HashMap<>();
                                                                    hashMapUser.put("feed_like_user", currentUser);
                                                                    hashMapUser.put("feed_like_date", Timestamp.now());

                                                                    firebaseFirestore.collection("feeds")
                                                                            .document(feedsDataModel.getFeed_uid())
                                                                            .collection("likes")
                                                                            .document(currentUser)
                                                                            .set(hashMapUser)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        viewHolder.textViewFeedsItemFeedsLikes.setText(addLikes);
                                                                                        Picasso.get().load(R.drawable.tab_feed_like_on).into(viewHolder.imageViewFeedsItemFeedsLikes);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });


                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return arrayFeedsDataModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewFeedsItemFeedsUser;
        TextView textViewFeedsItemFeedsLikes;

        CircleImageView imageViewFeedsItemFeedsThumb;
        ImageView imageViewFeedsItemFeedsCover;

        ImageView imageViewFeedsItemFeedsLikes;

        RelativeTimeTextView relativeTimeFeedsItemFeedsDate;


        ArrayList<Feeds_DataModel> intentFeedsDataModels = new ArrayList<Feeds_DataModel>();
        Context context;

        public ViewHolder(@NonNull View itemView, Context context, ArrayList<Feeds_DataModel> intentFeedsDataModels) {
            super(itemView);
            this.intentFeedsDataModels = intentFeedsDataModels;
            this.context = context;
            itemView.setOnClickListener(this);

            textViewFeedsItemFeedsUser = itemView.findViewById(R.id.textViewFeedsItemFeedsUser);
            textViewFeedsItemFeedsLikes = itemView.findViewById(R.id.textViewFeedsItemFeedsLikes);

            imageViewFeedsItemFeedsThumb = itemView.findViewById(R.id.imageViewFeedsItemFeedsThumb);
            imageViewFeedsItemFeedsCover = itemView.findViewById(R.id.imageViewFeedsItemFeedsCover);

            imageViewFeedsItemFeedsLikes = itemView.findViewById(R.id.imageViewFeedsItemFeedsLikes);

            relativeTimeFeedsItemFeedsDate = itemView.findViewById(R.id.relativeTimeFeedsItemFeedsDate); //Or just use Butterknife!


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Feeds_DataModel intentFeedsDataModel = this.intentFeedsDataModels.get(position);

            Intent intent = new Intent(this.context, ProfileActivity.class);
            intent.putExtra("user_uid", intentFeedsDataModel.getFeed_user());

            this.context.startActivity(intent);
        }


    }

}


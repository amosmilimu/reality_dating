package com.reality.datingapp.firestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reality.datingapp.R;
import com.reality.datingapp.model.Favorites_DataModel;
import com.reality.datingapp.model.Profile_DataModel;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class FavoritesFirestore extends FirestoreRecyclerAdapter<Favorites_DataModel, FavoritesFirestore.FavoritesHolder> {

    long milliseconds;

    private OnItemClickListener listener;

    public FavoritesFirestore(@NonNull FirestoreRecyclerOptions<Favorites_DataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final FavoritesHolder holder, int position, @NonNull final Favorites_DataModel model) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                Profile_DataModel profileDataModel = doc.getDocument().toObject(Profile_DataModel.class);
                                if (profileDataModel.getUser_uid().equals(model.getUser_favorite())) {
                                    holder.textViewFavoritesItemFavoritesName.setText(profileDataModel.getUser_name());

                                    if (profileDataModel.getUser_thumb().equals("thumb")) {
                                        holder.roundedImageViewFavoritesItemFavoritesImage.setImageResource(R.drawable.profile_image);
                                    } else {
                                        Picasso.get().load(profileDataModel.getUser_thumb()).into(holder.roundedImageViewFavoritesItemFavoritesImage);
                                    }

                                }
                            }
                        }
                    }
                });


        holder.RelativeTimeFavoritesItemFavoritesDate.setReferenceTime(model.getUser_favorited().getTime());

    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorites_item, viewGroup, false);
        return new FavoritesHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class FavoritesHolder extends RecyclerView.ViewHolder {

        TextView textViewFavoritesItemFavoritesName;
        TextView textViewFavoritesItemFavoritesUnread;

        RelativeTimeTextView RelativeTimeFavoritesItemFavoritesDate;

        RoundedImageView roundedImageViewFavoritesItemFavoritesImage;


        public FavoritesHolder(@NonNull View itemView) {
            super(itemView);

            textViewFavoritesItemFavoritesName = itemView.findViewById(R.id.textViewFavoritesItemFavoritesName);
            textViewFavoritesItemFavoritesUnread = itemView.findViewById(R.id.textViewFavoritesItemFavoritesUnread);

            RelativeTimeFavoritesItemFavoritesDate = itemView.findViewById(R.id.RelativeTimeFavoritesItemFavoritesDate); //Or just use Butterknife!

            roundedImageViewFavoritesItemFavoritesImage = itemView.findViewById(R.id.roundedImageViewFavoritesItemFavoritesImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }

    }


}

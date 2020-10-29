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
import com.reality.datingapp.model.Likes_DataModel;
import com.reality.datingapp.model.Profile_DataModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;


public class LikesFirestore extends FirestoreRecyclerAdapter<Likes_DataModel, LikesFirestore.LikesHolder> {


    private OnItemClickListener listener;


    public LikesFirestore(@NonNull FirestoreRecyclerOptions<Likes_DataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final LikesHolder holder, int position, @NonNull final Likes_DataModel model) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                Profile_DataModel profileDataModel = doc.getDocument().toObject(Profile_DataModel.class);
                                if (profileDataModel.getUser_uid().equals(model.getUser_likes())) {
                                    holder.textViewLikesItemLikesName.setText(profileDataModel.getUser_name());

                                    if (profileDataModel.getUser_thumb().equals("thumb")) {
                                        holder.roundedImageViewLikesItemLikesImage.setImageResource(R.drawable.profile_image);
                                    } else {
                                        Picasso.get().load(profileDataModel.getUser_thumb()).into(holder.roundedImageViewLikesItemLikesImage);
                                    }
                                }
                            }
                        }
                    }
                });

        SimpleDateFormat sfd = new SimpleDateFormat("d MMMM yyyy, hh:mm a");
        String x = sfd.format(new Date(model.getUser_liked().toString()));

        holder.RelativeTimeLikesItemLikesDate.setReferenceTime(model.user_liked.getTime());

    }

    @NonNull
    @Override
    public LikesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.likes_item, viewGroup, false);
        return new LikesHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class LikesHolder extends RecyclerView.ViewHolder {

        TextView textViewLikesItemLikesName;
        TextView textViewLikesItemLikesMessage;
        TextView textViewLikesItemLikesUnread;
        RoundedImageView roundedImageViewLikesItemLikesImage;
        RelativeTimeTextView RelativeTimeLikesItemLikesDate;

        public LikesHolder(@NonNull View itemView) {
            super(itemView);

            textViewLikesItemLikesName = itemView.findViewById(R.id.textViewLikesItemLikesName);
            textViewLikesItemLikesUnread = itemView.findViewById(R.id.textViewLikesItemLikesUnread);
            RelativeTimeLikesItemLikesDate = itemView.findViewById(R.id.RelativeTimeLikesItemLikesDate); //Or just use Butterknife!
            roundedImageViewLikesItemLikesImage = itemView.findViewById(R.id.roundedImageViewLikesItemLikesImage);

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

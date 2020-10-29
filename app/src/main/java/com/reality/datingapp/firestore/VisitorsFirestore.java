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
import com.reality.datingapp.model.Profile_DataModel;
import com.reality.datingapp.model.Visitors_DataModel;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class VisitorsFirestore extends FirestoreRecyclerAdapter<Visitors_DataModel, VisitorsFirestore.VisitsHolder> {

    long milliseconds;

    private OnItemClickListener listener;

    public VisitorsFirestore(@NonNull FirestoreRecyclerOptions<Visitors_DataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final VisitsHolder holder, int position, @NonNull final Visitors_DataModel model) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                Profile_DataModel profileDataModel = doc.getDocument().toObject(Profile_DataModel.class);
                                if (profileDataModel.getUser_uid().equals(model.getUser_visitor())) {
                                    holder.textViewVisitsItemVisitsName.setText(profileDataModel.getUser_name());

                                    if (profileDataModel.getUser_thumb().equals("thumb")) {
                                        holder.roundedImageViewVisitsItemVisitsImage.setImageResource(R.drawable.profile_image);
                                    } else {
                                        Picasso.get().load(profileDataModel.getUser_thumb()).into(holder.roundedImageViewVisitsItemVisitsImage);
                                    }

                                }
                            }
                        }
                    }
                });


        holder.RelativeTimeVisitsItemVisitsDate.setReferenceTime(model.user_visited.getTime());

    }

    @NonNull
    @Override
    public VisitsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visitors_item, viewGroup, false);
        return new VisitsHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class VisitsHolder extends RecyclerView.ViewHolder {

        TextView textViewVisitsItemVisitsName;
        TextView textViewVisitsItemVisitsUnread;

        RelativeTimeTextView RelativeTimeVisitsItemVisitsDate;

        RoundedImageView roundedImageViewVisitsItemVisitsImage;


        public VisitsHolder(@NonNull View itemView) {
            super(itemView);

            textViewVisitsItemVisitsName = itemView.findViewById(R.id.textViewVisitsItemVisitsName);
            textViewVisitsItemVisitsUnread = itemView.findViewById(R.id.textViewVisitsItemVisitsUnread);

            RelativeTimeVisitsItemVisitsDate = itemView.findViewById(R.id.RelativeTimeVisitsItemVisitsDate); //Or just use Butterknife!

            roundedImageViewVisitsItemVisitsImage = itemView.findViewById(R.id.roundedImageViewVisitsItemVisitsImage);

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

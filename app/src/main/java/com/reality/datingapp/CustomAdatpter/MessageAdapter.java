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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reality.datingapp.R;
import com.reality.datingapp.model.Chats_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    ArrayList<Chats_DataModel> arrayUsersClass;
    Context context;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public MessageAdapter(ArrayList<Chats_DataModel> arrayUsersClass, Context context) {
        this.arrayUsersClass = arrayUsersClass;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT) {

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.message_item_right, viewGroup, false);
            return new ViewHolder(view, context, arrayUsersClass);

        } else {

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.message_item_left, viewGroup, false);
            return new ViewHolder(view, context, arrayUsersClass);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {


        Chats_DataModel userClass = arrayUsersClass.get(i);
        viewHolder.textViewChatMessage.setText(userClass.getChat_message());

        if (userClass.getChat_seenchat().equals("no")) {
            if (userClass.getChat_sender().equals(firebaseUser.getUid())) {
                viewHolder.imageViewChatSent.setVisibility(View.VISIBLE);
                viewHolder.imageViewChatSeen.setVisibility(View.GONE);
            }
        } else {
            if (userClass.getChat_sender().equals(firebaseUser.getUid())) {
                viewHolder.imageViewChatSeen.setVisibility(View.VISIBLE);
                viewHolder.imageViewChatSent.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return arrayUsersClass.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (arrayUsersClass.get(position).getChat_sender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewChatMessage;
        ImageView imageViewChatSent;
        ImageView imageViewChatSeen;
        ArrayList<Chats_DataModel> intentUserClass = new ArrayList<Chats_DataModel>();
        Context context;

        public ViewHolder(@NonNull View itemView, Context context, ArrayList<Chats_DataModel> intentUserClass) {
            super(itemView);
            this.intentUserClass = intentUserClass;
            this.context = context;
            itemView.setOnClickListener(this);

            textViewChatMessage = itemView.findViewById(R.id.textViewChatMessage);
            imageViewChatSent = itemView.findViewById(R.id.imageViewChatSent);
            imageViewChatSeen = itemView.findViewById(R.id.imageViewChatSeen);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Chats_DataModel intentUsersClass = this.intentUserClass.get(position);

            Intent intent = new Intent(this.context, ProfileActivity.class);
            intent.putExtra("user_message", intentUsersClass.getChat_message());

        }


    }
}



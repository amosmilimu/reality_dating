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
import com.reality.datingapp.model.Profile_DataModel;
import com.reality.datingapp.ui.Activity.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ViewHolder> {

    ArrayList<Profile_DataModel> arrayProfileDataModels;
    Context context;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public SwipeAdapter(ArrayList<Profile_DataModel> arrayProfileDataModels, Context context) {
        this.arrayProfileDataModels = arrayProfileDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public SwipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.swipe_item, viewGroup, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        return new ViewHolder(view, context, arrayProfileDataModels);

    }

    @Override
    public void onBindViewHolder(@NonNull SwipeAdapter.ViewHolder viewHolder, int i) {

        Profile_DataModel userClass = arrayProfileDataModels.get(i);


        String userName = userClass.getUser_name();
        String[] splitUserNames = userName.split(" ");
        viewHolder.textViewUsersItemUserName.setText(splitUserNames[0]);

        viewHolder.textViewUsersItemUsersCity.setText(userClass.getUser_city());

        viewHolder.textViewUsersItemUsersAge.setText(", " + userClass.getUser_birthage());


        if (userClass.getUser_cover().equals("cover")) {
            viewHolder.imageViewUsersItemUsersImage.setImageResource(R.drawable.profile_image);
        } else {
            Picasso.get().load(userClass.getUser_cover()).into(viewHolder.imageViewUsersItemUsersImage);
        }

        if (userClass.getUser_status().equals("online")) {
            viewHolder.circleImageViewUsersItemUsersOnline.setVisibility(View.VISIBLE);
            viewHolder.circleImageViewUsersItemUsersOffline.setVisibility(View.GONE);
        } else {
            viewHolder.circleImageViewUsersItemUsersOffline.setVisibility(View.VISIBLE);
            viewHolder.circleImageViewUsersItemUsersOnline.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayProfileDataModels.size();
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

        TextView textViewUsersItemUserName;
        TextView textViewUsersItemUsersAge;
        TextView textViewUsersItemUsersCity;
        TextView textViewUsersItemUsersDistance;
        ImageView imageViewUsersItemUsersImage;
        CircleImageView circleImageViewUsersItemUsersOnline;
        CircleImageView circleImageViewUsersItemUsersOffline;


        ArrayList<Profile_DataModel> intentUserClass = new ArrayList<Profile_DataModel>();
        Context context;

        public ViewHolder(@NonNull View itemView, Context context, ArrayList<Profile_DataModel> intentUserClass) {
            super(itemView);
            this.intentUserClass = intentUserClass;
            this.context = context;
            itemView.setOnClickListener(this);

            textViewUsersItemUserName = itemView.findViewById(R.id.textViewUsersItemUsersName);
            textViewUsersItemUsersCity = itemView.findViewById(R.id.textViewUsersItemUsersCity);
            textViewUsersItemUsersDistance = itemView.findViewById(R.id.textViewUsersItemUsersDistance);
            textViewUsersItemUsersAge = itemView.findViewById(R.id.textViewUsersItemUsersAge);
            imageViewUsersItemUsersImage = itemView.findViewById(R.id.imageViewUsersItemUsersImage);
            circleImageViewUsersItemUsersOnline = itemView.findViewById(R.id.circleImageViewUsersItemUsersOnline);
            circleImageViewUsersItemUsersOffline = itemView.findViewById(R.id.circleImageViewUsersItemUsersOffline);


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Profile_DataModel intentProfileDataModel = this.intentUserClass.get(position);

            Intent intent = new Intent(this.context, ProfileActivity.class);
            intent.putExtra("user_uid", intentProfileDataModel.getUser_uid());

            this.context.startActivity(intent);
        }


    }

}


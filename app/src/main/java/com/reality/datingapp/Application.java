package com.reality.datingapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.reality.datingapp.model.ChatUser_DataModel;
import com.reality.datingapp.ui.Activity.AccountsActivity;
import com.reality.datingapp.ui.Activity.MainActivity;
import com.reality.datingapp.ui.Activity.MessageActivity;

import javax.annotation.Nullable;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class Application extends android.app.Application {

    public static final String CHANNEL_ID_1 = "New Matches";
    public static final String CHANNEL_NAME_1 = "New Matches";
    public static final String CHANNEL_GROUP_1 = "New Matches";
    public static final String CHANNEL_DETAIL_1 = "Check when you have new Match";
    public static final String CHANNEL_ID_2 = "Messages";
    public static final String CHANNEL_NAME_2 = "Messages";
    public static final String CHANNEL_GROUP_2 = "Messages";
    public static final String CHANNEL_DETAIL_2 = "Check new and unread messages";
    public static final String CHANNEL_ID_3 = "New Likes";
    public static final String CHANNEL_NAME_3 = "New Likes";
    public static final String CHANNEL_GROUP_3 = "New Likes";
    public static final String CHANNEL_DETAIL_3 = "Check out who is into you";
    public static final String CHANNEL_ID_4 = "Super Likes";
    public static final String CHANNEL_NAME_4 = "Super Likes";
    public static final String CHANNEL_GROUP_4 = "Super Likes";
    public static final String CHANNEL_DETAIL_4 = "Check who has crush on you";
    public static final String CHANNEL_ID_5 = "New Visits";
    public static final String CHANNEL_NAME_5 = "New Visits";
    public static final String CHANNEL_GROUP_5 = "New Visits";
    public static final String CHANNEL_DETAIL_5 = "Check who is stalking you";
    public static final String CHANNEL_ID_6 = "New Feeds";
    public static final String CHANNEL_NAME_6 = "New Feeds";
    public static final String CHANNEL_GROUP_6 = "New Feeds";
    public static final String CHANNEL_DETAIL_6 = "Check out the recent uploads";
    public static final String CHANNEL_ID_7 = "Uncategorised";
    public static final String CHANNEL_NAME_7 = "Uncategorised";
    public static final String CHANNEL_GROUP_7 = "Uncategorised";
    public static final String CHANNEL_DETAIL_7 = "Check out other notifications";

    public static final String ADDMOB_APP_ID = "ca-app-pub-2961175322148657~8021812010";  //TODO replace this test app

    public static NotificationManagerCompat notificationManagerCompat;
    public static boolean appRunning = false;
    static String user_unread = "0";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    int add;
    int news;
    Bitmap output;

    @Override
    public void onCreate() {
        super.onCreate();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        appRunning = true;

        NotificationChannels();
        StartNotificationMatch();
        StartNotificationChats();
        StartNotificationLikes();
        StartNotificationSuper();
        StartNotificationVisits();


    }


    /**
     * set firbase notification channels id
     */
    private void NotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Match Notification Channel
            NotificationChannel channelMatch = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_HIGH);
            channelMatch.setDescription(CHANNEL_DETAIL_1);
            channelMatch.enableLights(true);
            channelMatch.enableVibration(true);

            //Chats Notification Channel
            NotificationChannel channelChats = new NotificationChannel(
                    CHANNEL_ID_2,
                    CHANNEL_NAME_2,
                    NotificationManager.IMPORTANCE_HIGH);
            channelChats.setDescription(CHANNEL_DETAIL_2);
            channelChats.enableLights(true);
            channelChats.enableVibration(true);

            //Likes Notification Channel
            NotificationChannel channelLikes = new NotificationChannel(
                    CHANNEL_ID_3,
                    CHANNEL_NAME_3,
                    NotificationManager.IMPORTANCE_HIGH);
            channelLikes.setDescription(CHANNEL_DETAIL_3);
            channelLikes.enableLights(true);
            channelLikes.enableVibration(true);

            //Super Notification Channel
            NotificationChannel channelSuper = new NotificationChannel(
                    CHANNEL_ID_4,
                    CHANNEL_NAME_4,
                    NotificationManager.IMPORTANCE_HIGH);
            channelSuper.setDescription(CHANNEL_DETAIL_4);
            channelSuper.enableLights(true);
            channelSuper.enableVibration(true);

            //Visits Notification Channel
            NotificationChannel channelVisits = new NotificationChannel(
                    CHANNEL_ID_5,
                    CHANNEL_NAME_5,
                    NotificationManager.IMPORTANCE_HIGH);
            channelVisits.setDescription(CHANNEL_DETAIL_5);
            channelVisits.enableLights(true);
            channelVisits.enableVibration(true);

            //Feeds Notification Channel
            NotificationChannel channelFeeds = new NotificationChannel(
                    CHANNEL_ID_6,
                    CHANNEL_NAME_6,
                    NotificationManager.IMPORTANCE_HIGH);
            channelFeeds.setDescription(CHANNEL_DETAIL_6);
            channelFeeds.enableLights(true);
            channelFeeds.enableVibration(true);

            //Other Notification Channel
            NotificationChannel channelOther = new NotificationChannel(
                    CHANNEL_ID_7,
                    CHANNEL_NAME_7,
                    NotificationManager.IMPORTANCE_HIGH);
            channelOther.setDescription(CHANNEL_DETAIL_7);
            channelOther.enableLights(true);
            channelOther.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelMatch);
            notificationManager.createNotificationChannel(channelChats);
            notificationManager.createNotificationChannel(channelLikes);
            notificationManager.createNotificationChannel(channelSuper);
            notificationManager.createNotificationChannel(channelVisits);
            notificationManager.createNotificationChannel(channelFeeds);
            notificationManager.createNotificationChannel(channelOther);
        }

    }

    /**
     * send matched profile notifications
     */
    private void StartNotificationMatch() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("matches")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_match").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationMatch();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_match").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationMatch();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show the matched profile notification
     */
    private void ShowNotificationMatch() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_matches");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

            Notification newMessageNotificationMatch = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                    .setSmallIcon(setNotificationIcon())
                    .setLargeIcon(CircleBitmap(bitmapLogo))
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setColor(getResources().getColor(R.color.colorPink))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("You have new match")
                    .setContentText("Someone matched you! Tap here to find out who!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setGroup(CHANNEL_GROUP_1)
                    .build();

            notificationManagerCompat.notify(1, newMessageNotificationMatch);

    }


    /**
     * send like notifcation
     */
    private void StartNotificationLikes() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("likes")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_likes").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationLikes();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_likes").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationLikes();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show liks notification
     */
    private void ShowNotificationLikes() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_likes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationLikes = new NotificationCompat.Builder(this, CHANNEL_ID_3)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(CircleBitmap(bitmapLogo))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new likes")
                .setContentText("Someone liked you! Tap here to find out who!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_3)
                .build();

        notificationManagerCompat.notify(3, newMessageNotificationLikes);
    }


    /**
     * send supper like notification
     */
    private void StartNotificationSuper() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("likes")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_super").equals("yes")) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super != null && user_super.equals("yes")) {
                                                                    if (!appRunning) {
                                                                        ShowNotificationSuper();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_super").equals("yes")) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super != null && user_super.equals("yes")) {
                                                                    if (!appRunning) {
                                                                        ShowNotificationSuper();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show supper like notification
     */
    private void ShowNotificationSuper() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_likes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationSuper = new NotificationCompat.Builder(this, CHANNEL_ID_4)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(CircleBitmap(bitmapLogo))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new super like")
                .setContentText("Someone super liked you! Tap here to find out!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_4)
                .build();

        notificationManagerCompat.notify(4, newMessageNotificationSuper);
    }


    /**
     * send notification for visits
     */
    private void StartNotificationVisits() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("visits")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_visits").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationVisits();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_visits").equals("yes")) {
                                                                if (!appRunning) {
                                                                    ShowNotificationVisits();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show notification for visits
     */
    private void ShowNotificationVisits() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_visitors");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationVisits = new NotificationCompat.Builder(this, CHANNEL_ID_5)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(CircleBitmap(bitmapLogo))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new visitor")
                .setContentText("Someone visited you! Tap here to find out who!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_5)
                .build();

        notificationManagerCompat.notify(5, newMessageNotificationVisits);
    }

    /**
     * send notifcation for Chats
     */
    private void StartNotificationChats() {

        if (firebaseUser != null) {

            final String currentUser = firebaseUser.getUid();

            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("chats")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {

                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        ChatUser_DataModel chatUserDataModel = doc.getDocument().toObject(ChatUser_DataModel.class);

                                        if (!chatUserDataModel.getUser_unread().equals("0")) {

                                            String user_message = chatUserDataModel.getUser_message();
                                            String user_receiver = chatUserDataModel.getUser_receiver();
                                            String user_unread_new = chatUserDataModel.getUser_unread();

                                            CheckNotificationsChats(currentUser, user_message, user_receiver);
                                        }
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {

                                        ChatUser_DataModel chatUserDataModel = doc.getDocument().toObject(ChatUser_DataModel.class);

                                        if (!chatUserDataModel.getUser_unread().equals("0")) {

                                            String user_message = chatUserDataModel.getUser_message();
                                            String user_receiver = chatUserDataModel.getUser_receiver();
                                            String user_unread_new = chatUserDataModel.getUser_unread();


                                            CheckNotificationsChats(currentUser, user_message, user_receiver);
                                        }


                                    }
                                }
                            }
                        }
                    });
        }

    }

    /**
     * @param currentUser   current User
     * @param user_message  user message
     * @param user_receiver user receiver
     */
    private void CheckNotificationsChats(final String currentUser, final String user_message, String user_receiver) {

        firebaseFirestore.collection("users")
                .document(user_receiver)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String user_status = task.getResult().getString("user_status");
                            String user_name = task.getResult().getString("user_name");
                            final String user_uid = task.getResult().getString("user_uid");

                            String[] splitName = user_name.split(" ");
                            final String userTitle = splitName[0];

                            String uids = user_uid.replaceAll("[^0-9]", "");
                            final int uid = Integer.valueOf(uids);

                            if (!appRunning) {

                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getString("alert_chats").equals("yes")) {
                                                        ShowNotificationChats(userTitle, user_message, user_uid, uid, MessageActivity.class);
                                                    }
                                                }
                                            }
                                        });

                            }

                        }
                    }
                });
    }


    /**
     * @param stringTitle
     * @param stringContent
     * @param user_uid
     * @param uid
     * @param classActivity
     */
    private void ShowNotificationChats(String stringTitle, String stringContent,
                                       String user_uid, int uid, Class classActivity) {


        Intent intent = new Intent(this, classActivity);
        intent.putExtra("user_uid", user_uid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);

        Intent intentChat = new Intent(this, MainActivity.class);
        PendingIntent chatPending = PendingIntent.getActivity(this, 0, intentChat, FLAG_UPDATE_CURRENT);

        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationChats = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(CircleBitmap(bitmapLogo))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(stringTitle)
                .setContentText(stringContent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_2)
                .build();

        Notification summaryNotificationChats = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(setNotificationIcon())
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle("New Chat Messages")
                        .setSummaryText("New Chat Messages"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorPink))
                .setGroup(CHANNEL_GROUP_2)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setContentIntent(chatPending)
                .build();


        notificationManagerCompat.notify(uid, newMessageNotificationChats);
        notificationManagerCompat.notify(2, summaryNotificationChats);

    }

    /**
     * @return set notification icon
     */
    private int setNotificationIcon() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.notify_icon;
        } else {
            return R.drawable.notify_icon;
        }
    }

    /**
     * @param bitmap convert bitmap to cercle
     * @return
     */
    private Bitmap CircleBitmap(Bitmap bitmap) {
        try {
            output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();
        }catch (Exception e){
            Log.e("Application", "CircleBitmap: "+e.getMessage());
        }

        return output;
    }


}



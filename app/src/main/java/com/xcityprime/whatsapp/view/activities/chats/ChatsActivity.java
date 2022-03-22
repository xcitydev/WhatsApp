package com.xcityprime.whatsapp.view.activities.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.adapter.ChatAdapter;
import com.xcityprime.whatsapp.databinding.ActivityChatsBinding;
import com.xcityprime.whatsapp.model.chat.Chats;
import com.xcityprime.whatsapp.view.activities.profile.UserProfileActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String receiverID;
    private ChatAdapter adapter;
    private List<Chats> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");


        if (receiverID != null){
            binding.tvUserNameChat.setText(userName);

           /* if (userProfile != null) {
                if (userProfile.equals("")){
                    binding.tvChatImg.setImageResource(R.drawable.ic_male_placeholder); //setDefault img when profile is  null
                }else{*/
                    Glide.with(this).load(userProfile).into(binding.tvChatImg);
                //}
           // }
        }
        binding.goBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.sendMessageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.sendMessageBox.getText().toString())){
                    binding.voice1.setImageDrawable(getDrawable(R.drawable.ic_mic));
                }else {
                    binding.voice1.setImageDrawable(getDrawable(R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initClicked();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recycleViewChat.setLayoutManager(layoutManager);

        readChats();
    }

    private void readChats() {
        try {
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
            reference2.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Chats chats = snapshot1.getValue(Chats.class);
                        if (chats != null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverID)
                        || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverID)
                        ) {
                            list.add(chats);
                        }
                    }
                    if (adapter != null){
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter = new ChatAdapter(ChatsActivity.this, list);
                        binding.recycleViewChat.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    private void initClicked(){
        binding.voice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.sendMessageBox.getText().toString())){
                    sendTextMessage(binding.sendMessageBox.getText().toString());

                    binding.sendMessageBox.setText("");
                }
            }
        });
        binding.goBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class));
            }
        });

    }


    private void sendTextMessage(String message){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String today = format1.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat cDt = new SimpleDateFormat("hh:mm a");
        String currentTime = cDt.format(currentDateTime.getTime());

        Chats chats = new Chats(
                today +","+currentTime,
                message,
                "TEXT",
                firebaseUser.getUid(),
                receiverID
        );
        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("send", "onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("send", "onFailure"+e.getMessage());
            }
        });

        // add to chatLists

        DatabaseReference chatref1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
        chatref1.child("chatId").setValue(receiverID);

        DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
        chatref2.child("chatId").setValue(firebaseUser.getUid());
    }
}
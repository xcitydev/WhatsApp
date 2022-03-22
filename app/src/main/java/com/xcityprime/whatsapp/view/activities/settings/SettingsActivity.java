package com.xcityprime.whatsapp.view.activities.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.databinding.ActivitySettingsBinding;
import com.xcityprime.whatsapp.view.activities.profile.ProfileActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore fireStore;
    private ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        if (firebaseUser != null){
            getInfo();
        }
        inClickAction();
    }

    private void inClickAction() {
        binding.profileLayMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });
    }

    private void getInfo(){
        fireStore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = Objects.requireNonNull(documentSnapshot.get("userName")).toString();
                String imageProfile = Objects.requireNonNull(documentSnapshot.get("imageProfile")).toString();
                Glide.with(SettingsActivity.this).load(imageProfile).into(binding.profileImg1);
                binding.txtUserName.setText(userName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Get Data", "onFailure: "+e.getMessage());
            }
        });
    }
}
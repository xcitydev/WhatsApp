package com.xcityprime.whatsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.model.user.Users;
import com.xcityprime.whatsapp.view.activities.chats.ChatsActivity;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Holder> {
    private List<Users> usersList;
    private Context context;

    public ContactsAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.Holder holder, int position) {
        final Users users = usersList.get(position);

        holder.username_contacts1.setText(users.getUserName());
        holder.desc_contacts1.setText(users.getBio());

        Glide.with(context).load(users.getImageProfile()).into(holder.imageButton);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class).putExtra("userID",users.getUserID())
                        .putExtra("userName", users.getUserName())
                        .putExtra("userProfile", users.getImageProfile()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CircularImageView imageButton;
        private TextView username_contacts1, desc_contacts1;
        public Holder(@NonNull View itemView) {
            super(itemView);
            desc_contacts1 = itemView.findViewById(R.id.txtDesc_contacts);
            username_contacts1 = itemView.findViewById(R.id.txtUsername_contacts);
            imageButton = itemView.findViewById(R.id.image1_contacts);

        }
    }
}

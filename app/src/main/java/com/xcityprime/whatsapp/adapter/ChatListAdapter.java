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
import com.xcityprime.whatsapp.model.Chatlist;
import com.xcityprime.whatsapp.view.activities.chats.ChatsActivity;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder>{
    private List<Chatlist> list;
    private Context context;

    public ChatListAdapter(List<Chatlist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Holder holder, int position) {
        final Chatlist chatlist = list.get(position);

        holder.tvName.setText(chatlist.getUsername());
        holder.tvDesc.setText(chatlist.getDescription());
        holder.tvDate.setText(chatlist.getDate());

       /* if (chatlist.getUrlProfile().equals("")){
            holder.profile.setImageResource(R.drawable.ic_male_placeholder); //setDefault picture
        }else{*/
            Glide.with(context).load(chatlist.getUrlProfile()).into(holder.profile);
       // }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class).putExtra("userID",chatlist.getUserID())
                        .putExtra("userName", chatlist.getUsername())
                        .putExtra("userProfile", chatlist.getUrlProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDesc, tvDate;
        private CircularImageView profile;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.txtDate);
            tvName = itemView.findViewById(R.id.txtUsername);
            tvDesc = itemView.findViewById(R.id.txtDesc);

            profile = itemView.findViewById(R.id.image1);
        }
    }
}

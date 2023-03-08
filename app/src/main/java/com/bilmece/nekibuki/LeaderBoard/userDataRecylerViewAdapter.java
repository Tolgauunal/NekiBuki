package com.bilmece.nekibuki.LeaderBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bilmece.nekibuki.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class userDataRecylerViewAdapter extends RecyclerView.Adapter<com.bilmece.nekibuki.LeaderBoard.userDataRecylerViewAdapter.userDataHolder> {

    Context context;
    ArrayList<userData> userDataArrayList;

    public userDataRecylerViewAdapter(Context context, ArrayList<userData> userDataArrayList) {
        this.context = context;
        this.userDataArrayList = userDataArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public com.bilmece.nekibuki.LeaderBoard.userDataRecylerViewAdapter.userDataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
        return new com.bilmece.nekibuki.LeaderBoard.userDataRecylerViewAdapter.userDataHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull com.bilmece.nekibuki.LeaderBoard.userDataRecylerViewAdapter.userDataHolder holder, int position) {
        userData userData = userDataArrayList.get(position);
        if (position == 0) {
            holder.email.setTextColor(Color.parseColor("#b22222"));
            holder.puan.setTextColor(Color.parseColor("#b22222"));
            holder.siralamaNu.setTextColor(Color.parseColor("#b22222"));
        } else if (position == 1) {
            holder.email.setTextColor(Color.parseColor("#ffdf00"));
            holder.puan.setTextColor(Color.parseColor("#ffdf00"));
            holder.siralamaNu.setTextColor(Color.parseColor("#ffdf00"));
        } else if (position == 2) {
            holder.email.setTextColor(Color.parseColor("#40cfff"));
            holder.puan.setTextColor(Color.parseColor("#40cfff"));
            holder.siralamaNu.setTextColor(Color.parseColor("#40cfff"));
        } else {
            holder.email.setTextColor(Color.parseColor("#808080"));
            holder.puan.setTextColor(Color.parseColor("#808080"));
            holder.siralamaNu.setTextColor(Color.parseColor("#808080"));
        }
        holder.email.setText(userData.email);
        holder.puan.setText(String.valueOf(userData.puan));
        holder.siralamaNu.setText((position + 1) + "- ");

    }

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }

    public static class userDataHolder extends RecyclerView.ViewHolder {
        TextView email, puan, siralamaNu;

        public userDataHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.mail);
            puan = itemView.findViewById(R.id.puan);
            siralamaNu = itemView.findViewById(R.id.sıralamaNumarası);
        }
    }
}
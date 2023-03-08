package com.bilmece.nekibuki.Categories;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bilmece.nekibuki.R;
import org.jetbrains.annotations.NotNull;

public class MyHolder extends RecyclerView.ViewHolder {
    ImageView mimageView;
    CheckedTextView mtitle;
    int position;
    View view ;
    CardView cardView;
    public MyHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        view=itemView;
        cardView=itemView.findViewById(R.id.kategoriCardview);
        mimageView=itemView.findViewById(R.id.categories_ImageView);
        mtitle= itemView.findViewById(R.id.categories_textView);
    }
}

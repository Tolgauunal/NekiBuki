package com.bilmece.nekibuki.Categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bilmece.nekibuki.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context context;
    ArrayList<Model> mModels;
    ChangeStatusListener changeStatusListener;
    String pozisyon, soru;
    int i = 1;
    SQLiteDatabase database;
    String sqlSorgusu;
    SQLiteStatement statement;
    public MyAdapter(Context context, ArrayList<Model> mModels, ChangeStatusListener changeStatusListener) {
        this.context = context;
        this.mModels = mModels;
        this.changeStatusListener = changeStatusListener;
    }

    public interface ChangeStatusListener {
        void onItemChangeListener(int position, Model model);
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoriescardviewdesign, null);
        sqlcreate();
        return new MyHolder(view);
    }
    private void sqlcreate() {
        try {
            database = context.openOrCreateDatabase("Kategoriler1", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Kategoriler1(id INTEGER,kategoriId VARCHAR,soruu VARCHAR)");
            database.execSQL("DELETE FROM Kategoriler1");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sqlInsert() {
        try {
            sqlSorgusu = "INSERT INTO Kategoriler1 (id,kategoriId,soruu) VALUES (?,?,?)";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindString(1, String.valueOf(i));
            statement.bindString(2, String.valueOf(pozisyon));
            statement.bindString(3, String.valueOf(soru));
            statement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void sqlDelete() {
        try {
            database.execSQL("DELETE FROM Kategoriler1 WHERE kategoriId=?", new Object[]{pozisyon});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Model model = mModels.get(position);
        if (model != null) {
            holder.position = position;
            pozisyon = mModels.get(position).getCode();
            soru = mModels.get(position).getSoru();
            holder.mtitle.setText(mModels.get(position).getTitle());
            holder.mimageView.setImageResource(mModels.get(position).getImg());
            holder.cardView.setCardBackgroundColor(Color.parseColor(mModels.get(position).getColor()));
            if (model.isSelect()) {
                holder.mtitle.setTextColor(Color.BLACK);
                sqlInsert();
            } else {
                holder.mtitle.setTextColor(Color.parseColor("#A9A9A9"));
                sqlDelete();
            }
        }
        holder.view.setOnClickListener(v -> {
            Model model1 = mModels.get(position);
            if (model1.isSelect()) {
                model1.setSelect(false);
            } else {
                model1.setSelect(true);
            }
            mModels.set(holder.position, model1);
            if (changeStatusListener != null) {
                changeStatusListener.onItemChangeListener(holder.position, model1);
            }

            notifyItemChanged(holder.position);
        });
    }
    @Override
    public int getItemCount() {
        if (mModels != null) {
            return mModels.size();
        }
        return 0;
    }
}

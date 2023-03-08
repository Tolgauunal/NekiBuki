package com.bilmece.nekibuki.Categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bilmece.nekibuki.MenuActivity.Home;
import com.bilmece.nekibuki.MenuActivity.PlayActivity;
import com.bilmece.nekibuki.R;

import java.util.ArrayList;


public class Kategori extends AppCompatActivity implements MyAdapter.ChangeStatusListener {
    RecyclerView kategoriRecylerView;
    MyAdapter myAdapter;
    ImageView catOnay;
    ArrayList<Model> models = new ArrayList<>();
    SQLiteDatabase database2;
    ArrayList<String> SecilenToplamKategori;
    Cursor cursor;
    ImageView backpres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        database2 = this.openOrCreateDatabase("Kategoriler1", MODE_PRIVATE, null);
        kategoriRecylerView = findViewById(R.id.kategoriRecylerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        kategoriRecylerView.setLayoutManager(manager);
        myAdapter = new MyAdapter(Kategori.this, getMylist(), this);
        kategoriRecylerView.setAdapter(myAdapter);
        catOnay = findViewById(R.id.deneme);
        catOnay.setOnClickListener(v -> secilenKategoriler());
        SecilenToplamKategori = new ArrayList<>();
        backpres = findViewById(R.id.backPress);
        backpres.setOnClickListener(v -> {
            Intent playIntent = new Intent(Kategori.this, Home.class);
            finish();
            startActivity(playIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Kategori.this, Home.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private ArrayList<Model> getMylist() {
        ArrayList<Model> models = new ArrayList<>();
        Model m = new Model();
        m.setSoru("Bu Bir Yiyecek Veya Yemek Sizce Ne ki Bu ki ?");
        m.setCode("yiyecekS1");
        m.setTitle("Yiyecek");
        m.setImg(R.drawable.fruits);
        m.setColor("#FAEBD7");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Bir Eşya Sizce Ne ki Bu ki ?");
        m.setCode("esyaS1");
        m.setTitle("Eşya");
        m.setImg(R.drawable.furnitures);
        m.setColor("#F5F5DC");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Bir Hayvan Sizce Ne ki Bu ki ?");
        m.setCode("hayvanS1");
        m.setTitle("Hayvan");
        m.setImg(R.drawable.koala);
        m.setColor("#FFE4C4");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Türkiye'de Bulunan Bir İl Sizce Ne ki Bu ki ?");
        m.setCode("illerS1");
        m.setTitle("İller");
        m.setImg(R.drawable.turkey);
        m.setColor("#FFFAF0");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Bir Ülke Sizce Ne ki Bu ki ?");
        m.setCode("ulkelerS1");
        m.setTitle("Ülke");
        m.setImg(R.drawable.dunya);
        m.setColor("#F8F8FF");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Bir Ünlü Veya Sanatçı Sizce Ne ki Bu ki ?");
        m.setCode("sanatciS1");
        m.setTitle("Ünlü");
        m.setImg(R.drawable.fame);
        m.setColor("#F0FFF0");
        models.add(m);
        m = new Model();
        m.setSoru("Bu Bir Spor Kulubü Veya Spor Branşı Sizce Ne ki Bu ki ?");
        m.setCode("sporS1");
        m.setTitle("Spor");
        m.setImg(R.drawable.sports);
        m.setColor("#FFFFF0");
        models.add(m);
        return models;
    }

    @Override
    public void onItemChangeListener(int position, Model model) {
        try {
            models.set(position, model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void secilenKategoriler() {
        cursor = database2.rawQuery("SELECT * FROM Kategoriler1", null);
        SecilenToplamKategori.clear();
        int kategoriInx = cursor.getColumnIndex("kategoriId");
        while (cursor.moveToNext()) {
            SecilenToplamKategori.add(cursor.getString(kategoriInx));
        }
        if (SecilenToplamKategori.size() <= 2) {
            Toast.makeText(this, "En Az 3 Tane Kategori Seçmeniz Gerekiyor.", Toast.LENGTH_SHORT).show();
        } else {
            Intent playIntent = new Intent(this, PlayActivity.class);
            finish();
            startActivity(playIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
    }
}

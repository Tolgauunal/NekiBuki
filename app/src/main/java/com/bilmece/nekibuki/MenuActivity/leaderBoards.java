package com.bilmece.nekibuki.MenuActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bilmece.nekibuki.LeaderBoard.userData;
import com.bilmece.nekibuki.LeaderBoard.userDataRecylerViewAdapter;
import com.bilmece.nekibuki.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class leaderBoards extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    ArrayList<userData> arrayList;
    RecyclerView recyclerView;
    userDataRecylerViewAdapter RecylerViewAdapter;
    CircleImageView profilPhoto;
    SQLiteDatabase database;
    Cursor cursor;
    int resimIndexi;
    byte[] resimByte;
    TextView leaderBoardNickname;
    String gelenNickName;
    HashMap hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_boards);
        firebaseFirestore = FirebaseFirestore.getInstance();
        profilPhoto = findViewById(R.id.leaderBoard_ImageViev);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.leaderBoardrecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecylerViewAdapter = new userDataRecylerViewAdapter(leaderBoards.this, arrayList);
        recyclerView.setAdapter(RecylerViewAdapter);
        try {
            database = this.openOrCreateDatabase("NekiBuki", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT k_image FROM Ayarlar", null);
            resimIndexi = cursor.getColumnIndex("k_image");
            cursor.moveToFirst();
            resimByte = cursor.getBlob(resimIndexi);
            Bitmap gelenResimBitmap = BitmapFactory.decodeByteArray(resimByte, 0, resimByte.length);
            profilPhoto.setImageBitmap(gelenResimBitmap);
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        getData();
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        TextView txtpuan, txtSiralama;
        Intent nicknameIntent = getIntent();
        txtpuan = findViewById(R.id.leaderBoard_puan);
        txtSiralama = findViewById(R.id.leaderBoard_Sıralama);
        gelenNickName = nicknameIntent.getStringExtra("nickname");
        leaderBoardNickname = findViewById(R.id.leaderboard_nickname);
        leaderBoardNickname.setText(gelenNickName);
        firebaseFirestore.collection("Kullanıcılar").orderBy("Puan", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            arrayList.clear();
            if (error != null) {
                Toast.makeText(leaderBoards.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            int i = 0;
            if (value != null) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    i++;
                    hm = (HashMap) snapshot.getData();
                    String nickName = (String) hm.get("NickName");
                    long puan = (long) hm.get("Puan");
                    if (nickName!=null&& nickName.matches(leaderBoardNickname.getText().toString())) {
                        int puann = (int) puan;
                        txtSiralama.setText(String.valueOf(puann));
                        txtpuan.setText(i + ".");
                    }
                    userData gelenDegerler = new userData(nickName, puan);
                    arrayList.add(gelenDegerler);
                }
                RecylerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
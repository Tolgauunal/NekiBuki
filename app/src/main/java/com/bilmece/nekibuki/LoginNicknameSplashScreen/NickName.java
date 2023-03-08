package com.bilmece.nekibuki.LoginNicknameSplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bilmece.nekibuki.R;
import com.bilmece.nekibuki.databinding.ActivityNickNameBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NickName extends AppCompatActivity {
    private ActivityNickNameBinding binding;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView nickname;
    String GirilenNickname;
    ArrayList<String> nicknameArraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNickNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Değişken ve Firebase bağlantıları
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        nickname=findViewById(R.id.nickName);
        nicknameArraylist=new ArrayList<>();
        nickNameAddArray();
        //Save butonuna basıldığında çalışacak kodlar
        binding.Save.setOnClickListener(v -> nicknameCheck());
    }
    private void nickNameAddArray() {
        //Firebase de kayıtlı olan kullanıcıların nicknamelerine erişip hepsinin bir dizi içerisine kayıt edilmesi
        firebaseFirestore.collection("Kullanıcılar").addSnapshotListener((value, error) -> {
            if (error!=null){
                Toast.makeText(NickName.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }if (value!=null){
                for (DocumentSnapshot snapshot:value.getDocuments()){
                    Map<String, Object> data=snapshot.getData();
                    String kayitliNickname=(String) data.get("NickName");
                    nicknameArraylist.add(kayitliNickname);
                }
            }
        });
    }
    //Bizim girdiğimiz nicknamein firebasede olup olmadığının kontrolü
    private void nicknameCheck() {
        //EditTexte olan nicnkname erişim sağlıyoruz.
        GirilenNickname=nickname.getText().toString();
        //Boolean yardımıyla dizi ve edittexti kontrol ediyoruz
        boolean kontrol=nicknameArraylist.contains(GirilenNickname);
        if (kontrol){
        Toast.makeText(NickName.this,"Bu Kullanıcı Adı Kullanılıyor",Toast.LENGTH_LONG).show();
        }else{//Firebasede böyle bir nickname yoksa yapılacaklar
            FirebaseUser user=firebaseAuth.getCurrentUser();
            String email=user.getEmail();
            int puan=0;
            Map<String,Object> nickName1=new HashMap<>();
            nickName1.put("Email",email);
            firebaseFirestore.collection("Kullanıcılar2").document(GirilenNickname).set(nickName1).addOnSuccessListener(unused -> {
            }).addOnFailureListener(e -> {
            });
            Map<String,Object> nickName=new HashMap<>();
            nickName.put("Email",email);
            nickName.put("NickName", GirilenNickname);
            nickName.put("Puan",puan);
            firebaseFirestore.collection("Kullanıcılar").document(email).set(nickName).addOnSuccessListener(unused -> {
                Toast.makeText(NickName.this,"Kayıt Başarılı",Toast.LENGTH_LONG).show();
                startActivity(new Intent(NickName.this, SplashScreenActivity.class));
                finish();
            }).addOnFailureListener(e -> Toast.makeText(NickName.this,"Kayıt Başarısız",Toast.LENGTH_LONG).show());
        }
    }
}

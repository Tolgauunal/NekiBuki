package com.bilmece.nekibuki.MenuActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bilmece.nekibuki.LoginNicknameSplashScreen.SplashScreenActivity;
import com.bilmece.nekibuki.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    //Sql Bağlantıları ve sorulara erişim
    SQLiteDatabase database, database2;
    SQLiteStatement statement;
    Cursor cursor;
    TextView userHeart, soru, cevap;
    ArrayList<String> sorularList, sorularKodList, kelimelerList;
    ArrayList<Character> kelimeHarfleri;
    Random rndSoru, rndKelime, rndHarf, rndYerRandom;
    int rndSoruNumber, rndKelimeNumber, rndHarfNumber, rastgeleBelirlenecekHarfSayisi, heartIndex, heartCount, sonHaksayisi;
    String rastgeleSoru, rastgeleSoruKodu, rastgeleKelime, kelimeBilgisi, txtTahminDegeri, sqlSorgusu;
    EditText edittxtTahminiCevap;
    TextView rndmYerKelime;
    ImageView refreshRndKelime;
    //Çıkış Alert
    AlertDialog.Builder alert;
    Handler hndler = new Handler();
    //İstatistik Dialog
    Dialog statisticDialog;
    TextView statisticWordCount, statisticFalseCount;
    ProgressBar statisticBarWordCount, statisticBarFalseCount;
    ImageView statisticImageClose, statisticTableShow;
    LinearLayout statisticTableLinearLayout;
    Button statisticTableBtnMainmenu, statisticTableBtnPlayAgain, harfAl;
    WindowManager.LayoutParams params;
    int cozulenSoruSayisi = 0;
    int cozulenKelimeSayisi = 0, yapilanYanlisSayisi = 0, maksimumKelimeSayisi;
    //Puan
    TextView txtPuan;
    FirebaseAuth firebaseAuth;
    FirebaseUser userid;
    FirebaseFirestore firebaseFirestore;
    int guncelPuan, firebasepuan, kontrol;
    String email;
    //Reklam
    AdView mAdView;
    ImageView playreklamGetir;
    RewardedAd mRewardedAd;
    AdRequest adRequest;
    Dialog getplayHeartDialog;
    ImageView getplayHeartImgClose, playimageShowAndGet;
    int sonHakSayisi;
    ArrayList<String> soruparolasi, sorununKendisi;
    int dogruCevap = 0, comboAktivite = 0;
    private InterstitialAd mInterstitialAd;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    //Kalp Animasyonu
    ConstraintLayout constraintLayout;
    ConstraintLayout.LayoutParams heartParams;
    ImageView imgHeart, imgHeartKon;
    Bitmap imgHeartBitmap;
    float imgHeartXPos, imgHeartYPos;
    int imgHeartDuration = 2500;
    ObjectAnimator objectAnimatorImgHeartX, objectAnimatorImgHeartY;
    AnimatorSet animatorSet;
    //Combo Animasyonu
    ImageView comboImage, comboImageKonum;
    Bitmap comboBitmap;
    float comboImageXPos, comboImageYPos;
    int comboImageDuration = 2500;
    ObjectAnimator objectAnimatorComboX, objectAnimatorComboY;
    ConstraintLayout.LayoutParams comboParams;
    CardView cardView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playreklamGetir = findViewById(R.id.play_activityReklamGetir);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        adRequest = new AdRequest.Builder().build();
        reklamiYukle();
        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.play_activity_banner);
        AdRequest playBanner = new AdRequest.Builder().build();
        mAdView.loadAd(playBanner);
        playreklamGetir.setOnClickListener(v -> canKazanmaMenusu());
        sorularList = new ArrayList<>();
        sorularKodList = new ArrayList<>();
        kelimelerList = new ArrayList<>();
        soruparolasi = new ArrayList<>();
        sorununKendisi = new ArrayList<>();
        rndSoru = new Random();
        rndKelime = new Random();
        rndHarf = new Random();
        rndYerRandom = new Random();
        rndmYerKelime = findViewById(R.id.play_activity_rndmKelime);
        soru = findViewById(R.id.play_activity_textViewQuestion);
        cevap = findViewById(R.id.play_activity_textViewQuest);
        edittxtTahminiCevap = findViewById(R.id.play_activity_editText);
        userHeart = findViewById(R.id.play_activity_canSayısı);
        statisticTableShow = findViewById(R.id.play_activity_statisticImage);
        txtPuan = findViewById(R.id.play_activity_puanSayisi);
        refreshRndKelime = findViewById(R.id.play_activity_refreshKelime);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        harfAl = findViewById(R.id.harfAl);
        cardView = findViewById(R.id.play_activity_cardViewQuestion);
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser();
        if (userid != null) {
            email = userid.getEmail();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        puanSayisiniGetir();
        //Can sayısı
        try {
            database = this.openOrCreateDatabase("NekiBuki", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT k_heart FROM Ayarlar", null);
            heartIndex = cursor.getColumnIndex("k_heart");
            cursor.moveToFirst();
            heartCount = Integer.parseInt(cursor.getString(heartIndex));
            userHeart.setText("+" + heartCount);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //SplashScreenActivity de bulunan hashmaplere ulaşıyoruz.
        for (Map.Entry soru : SplashScreenActivity.sorularHashmap.entrySet()) {
            sorularList.add(String.valueOf(soru.getValue()));
            sorularKodList.add(String.valueOf(soru.getKey()));
        }
        statisticTableShow.setOnClickListener(v -> maksimumVerileriHesapla(""));
        database2 = this.openOrCreateDatabase("Kategoriler1", MODE_PRIVATE, null);
        Cursor cursor;
        cursor = database2.rawQuery("SELECT * FROM Kategoriler1", null);

        while (cursor.moveToNext()) {
            sorularKodList.clear();
            sorularList.clear();
            int kategoriInx = cursor.getColumnIndex("kategoriId");
            int soruInx = cursor.getColumnIndex("soruu");
            sorununKendisi.add(cursor.getString(soruInx));
            soruparolasi.add(cursor.getString(kategoriInx));
            for (int i = 0; i < soruparolasi.size(); i++) {
                sorularKodList.add(soruparolasi.get(i));
                sorularList.add(sorununKendisi.get(i));
            }
        }
        cursor.close();
        rndSoruGetir();
        gecisReklaminiYukle();
        refreshRndKelime.setOnClickListener(v -> kelimeyiRastgeleYerlestir());
        //Animasyonlar
        constraintLayout = findViewById(R.id.playCons);
        imgHeartKon = findViewById(R.id.playHeart_design);
        imgHeart = new ImageView(PlayActivity.this);
        imgHeartBitmap = BitmapFactory.decodeResource(PlayActivity.this.getResources(), R.drawable.heart);
        imgHeart.setImageBitmap(imgHeartBitmap);
        heartParams = new ConstraintLayout.LayoutParams(64, 64);
        imgHeart.setLayoutParams(heartParams);
        imgHeart.setX(0);
        imgHeart.setY(0);
        imgHeart.setVisibility(View.INVISIBLE);
        constraintLayout.addView(imgHeart);
        comboImageKonum = findViewById(R.id.play_activity_puan);
        comboImage = new ImageView(PlayActivity.this);
        comboBitmap = BitmapFactory.decodeResource(PlayActivity.this.getResources(), R.drawable.combo);
        comboImage.setImageBitmap(comboBitmap);
        comboParams = new ConstraintLayout.LayoutParams(60, 60);
        comboImage.setLayoutParams(comboParams);
        comboImage.setX(0);
        comboImage.setY(0);
        comboImage.setVisibility(View.INVISIBLE);
        constraintLayout.addView(comboImage);
    }

    private void reklamiYukle() {
        RewardedAd.load(this, "ca-app-pub-2727876875460311/3322868658",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });

    }

    private void canKazanmaMenusu() {
        //dialog tanımlayır boyutlandırma işlemlerini yapıyoruz. butonlara basıldığında yapılacak işlemler aynı.
        getplayHeartDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(getplayHeartDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getplayHeartDialog.setCancelable(false);
        getplayHeartDialog.setContentView(R.layout.custom_dialog_playshop);
        getplayHeartImgClose = getplayHeartDialog.findViewById(R.id.custom_dialog_play_imageViewClose);
        playimageShowAndGet = getplayHeartDialog.findViewById(R.id.custom_dialog_play_ImageViewShowAndGet);
        getplayHeartImgClose.setOnClickListener(v -> getplayHeartDialog.dismiss());
        playimageShowAndGet.setOnClickListener(v -> reklamiGoster());
        getplayHeartDialog.getWindow().setAttributes(params);
        getplayHeartDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void reklamiGoster() {
        if (mRewardedAd == null) {
            Toast.makeText(this, "Reklam Yüklenemedi.\n Lütfen Daha Sonra Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
            reklamiYukle();
        } else {
            Activity activityContext = PlayActivity.this;
            getplayHeartDialog.dismiss();
            mRewardedAd.show(activityContext, rewardItem -> {
                sonHakSayisi = heartCount;
                heartCount = heartCount + 3;
                canSayisiniArttir(heartCount, sonHakSayisi);
                userHeart.setText("+" + heartCount);
            });
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    imgHeart.setX(cardView.getPivotX() + 250);
                    imgHeart.setY(cardView.getPivotY() + 250);
                    imgHeart.setVisibility(View.VISIBLE);
                    imgHeartXPos = (imgHeartKon.getX() + (imgHeartKon.getWidth() / 2f) + 16);
                    imgHeartYPos = (imgHeartKon.getY() + (imgHeartKon.getHeight() / 2f) - 32);
                    objectAnimatorImgHeartX = ObjectAnimator.ofFloat(imgHeart, "x", imgHeartXPos);
                    objectAnimatorImgHeartX.setDuration(imgHeartDuration);
                    objectAnimatorImgHeartY = ObjectAnimator.ofFloat(imgHeart, "y", imgHeartYPos);
                    objectAnimatorImgHeartY.setDuration(imgHeartDuration);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(objectAnimatorImgHeartX);
                    animatorSet.playTogether(objectAnimatorImgHeartY);
                    animatorSet.start();
                    objectAnimatorImgHeartY.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            imgHeart.setVisibility(View.INVISIBLE);
                            sonHakSayisi = heartCount;
                            heartCount = heartCount + 3;
                            canSayisiniArttir(heartCount, sonHakSayisi);
                            userHeart.setText("+" + heartCount);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            });
        }
    }


    private void canSayisiniArttir(int hCount, int sonHsayisi) {
        try {
            sqlSorgusu = "UPDATE Ayarlar SET k_heart = ? WHERE k_heart = ?";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindString(1, String.valueOf(hCount));
            statement.bindString(2, String.valueOf(sonHsayisi));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void puanSayisiniGetir() {
        firebaseFirestore.collection("Kullanıcılar").document(email).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(PlayActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            if (value != null) {
                HashMap hm = (HashMap) value.getData();
                long firebaseGelenPuan = (long) hm.get("Puan");
                firebasepuan = (int) firebaseGelenPuan;
                kontrol = firebasepuan + guncelPuan;
            }
        });
    }

    //Harf Al
    @SuppressLint("SetTextI18n")
    public void btn_harfAl(View v) {
        if (heartCount > 0) {
            rastgeleHarfAl();
            sonHaksayisi = heartCount;
            heartCount--;
            kalanHakkiKaydet(heartCount, sonHaksayisi);
        } else {
            Toast.makeText(getApplicationContext(), "Harf Alabilmek İçin Kalp Sayısı Yetersiz", Toast.LENGTH_SHORT).show();
        }
        userHeart.setText("+" + heartCount);
    }

    //Hak Sayısının kayıt olması
    public void kalanHakkiKaydet(int hCount, int sonHsayisi) {
        try {
            sqlSorgusu = "UPDATE Ayarlar SET k_heart = ? WHERE k_heart = ?";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindString(1, String.valueOf(hCount));
            statement.bindString(2, String.valueOf(sonHsayisi));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Random olarak çıkmamış harfların alınması
    private void rastgeleHarfAl() {
        if (kelimeHarfleri.size() > 0) {
            rndHarfNumber = rndHarf.nextInt(kelimeHarfleri.size());
            String[] txtHarfler = cevap.getText().toString().split(" ");
            char[] gelenKelimeHarfler = rastgeleKelime.toCharArray();
            for (int i = 0; i < rastgeleKelime.length(); i++) {
                if (txtHarfler[i].equals("_") && gelenKelimeHarfler[i] == kelimeHarfleri.get(rndHarfNumber)) {
                    txtHarfler[i] = String.valueOf(kelimeHarfleri.get(rndHarfNumber));
                    System.out.println(txtHarfler[i]);
                    kelimeBilgisi = "";
                    for (int j = 0; j < txtHarfler.length; j++) {
                        if (j < txtHarfler.length - 1) {
                            kelimeBilgisi = kelimeBilgisi + txtHarfler[j] + " ";
                        } else {
                            kelimeBilgisi = kelimeBilgisi + txtHarfler[j];
                        }
                    }
                    break;
                }
            }
            cevap.setText(kelimeBilgisi);
            kelimeHarfleri.remove(rndHarfNumber);
            if (kelimeHarfleri.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edittxtTahminiCevap.setText(rastgeleKelime);
                        Button tahmin;
                        tahmin = findViewById(R.id.tahmin_et);
                        tahmin.performClick();
                    }
                }, 100);
            }
        }
    }

    private void gecisReklaminiYukle() {
        InterstitialAd.load(this, "ca-app-pub-2727876875460311/6832899487", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    //Tahmin etme işlemi
    public void btn_tahminEt(View v) {
        txtTahminDegeri = edittxtTahminiCevap.getText().toString();
        if (!TextUtils.isEmpty(txtTahminDegeri))
            if (txtTahminDegeri.matches(rastgeleKelime)) {
                cevap.setText(rastgeleKelime);
                guncelPuan += 10;
                cozulenSoruSayisi++;
                kelimelerList.clear();
                mediaPlayer.start();
                comboAktivite++;
                dogruCevap = dogruCevap + 1;
                kontrol += 10;
                txtPuan.setText(String.valueOf(guncelPuan));
                edittxtTahminiCevap.setText("");
                cozulenKelimeSayisi++;
                hndler.postDelayed(() -> {
                    if (sorularList.size() > 0) {
                        rndSoruGetir();
                    } else {
                        maksimumVerileriHesapla("oyunBitti");
                    }
                }, 2000);
            } else {
                if (heartCount > 0) {
                    vibrator.vibrate(25);
                    sonHaksayisi = heartCount;
                    yapilanYanlisSayisi++;
                    kalanHakkiKaydet(heartCount, sonHaksayisi);
                } else {
                    maksimumVerileriHesapla("oyunBitti");
                    Toast.makeText(getApplicationContext(), "Oyun Bitti İstatistikleriniz...", Toast.LENGTH_SHORT).show();
                }
            }
        else {
            Toast.makeText(getApplicationContext(), "Cevap Boş Olamaz", Toast.LENGTH_SHORT).show();
        }
        if (cozulenSoruSayisi == 10) {
            guncelPuan += 100;
            cozulenSoruSayisi = 0;
        }
        if (comboAktivite == 5) {
            comboImage.setX(cardView.getPivotX() + 250);
            comboImage.setY(cardView.getPivotY() + 250);
            comboImage.setVisibility(View.VISIBLE);
            comboImage.setRotation(35);
            comboImageXPos = (comboImageKonum.getX() + (comboImageKonum.getWidth() / 2f) + 60);
            comboImageYPos = (comboImageKonum.getY() + (comboImageKonum.getHeight() / 2f) - 30);
            objectAnimatorComboX = ObjectAnimator.ofFloat(comboImage, "x", comboImageXPos);
            objectAnimatorComboX.setDuration(imgHeartDuration);
            objectAnimatorComboY = ObjectAnimator.ofFloat(comboImage, "y", comboImageYPos);
            objectAnimatorComboY.setDuration(comboImageDuration);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimatorComboX);
            animatorSet.playTogether(objectAnimatorComboY);
            animatorSet.start();
        }
        if (comboAktivite >= 5) {
            guncelPuan += 10;
        }
        if (dogruCevap == 10) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(this);
                sonHakSayisi = heartCount;
                heartCount = heartCount + 3;
                canSayisiniArttir(heartCount, sonHakSayisi);
                userHeart.setText("+" + heartCount);
            }
        }
        userHeart.setText("+" + heartCount);
    }


    private void rndSoruGetir() {
        //Soru sayısına göre random soru belirlemek için tanımlamalar yapıp soruyu yazdırıyoruz.
        rndSoruNumber = rndSoru.nextInt(sorularKodList.size());
        rastgeleSoru = sorularList.get(rndSoruNumber);
        rastgeleSoruKodu = sorularKodList.get(rndSoruNumber);
        soru.setText(rastgeleSoru);
        //Kelimelere ulaşmak için yapılan işlemler
        try {
            database = this.openOrCreateDatabase("NekiBuki", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT * FROM Kelimeler WHERE kkod=?", new String[]{rastgeleSoruKodu});
            int kelimeIndex = cursor.getColumnIndex("kelime");
            while (cursor.moveToNext()) {
                kelimelerList.add(cursor.getString(kelimeIndex));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rndmKelimeGetir();
    }

    private void rndmKelimeGetir() {
        //Yukarıda select kullanarak ulaştığımız sorunun cevaplarını random olarak seçip tekrar çıkmaması için remove ekliyoruz.
        kelimeBilgisi = "";
        rndKelimeNumber = rndKelime.nextInt(kelimelerList.size());
        rastgeleKelime = kelimelerList.get(rndKelimeNumber);
        System.out.println(rastgeleKelime);
        kelimeHarfleri = new ArrayList<>();
        //Kelimeyi harf harf diziye ekleme
        for (char harf : rastgeleKelime.toCharArray()) {

            kelimeHarfleri.add(harf);
        }
        for (int i = 0; i < kelimeHarfleri.size(); i++) {
            if (kelimeHarfleri.get(i) == '\040') {
                kelimeBilgisi = kelimeBilgisi + " ";
            } else {
                kelimeBilgisi = kelimeBilgisi + "_ ";
            }
            cevap.setText(kelimeBilgisi);
        }

        for (int i = 0; i < rastgeleBelirlenecekHarfSayisi; i++) {
            rastgeleHarfAl();
        }
        kelimeyiRastgeleYerlestir();
    }

    private void kelimeyiRastgeleYerlestir() {
        ArrayList<Character> kelime = new ArrayList();
        ArrayList<Character> kelime2 = new ArrayList();
        boolean kontrol = false;
        String randomYerKelime = "";
        ArrayList<Integer> randomHarfInx = new ArrayList<>();
        ArrayList<Integer> randomHarfInx2 = new ArrayList<>();
        for (char harfler : rastgeleKelime.toCharArray()) {
            if (harfler == '\040') {
                kontrol = true;
            } else if (harfler != '\040' && kontrol == false) {
                kelime.add(harfler);
            } else if (kontrol == true) {
                kelime2.add(harfler);
            }
        }
        while (randomHarfInx.size() < kelime.size()) {
            int v = rndYerRandom.nextInt(kelime.size());
            if (!randomHarfInx.contains(v)) {
                randomHarfInx.add(v);
            }
        }
        for (int i = 0; i < kelime.size(); i++) {
            randomYerKelime = randomYerKelime + kelime.get(randomHarfInx.get(i));
        }
        if (kontrol == true) {
            randomYerKelime = randomYerKelime + " ";
        }
        while (randomHarfInx2.size() < kelime2.size()) {
            int ve = rndYerRandom.nextInt(kelime2.size());
            if (!randomHarfInx2.contains(ve)) {
                randomHarfInx2.add(ve);
            }
        }
        if (kelime2.size() > 0) {
            for (int k = 0; k < kelime2.size(); k++) {
                randomYerKelime = randomYerKelime + kelime2.get(randomHarfInx2.get(k));
            }
        }
        rndmYerKelime.setText(randomYerKelime);
    }

    @Override
    protected void onStop() {
        firebasePuanGuncelle();
        super.onStop();
    }

    //Geri tuşuna basıldığında yapılacak işler ve animasyonlar
    @Override
    public void onBackPressed() {
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Ne ki Bu ki");
        alert.setMessage("Ana Menüye Dönmek İstediğinize Emin Misiniz?");
        alert.setIcon(R.mipmap.ic_kelimebilmecealert);
        alert.setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss());
        alert.setPositiveButton("Evet", ((dialog, which) -> mainIntent()));
        alert.show();

    }

    public void mainIntent() {
        Intent MainIntent = new Intent(this, Home.class);
        finish();
        startActivity(MainIntent);
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
    }

    private void maksimumVerileriHesapla(String oyunDurumu) {
        try {
            int a = 0;
            for (int i = 0; i < sorularKodList.size(); i++) {
                cursor = database.rawQuery("SELECT * FROM Kelimeler WHERE Kelimeler.kkod=?", new String[]{sorularKodList.get(i)});
                a = a + cursor.getCount();
            }
            maksimumKelimeSayisi = a;
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        istatistikTablosunuGoster(oyunDurumu, maksimumKelimeSayisi, cozulenKelimeSayisi, yapilanYanlisSayisi);
    }

    @SuppressLint("SetTextI18n")
    private void istatistikTablosunuGoster(String oyunDurumu, int maksimumKelimeSayisi, int cozulenKelimeSayisi, int yapilanYanlisSayisi) {
        statisticDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(statisticDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        statisticDialog.setContentView(R.layout.custom_dialog_statistic);
        statisticImageClose = statisticDialog.findViewById(R.id.custom_dialog_statistic_imageViewClose);
        statisticTableLinearLayout = statisticDialog.findViewById(R.id.custom_dialog_statistic_linearlayout);
        statisticTableBtnMainmenu = statisticDialog.findViewById(R.id.custom_dialog_statisticTable_btnMainMenu);
        statisticTableBtnPlayAgain = statisticDialog.findViewById(R.id.custom_dialog_statisticTable_PlayAganin);
        statisticFalseCount = statisticDialog.findViewById(R.id.custom_dialog_statistic_textViewFalseGuessCount);
        statisticWordCount = statisticDialog.findViewById(R.id.custom_dialog_statistic_textViewWordCount);
        statisticBarFalseCount = statisticDialog.findViewById(R.id.custom_dialog_statistic_processBarFalseGuessCount);
        statisticBarWordCount = statisticDialog.findViewById(R.id.custom_dialog_statistic_processBarWordCount);
        if (oyunDurumu.matches("oyunBitti")) {
            statisticDialog.setCancelable(false);
            statisticTableLinearLayout.setVisibility(View.VISIBLE);
            statisticImageClose.setVisibility(View.INVISIBLE);
        }
        statisticWordCount.setText(cozulenKelimeSayisi + " / " + maksimumKelimeSayisi);
        statisticFalseCount.setText(yapilanYanlisSayisi + " /" + maksimumKelimeSayisi);
        statisticBarFalseCount.setMax(maksimumKelimeSayisi);
        statisticBarWordCount.setMax(maksimumKelimeSayisi);
        statisticBarWordCount.setProgress(cozulenKelimeSayisi);
        statisticBarFalseCount.setProgress(yapilanYanlisSayisi);
        statisticImageClose.setOnClickListener(v -> statisticDialog.dismiss());
        statisticTableBtnMainmenu.setOnClickListener(v -> mainIntent());
        statisticTableBtnPlayAgain.setOnClickListener(v -> {
            guncelPuan = 0;
            Intent thisIntent = getIntent();
            finish();
            startActivity(thisIntent);
        });
        statisticDialog.getWindow().setAttributes(params);
        statisticDialog.show();
    }

    private void firebasePuanGuncelle() {
        firebaseFirestore.collection("Kullanıcılar").document(email).update("Puan", kontrol).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Güncellendi.");
            }
        });
    }

}

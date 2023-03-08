package com.bilmece.nekibuki.MenuActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bilmece.nekibuki.Categories.Kategori;
import com.bilmece.nekibuki.LoginNicknameSplashScreen.Login;
import com.bilmece.nekibuki.LoginNicknameSplashScreen.NetworkChangeReceiver;
import com.bilmece.nekibuki.R;
import com.bilmece.nekibuki.databinding.ActivityHomeBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {
    ActivityHomeBinding binding;
    Button leaderBoards;
    //Can Kazanma Dialog
    int heartIndex, heartCount, sonHakSayisi, kIndex;
    String kCount;
    TextView userHeart;
    Dialog getHeartDialog;
    WindowManager.LayoutParams params;
    ImageView getHeartImgClose, imageShowAndGet;
    //Database ve Firebase
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    FirebaseFirestore firebaseFirestore;
    String email;
    SQLiteDatabase database;
    String sqlSorgusu, sqlSorgusuprofil;
    SQLiteStatement statement, statementPhoto;
    Cursor cursor;
    String firebaseKayitliNickname;
    //Reklam
    AdView mAdView;
    ImageView reklamGetir;
    RewardedAd mRewardedAd;
    AdRequest adRequest;
    CircleImageView userProfilImage;
    //settingsDialog
    Button settingsBtn, profilResmi;
    Dialog settingDialog;
    ImageView close;
    Intent galleryShow;
    AlertDialog.Builder photoOnay;
    Bitmap secilenResimBitmap, gelenResimBitmap, smallerBitmapImage;
    int resimIndex;
    byte[] resimByte;
    ImageDecoder.Source resimDosyasi;
    //nasilonyanir
    Button nasiloynanir;
    Dialog nasiloynanirCustomDialog;
    ImageView nclose, insta;
    //Kalp Animasyon
    ConstraintLayout constraintLayout;
    ConstraintLayout.LayoutParams heartParams;
    ImageView imgHeart, imgHeartKon;
    Bitmap imgHeartBitmap;
    float imgHeartXPos, imgHeartYPos;
    int imgHeartDuration = 2500;
    ObjectAnimator objectAnimatorImgHeartX, objectAnimatorImgHeartY;
    AnimatorSet animatorSet;

    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver networkChangeReceiver;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constraintLayout = findViewById(R.id.home_activity_cons);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
        userProfilImage = findViewById(R.id.circleImage);
        //firebase auth ve firestore ulaşmak için tanımlamalar
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        checkUser();
        //singout butonuna tıklandığında kullanıcıdan çıkış
        leaderBoards = findViewById(R.id.leaderBoardClick);
        leaderBoards.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, leaderBoards.class);
            intent.putExtra("nickname", firebaseKayitliNickname);
            startActivity(intent);
        });
        //reklam
        mAdView = findViewById(R.id.main_activity_banner);
        AdRequest homebanner = new AdRequest.Builder().build();
        mAdView.loadAd(homebanner);
        reklamGetir = findViewById(R.id.reklamGetirBtn);
        adRequest = new AdRequest.Builder().build();
        reklamiYukle();
        MobileAds.initialize(this, initializationStatus -> {
        });
        reklamGetir.setOnClickListener(v -> canKazanmaMenusu());
        //can ve pp göstermek için kullanıyoruz.
        userHeart = findViewById(R.id.home_activity_canSayisi);
        try {
            database = this.openOrCreateDatabase("NekiBuki", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT * FROM Ayarlar", null);
            heartIndex = cursor.getColumnIndex("k_heart");
            kIndex = cursor.getColumnIndex("k_id");
            resimIndex = cursor.getColumnIndex("k_image");
            cursor.moveToFirst();
            heartCount = Integer.parseInt(cursor.getString(heartIndex));
            kCount = String.valueOf(cursor.getString(kIndex));
            userHeart.setText("+" + heartCount);
            resimByte = cursor.getBlob(resimIndex);
            gelenResimBitmap = BitmapFactory.decodeByteArray(resimByte, 0, resimByte.length);
            userProfilImage.setImageBitmap(gelenResimBitmap);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(v -> settingsCustomDialog());
        nasiloynanir = findViewById(R.id.nasiloynanir);
        nasiloynanir.setOnClickListener(v -> nasiloynanirCustomDialog());

        imgHeartKon = findViewById(R.id.playHeart_design);
        imgHeart = new ImageView(Home.this);
        imgHeartBitmap = BitmapFactory.decodeResource(Home.this.getResources(), R.drawable.heart);
        imgHeart.setImageBitmap(imgHeartBitmap);
        heartParams = new ConstraintLayout.LayoutParams(64, 64);
        imgHeart.setLayoutParams(heartParams);
        imgHeart.setX(0);
        imgHeart.setY(0);
        imgHeart.setVisibility(View.INVISIBLE);
        constraintLayout.addView(imgHeart);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onDestroy() { //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);//receiver durduruluyor

    }

    //FirebaseAut bağlantısında bir sorun olup olmadığını kontrol ediyoruz.
    private void checkUser() {
        //Firebase user tanımlayıp bilgileri alıyoruz.
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //Bilgilere göre intent
        if (firebaseUser == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        } else {//Gelen bilgi boş değilse kullanıcı adını filtreleyip ulaşma
            email = firebaseUser.getEmail();
            documentReference = firebaseFirestore.collection("Kullanıcılar").document(email);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    firebaseKayitliNickname = (String) documentSnapshot.getData().get("NickName");
                    binding.Email.setText(firebaseKayitliNickname);
                }
            }).addOnFailureListener(e -> Toast.makeText(Home.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void reklamiYukle() {
        RewardedAd.load(this, "ca-app-pub-2727876875460311/9364323979",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        Toast.makeText(Home.this, "Reklam Yüklendi.\n Şimdi Can Kazanma Zamanı", Toast.LENGTH_SHORT).show();
                        mRewardedAd = rewardedAd;
                    }
                });
    }


    private void nasiloynanirCustomDialog() {
        nasiloynanirCustomDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(nasiloynanirCustomDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        nasiloynanirCustomDialog.setCancelable(false);
        nasiloynanirCustomDialog.setContentView(R.layout.custom_dialog_info);
        nclose = nasiloynanirCustomDialog.findViewById(R.id.custom_dialog_info_imageViewClose);
        insta = nasiloynanirCustomDialog.findViewById(R.id.custom_dialog_info_imageviewGoToInstagram);
        nclose.setOnClickListener(v -> nasiloynanirCustomDialog.dismiss());
        insta.setOnClickListener(v -> callInstagram());
        nasiloynanirCustomDialog.getWindow().setAttributes(params);
        nasiloynanirCustomDialog.show();
    }

    private void callInstagram() {
        Uri uri = Uri.parse("https://www.instagram.com/nnekibuki/");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.instagram.android");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/xxx")));
        }
    }

    //settings butonuna basildiktan sonra olacak islemler
    private void settingsCustomDialog() {
        //Settings Dialog tanımlıyoruz
        settingDialog = new Dialog(this);
        //params ile ekranın boyutuna göre dialıgun boyutunu ayarlıyoruz.
        params = new WindowManager.LayoutParams();
        params.copyFrom(settingDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //dialog dışında bir yere tıklanırsa kapanmayacak
        settingDialog.setCancelable(false);
        settingDialog.setContentView(R.layout.custom_dialog_settings);
        profilResmi = settingDialog.findViewById(R.id.custom_dialog_btnChangeProfileImage);
        close = settingDialog.findViewById(R.id.custom_dialog_settings_imageViewClose);
        close.setOnClickListener(v -> settingDialog.dismiss());
        //profil resmini değiştire basıldığında alınacak izinler.
        profilResmi.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                galleryShow = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryShow, 2);
            }
        });
        //paramsı dialoga tanımlayıp gösterdik
        settingDialog.getWindow().setAttributes(params);
        settingDialog.show();

    }

    //İzin alındıysa yapılacak işlemler
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //galeriye yönlendirme işlemi
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryShow = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryShow, 2);
            }
        }
    }

    //Gelen data boş değilse gelecek değerlere göre işlemler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            //gelen veri boi değilse alert dialogla kullanıcıya onay işlemi soruyoruz.
            photoOnay = new AlertDialog.Builder(Home.this);
            photoOnay.setTitle("Ne ki Bu ki");
            photoOnay.setMessage("Profil Resminizi Değiştirmek İstediğinizden Emin Misiniz?");
            photoOnay.setIcon(R.mipmap.ic_kelimebilmecealert);
            photoOnay.setPositiveButton("Evet", (dialog, which) -> {
                //Evet dediyse gelen veriyi uriye çevirip bitmap yardımıyla Imagevieve atıyoruz.SmallerBitmap ile resmin mb azaltıyoruz.
                Uri imageUri = data.getData();
                try {
                    if (Build.VERSION.SDK_INT > 27) {
                        resimDosyasi = ImageDecoder.createSource(Home.this.getContentResolver(), imageUri);
                        secilenResimBitmap = ImageDecoder.decodeBitmap(resimDosyasi);
                    } else {
                        secilenResimBitmap = MediaStore.Images.Media.getBitmap(Home.this.getContentResolver(), imageUri);
                    }
                    smallerBitmapImage = makeSmallerImage(secilenResimBitmap, 300);
                    userProfilImage.setImageBitmap(smallerBitmapImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                databasePhotoUpdate(smallerBitmapImage);
            });
            photoOnay.setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss());
            photoOnay.show();
        }
    }

    //Gelen fotoğrafı database e kaydetme
    private void databasePhotoUpdate(Bitmap profilResmi) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            profilResmi.compress(Bitmap.CompressFormat.PNG, 1, outputStream);
            resimByte = outputStream.toByteArray();
            sqlSorgusuprofil = "UPDATE Ayarlar SET k_image = ? WHERE k_id = ?";
            statementPhoto = database.compileStatement(sqlSorgusuprofil);
            statementPhoto.bindBlob(1, resimByte);
            statementPhoto.bindString(2, kCount);
            statementPhoto.execute();
            if (settingDialog.isShowing()) {
                settingDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Profil Resminizi Başarıyla Değiştirildi.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Resim boyutunu düşürme
    public Bitmap makeSmallerImage(Bitmap image, int MaximumSize) {
        int widht = image.getWidth();
        int height = image.getHeight();
        float bitmapRadio = (float) widht / (float) height;
        if (bitmapRadio > 1) {
            widht = MaximumSize;
            height = widht / MaximumSize;
        } else {
            height = MaximumSize;
            widht = (int) (height * bitmapRadio);
        }
        return image.createScaledBitmap(image, widht, height, true);
    }


    //Can kazanma dialog
    private void canKazanmaMenusu() {
        //dialog tanımlayır boyutlandırma işlemlerini yapıyoruz. butonlara basıldığında yapılacak işlemler aynı.
        getHeartDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(getHeartDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getHeartDialog.setCancelable(false);
        getHeartDialog.setContentView(R.layout.custom_dialog_shop);
        getHeartImgClose = getHeartDialog.findViewById(R.id.custom_dialog_shop_imageViewClose);
        imageShowAndGet = getHeartDialog.findViewById(R.id.custom_dialog_shop_ImageViewShowAndGet);
        getHeartImgClose.setOnClickListener(v -> getHeartDialog.dismiss());
        imageShowAndGet.setOnClickListener(v -> reklamiGoster());
        getHeartDialog.getWindow().setAttributes(params);
        getHeartDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void reklamiGoster() {
        reklamiYukle();
        if (mRewardedAd == null) {
            Toast.makeText(this, "Reklam Yüklenemedi.\n Lütfen Daha Sonra Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
        } else{
            Activity activityContext = Home.this;
            mRewardedAd.show(activityContext, rewardItem -> getHeartDialog.dismiss());
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    System.out.println(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    imgHeart.setX(constraintLayout.getPivotX());
                    imgHeart.setY(constraintLayout.getPivotY());
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

    //Can sayısını database kaydetme
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Buton Geçişleri
    @SuppressLint("NonConstantResourceId")
    public void mainBtnClick(View v) {
        if (v.getId() == R.id.play) {
            Intent CategoriesIntent = new Intent(this, Kategori.class);
            finish();
            startActivity(CategoriesIntent);
            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        }
    }
}
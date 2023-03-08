package com.bilmece.nekibuki.LoginNicknameSplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bilmece.nekibuki.R;
import com.bilmece.nekibuki.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int RC_SING_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Google giriş ayarları
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        //Firebase Bağlantısı
        firebaseAuth = FirebaseAuth.getInstance();
        checkuser();
        //Butona basıldığında yapılacaklar
        binding.signIn.setOnClickListener(v -> {
            //Google Giriş ekranın açılması
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SING_IN);
        });
    }

    //Firebasede böyle bir kayıt varsa yönlendirme
    private void checkuser() {
        //Firebasede user bilgilerini çekme
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //firebaseuser boş değilse home activitye yönlendirme
        if (firebaseUser != null) {
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
        }
    }

    //SignIne tıklandıktan sonra gelen veriye yani requestcode göre yapılacaklar işlemler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request code eşitse yapılacaklar
        if (requestCode == RC_SING_IN) {
            //Accountda bulunan deperler task değişkenine atılıp firebaseAuthWithGoogleAccount ekleniyor.
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Firebase'e kayıt olacak kullanıcının verilerini kaydedilip intent yardımıyla activity değiştirme
    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        //Kimlik Doğrulama işlemi yapmak için AuthCredential kullanıyoruz.
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //Sistemde kayıtlı olan kullanıcı durumunu kontrol ediyoruz
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            //Yeni Bir kullanıcı kayıt oluyorsa yapılacak işlemler
            if (authResult.getAdditionalUserInfo().isNewUser()) {
                Toast.makeText(Login.this, "Kullanıcı Oluşturuldu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, NickName.class));
            } else {//Kayıtlı bir kullanıcı varsa yapılacak işlemler
                Toast.makeText(Login.this, "Tekrar Hoşgeldiniz", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, SplashScreenActivity.class));
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }
}
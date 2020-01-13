package com.burak.tour.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.burak.tour.R;
import com.burak.tour.helperClasses.NetworkCheckingClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.burak.tour.MainActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView register;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        progressBar=findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();


        if(firebaseUser != null){ // check user session

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                String validemail= "[a-zA-Z0-9\\+\\.\\_\\-\\+]{1,256}"+
                        "\\@"+
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                        "("+
                        "\\."+
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+
                        ")+";
                Matcher matcher= Pattern.compile(validemail).matcher(txt_email);

                if (NetworkCheckingClass.isNetworkAvailable(getApplicationContext())) {

                    progressBar.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(txt_email)) {
                        progressBar.setVisibility(View.GONE);
                        email.setError("Lütfen emailinizi giriniz");
                        return;
                    }
                    else if(!matcher.matches()){
                        progressBar.setVisibility(View.GONE);
                        email.setError("Geçersiz E-Posta");
                    }
                    else if (TextUtils.isEmpty(txt_password)) {
                        progressBar.setVisibility(View.GONE);
                        password.setError("Lütfen parolanızı giriniz");
                    } else if (txt_password.length() < 6) {
                        progressBar.setVisibility(View.GONE);
                        password.setError("Parola en az 6 haneli olmalıdır");
                    } else{
                        loginUser(txt_email , txt_password);
                    }



                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "İnternet Bağlantısı Yok", Toast.LENGTH_LONG).show();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
            }
        });




    }

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this , MainActivity.class));
                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this, "E-Posta veya Şifre Yanlış", Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }
    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Uygulamayı kapatmak istediğinize emin misiniz?")
                .setCancelable(false)
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();

    }
}

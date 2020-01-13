package com.burak.tour.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.burak.tour.R;
import com.burak.tour.helperClasses.NetworkCheckingClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.burak.tour.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        progressBar=findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        registerUser(txt_email, txt_password);
                    }

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "İnternet Bağlantısı Yok", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(String email, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}

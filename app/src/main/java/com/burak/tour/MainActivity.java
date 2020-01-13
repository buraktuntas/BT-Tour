package com.burak.tour;

/**
 * Created by tahakirca on 11/01/15.
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.burak.tour.Interface.ApiInterface;
import com.burak.tour.R;
import com.burak.tour.fragments.CategoryFragment;
import com.burak.tour.fragments.ListFragment;
import com.burak.tour.retrofit.RetrofitApiClient;

public class MainActivity extends Activity{
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);


        FragmentManager fm = getFragmentManager();//Fragment Manager objesi oluşturuyoruz.//getFragmentManager() methodu Activity class ının methodudur
        FragmentTransaction ft = fm.beginTransaction();//Fragment oluşturabilmek için fm objesinin beginTransaction() methodunu çağırıyoruz
        ft.add(R.id.frame1,new CategoryFragment()); // Fragmentları ekleyeceğimiz yerleri ve class larını set ediyoruz
        ft.add(R.id.frame2,new ListFragment()); //Fragmentları ekleyeceğimiz yerleri ve class larını set ediyoruz
        ft.commit();//commit methodu ile değişiklikleri uyguluyoruz

    }
    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
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

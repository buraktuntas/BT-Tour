package com.burak.tour.fragments;
/**
 * Created by tahakirca on 11/01/15.
 */

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burak.tour.Interface.UpdateInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.burak.tour.adapter.CategoryAdapter;
import com.burak.tour.helperClasses.NetworkCheckingClass;
import com.burak.tour.Interface.ApiInterface;
import com.burak.tour.user.LoginActivity;
import com.burak.tour.model.Tour;
import com.burak.tour.R;
import com.burak.tour.retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements UpdateInterface {
    RecyclerView recyclerViewHorizontal;
    CategoryAdapter categoryAdapter;
    List<Tour> list= new ArrayList<>();
    List<Tour> filter= new ArrayList<>();
    ArrayList<String> category= new ArrayList<String>();
    ProgressBar progressBar;
    TextView logout;
    private ApiInterface apiInterface;
    String ctgry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        logout = view.findViewById(R.id.logout);

        if(ctgry==null){
            ctgry="empty";
        }
        recyclerViewHorizontal = view.findViewById(R.id.horizontal_recycler_view);
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        if (NetworkCheckingClass.isNetworkAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            fetchData();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "İnternet Bağlantısı Yok", Toast.LENGTH_LONG).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(),"Çıkış Yapıldı!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }
    public void fetchData() {

        Call<Tour[]> call = apiInterface.apiCall();
        call.enqueue(new Callback<Tour[]>() {
            @Override
            public void onResponse(@NonNull Call<Tour[]> call, @NonNull Response<Tour[]> response) {

               progressBar.setVisibility(View.GONE);

                for (Tour p : response.body()){

                    if(ctgry.equals(p.getCategory())){
                        filter.add(p);
                    }

                    list.add(p);
                    category.add(p.getCategory());
                }

                //Tekrar eden category isimlerinin eklenmemesi için
                Set<String> set = new HashSet<>(category);
                category.clear();
                category.addAll(set);


                if (list != null) {

                    categoryAdapter = new CategoryAdapter(getActivity(), category, CategoryFragment.this);
                    recyclerViewHorizontal.setAdapter(categoryAdapter);

                } else {
                    Toast.makeText(getActivity(), "JSON body is null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Tour[]> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void kaydetClick(String data) {
        Log.v("ctgryInt:",String.valueOf(data));
        FragmentManager fm = getFragmentManager();
        ListFragment f2 = (ListFragment)fm.findFragmentById(R.id.frame2);
        f2.fetchData(data);
    }
}

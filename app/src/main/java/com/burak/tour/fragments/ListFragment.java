package com.burak.tour.fragments;
/**
 * Created by tahakirca on 11/01/15.
 */


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.burak.tour.adapter.ListAdapter;
import com.burak.tour.helperClasses.NetworkCheckingClass;
import com.burak.tour.Interface.ApiInterface;
import com.burak.tour.model.Tour;
import com.burak.tour.R;
import com.burak.tour.retrofit.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment{
    RecyclerView recyclerViewVertical;
    ListAdapter listAdapter;
    List<Tour> list= new ArrayList<>();
    List<Tour> filter= new ArrayList<>();
    ProgressBar progressBar;
    private ApiInterface apiInterface;
    String ctgry_name;
    TextView numberTour,texView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        progressBar = view.findViewById(R.id.progressBar);

        numberTour = view.findViewById(R.id.numberTour);
        texView = view.findViewById(R.id.textView);
        recyclerViewVertical = view.findViewById(R.id.vertical_recycler_view);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if(ctgry_name==null)
            ctgry_name="empty";
        numberTour.setText("");

        if (NetworkCheckingClass.isNetworkAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            fetchData(ctgry_name);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_LONG).show();
        }

        return view;

    }

    public void fetchData(String ctgry) {

        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        this.ctgry_name=ctgry;
        Call<Tour[]> call = apiInterface.apiCall();
        call.enqueue(new Callback<Tour[]>() {
            @Override
            public void onResponse(@NonNull Call<Tour[]> call, @NonNull Response<Tour[]> response) {

                progressBar.setVisibility(View.GONE);
                list.clear();
                if(filter.size()==0){

                    for (Tour p : response.body()){

                        if(ctgry_name.equals(p.getCategory())){
                            filter.add(p);
                        }

                        list.add(p);
                    }
                }

                else{
                    filter.clear();

                    for (Tour p : response.body()){

                        if(ctgry_name.equals(p.getCategory())){
                            filter.add(p);
                        }

                        list.add(p);

                    }
                }

                texView.setText("Group Tours in İntesasoft");

                if (filter.size() != 0) {
                    numberTour.setText(filter.size() + " Tour Available");
                } else {
                    numberTour.setText(list.size() + " Tour Available");
                }

                if (list != null) {
                    //Log.v("ctgryIntRef:",String.valueOf(ctgry_name));
                    if(ctgry_name.equals("empty") || ctgry_name.equals("all"))
                        listAdapter = new ListAdapter(getActivity(), list);
                    else
                        listAdapter = new ListAdapter(getActivity(), filter);

                        recyclerViewVertical.setAdapter(listAdapter);
                }

                else {
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



}

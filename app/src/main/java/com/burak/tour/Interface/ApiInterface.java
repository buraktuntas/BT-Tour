package com.burak.tour.Interface;

import com.burak.tour.model.Tour;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("api/v1/tour")
    Call<Tour[]> apiCall();

}


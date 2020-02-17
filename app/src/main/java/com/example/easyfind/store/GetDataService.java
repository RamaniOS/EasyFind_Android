package com.example.easyfind.store;

import com.example.easyfind.models.BaseBusiness;
import com.example.easyfind.models.Business;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    String API_KEY = "H7_n0PCOUjVZEMUpa1HCeC6LFPts3LXArEZbjpURqiis-40Cu3wQER1SAENdf4t08Td08hODQQMinYB4U97GpJ4vMptJHQdOz1ELeONy7KxyiQiBB1aLyHUJBczOXXYx";

    @Headers({"Authorization: Bearer " + API_KEY})
    @GET("businesses/search")
    Call<BaseBusiness> getBaseBusiness(@Query("location") String location,
                                       @Query("limit") int limit);

    @Headers({"Authorization: Bearer " + API_KEY})
    @GET("businesses/{id}")
    Call<Business> getBusinessDetail(@Path("id") String id);

}

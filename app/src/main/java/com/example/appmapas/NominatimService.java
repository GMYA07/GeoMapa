package com.example.appmapas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimService {
    @GET("search")
    Call<List<PlaceResult>> searchPlace(
            @Query("q") String query,
            @Query("format") String format,
            @Query("limit") int limit,
            @Query("addressdetails") int addressDetails
    );
}

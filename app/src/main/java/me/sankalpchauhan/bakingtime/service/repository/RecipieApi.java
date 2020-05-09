package me.sankalpchauhan.bakingtime.service.repository;

import java.util.List;

import me.sankalpchauhan.bakingtime.service.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipieApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecepieJSON();
}

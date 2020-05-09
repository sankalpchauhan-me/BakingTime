package me.sankalpchauhan.bakingtime.service.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.sankalpchauhan.bakingtime.service.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * The recipie repository fetches all the requests from the netowrk
 */
public class RecipieRepository {
    private static RecipieRepository recipieRepository;
    private RecipieApi recipieApi;

    public RecipieRepository() {
        recipieApi = RetrofitService.createService(RecipieApi.class);
    }

    /**
     * Singleton design pattern
     *
     * @return instance
     */
    public static RecipieRepository getInstance() {
        if (recipieRepository == null) {
            recipieRepository = new RecipieRepository();
        }
        return recipieRepository;
    }

    public MutableLiveData<List<Recipe>> getRecipieListFromNetwork() {
        final MutableLiveData<List<Recipe>> listMutableLiveData = new MutableLiveData<>();
        recipieApi.getRecepieJSON().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Timber.d(response.toString());
                if (response.isSuccessful() && response.code() == 200) {
                    listMutableLiveData.setValue(response.body());
                } else {
                    listMutableLiveData.setValue(null);
                    Timber.d("Request failed with error" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                //Log.d("Test", t.toString());
                listMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });
        return listMutableLiveData;
    }
}

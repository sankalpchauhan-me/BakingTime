package me.sankalpchauhan.bakingtime.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.service.repository.RecipieRepository;

/**
 * MainActivityViewModel to persist data and survive configuration changes while limiting api network calls
 */
public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recipe>> listMutableLiveData;
    private RecipieRepository recipieRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (listMutableLiveData != null) {
            return;
        }
        recipieRepository = recipieRepository.getInstance();
        listMutableLiveData = recipieRepository.getRecipieListFromNetwork();
    }

    public LiveData<List<Recipe>> getRecipieList() {
        return listMutableLiveData;
    }

}

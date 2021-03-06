package org.otfusion.caturday.service;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

import org.otfusion.caturday.db.repository.FavoriteCatRepository;
import org.otfusion.caturday.common.model.Cat;
import org.otfusion.caturday.providers.catapi.CatApiAsyncTask;
import org.otfusion.caturday.providers.catapi.CatApiProvider;
import org.otfusion.caturday.service.images.StorageImageService;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class CatServiceImpl implements CatService {

    private Bus bus;
    private CatApiProvider catApiProvider;
    private final FavoriteCatRepository favoriteCatRepository;
    private final StorageImageService storageImageService;

    @Inject
    public CatServiceImpl(Bus bus, CatApiProvider catApiProvider, FavoriteCatRepository
            favoriteCatRepository, StorageImageService storageImageService) {
        this.bus = bus;
        this.catApiProvider = catApiProvider;
        this.favoriteCatRepository = favoriteCatRepository;
        this.storageImageService = storageImageService;
    }

    @Override
    public void getCatFromApi() {
        CatApiAsyncTask catApiAsyncTask = new CatApiAsyncTask(bus, catApiProvider);
        catApiAsyncTask.execute();
    }

    @Override
    public long saveCatToFavorites(@NonNull Cat cat) {
        long catId = favoriteCatRepository.saveFavoriteCat(cat);
        storageImageService.saveImageIntoStorage(cat);
        return catId;
    }

    @Override
    public void deleteFromFavorites(Cat cat) {
        favoriteCatRepository.deleteFromFavorites(cat);
        storageImageService.deleteImageFromStorage(cat);
    }

    @Override
    public boolean isCatInFavorites(@NonNull Cat cat) {
        Map<String, Cat> favoriteCatsMap = favoriteCatRepository.getFavoriteCatsMap();
        return favoriteCatsMap.containsKey(cat.getProviderId());
    }

    @Override
    public List<Cat> getFavoriteCats() {
        return favoriteCatRepository.getFavoriteCats();
    }
}

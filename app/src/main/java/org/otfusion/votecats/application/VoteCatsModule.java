package org.otfusion.votecats.application;

import com.mobprofs.retrofit.converters.SimpleXmlConverter;
import com.squareup.otto.Bus;

import org.otfusion.votecats.providers.catapi.CatApiProvider;
import org.otfusion.votecats.providers.catapi.CatApiService;
import org.otfusion.votecats.service.CatServiceImpl;
import org.otfusion.votecats.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(library = true, injects = MainActivity.class)
public class VoteCatsModule {

    @Provides
    @Singleton
    public CatServiceImpl providesCatService(Bus bus, CatApiProvider catApiProvider) {
        return new CatServiceImpl(bus, catApiProvider);
    }

    @Provides
    public CatApiProvider providesCatApiProvider(CatApiService catApiService) {
        return new CatApiProvider(catApiService);
    }

    @Provides
    @Singleton
    public CatApiService providesCatApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(CatApiProvider.ENDPOINT)
                .setConverter(new SimpleXmlConverter())
                .build();
        return restAdapter.create(CatApiService.class);
    }

    @Provides
    @Singleton
    public Bus providesBus() {
        return new Bus();
    }

}
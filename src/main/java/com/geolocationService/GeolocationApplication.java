package com.geolocationService;

import com.geolocationService.cache.CacheStore;
import com.geolocationService.controller.GeolocationController;
import com.geolocationService.dao.GeoLocationDAO;
import com.geolocationService.entity.GeoLocation;
import com.geolocationService.service.GeolocationService;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import java.util.concurrent.TimeUnit;

public class GeolocationApplication extends Application<Configuration> {

    public static void main(final String[] args) throws Exception {
        new GeolocationApplication().run(args);
    }

    @Override
    public String getName() {
        return "demo";
    }

    private final HibernateBundle<Configuration> hibernate = new HibernateBundle<Configuration>(GeoLocation.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(Configuration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }


    @Override
    public void run(final Configuration configuration,
                    final Environment environment) {
        final CacheStore<GeoLocation> cacheStore = new CacheStore<>(60, TimeUnit.SECONDS);
        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());

        final GeoLocationDAO geoLocationDAO = new GeoLocationDAO(hibernate.getSessionFactory());
        final GeolocationService service = new GeolocationService(geoLocationDAO, client, cacheStore);
        environment.jersey().register(new GeolocationController(service));

    }

}

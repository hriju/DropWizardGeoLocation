package com.geolocationService.service;

import com.geolocationService.cache.CacheStore;
import com.geolocationService.dao.GeoLocationDAO;
import com.geolocationService.entity.GeoLocation;
import com.geolocationService.exceptions.GeoLocationNotFoundException;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GeolocationService {
    private static Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);
    private GeoLocationDAO geoLocationDAO;
    private Client client;
    CacheStore<GeoLocation> cacheStore;

    public GeolocationService(GeoLocationDAO dao, Client client, CacheStore<GeoLocation> cacheStore) {
        this.geoLocationDAO = dao;
        this.client = client;
        this.cacheStore = cacheStore;
    }

    @UnitOfWork
    public GeoLocation getGeoLocationByName(String ipAddress) {
        GeoLocation geoLocation;
        geoLocation = cacheStore.get(ipAddress);
        if (geoLocation == null) {
            LOGGER.info("Not found in cache", ipAddress);
            try {
                geoLocation = geoLocationDAO.findByName(ipAddress);
            } catch (NoResultException e) {
                LOGGER.info("Could not find any information for" + ipAddress + " " + e.getLocalizedMessage());
            }
            if (geoLocation == null) {
                LOGGER.info("not found in db", ipAddress);
                try {
                    WebTarget webTarget = client.target("http://ip-api.com/json/" + ipAddress);
                    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
                    Response response = invocationBuilder.get();
                    geoLocation = response.readEntity(GeoLocation.class);
                } catch (Exception e) {
                    throw new GeoLocationNotFoundException("Sorry, could not find any data for this IP:" + ipAddress);
                }
                LOGGER.info("This is from api call" + ipAddress);
                try {
                    geoLocationDAO.insert(geoLocation);
                } catch (Exception e) {
                    throw e;
                }
            }
            cacheStore.add(ipAddress, geoLocation);
        }
        return geoLocation;
    }
}

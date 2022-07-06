package com.geolocationService.exceptions;

public class GeoLocationNotFoundException extends RuntimeException{
    public GeoLocationNotFoundException (String msg){
        super(msg);
    }
}

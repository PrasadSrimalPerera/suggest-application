package com.task.suggest.index;

import java.io.Serializable;

/**
 * Created by prasad on 6/30/18.
 * GeoLocation object represents the geo location with two coordinates of {latitude, longitude}
 */
public class GeoLocation implements Serializable {
    private static final long serialVersionUID = 2998663114900115635L;
    private double latitude;
    private double longitude;

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

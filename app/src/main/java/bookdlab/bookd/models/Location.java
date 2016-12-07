package bookdlab.bookd.models;

import org.parceler.Parcel;

/**
 * Created by akhmedovi on 11/26/16.
 * Copyright - 2016
 */

@Parcel
public class Location {

    double latitude;
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

package bookdlab.bookd.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */

@Parcel
public class Business {

    String id;                  // Unique for business
    String owner;               // Only the business owner can edit a business
    String name;                // Visible name of the business
    String about;               // Description of business
    String address;             // Full address of business
    String city;                // Internal only; Useful for querying
    Double latitude;            // Internal only; Useful for querying
    Double longitude;           // Internal only; Useful for querying
    Double servingRadius;       // How long the performers are willing to travel
    String imageURL;            // Backdrop URL
    String businessURL;         // Any public business URL
    double rating;              // Rating. Needs to be updated upon review
    Long reviewCount;           // Number of reviews
    boolean instantBook;        // Optional: Instant book -> AirBnb style
    ArrayList<String> tags;     // Tags for this business -> Food, Music etc.,
    int price;
    String phone;               // Contact info
    String distance;            // ? This should be calculated per user upon their location
    String logoURL;             // Logo for the business
    //TODO: Add Social

    public Business() {
    }

    public Business(String id, String owner, String name, String about, String address, String city, Double latitude, Double longitude, Double servingRadius, String imageURL, String businessURL, double rating, Long reviewCount, boolean instantBook, ArrayList<String> tags, int price, String phone, String distance, String logoURL) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.about = about;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servingRadius = servingRadius;
        this.imageURL = imageURL;
        this.businessURL = businessURL;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.instantBook = instantBook;
        this.tags = tags;
        this.price = price;
        this.phone = phone;
        this.distance = distance;
        this.logoURL = logoURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getServingRadius() {
        return servingRadius;
    }

    public void setServingRadius(Double servingRadius) {
        this.servingRadius = servingRadius;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBusinessURL() {
        return businessURL;
    }

    public void setBusinessURL(String businessURL) {
        this.businessURL = businessURL;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isInstantBook() {
        return instantBook;
    }

    public void setInstantBook(boolean instantBook) {
        this.instantBook = instantBook;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String formPriceLabel() {
        String priceLabel = "";
        for (int i = 0; i < getPrice(); i++) {
            priceLabel += "$";
        }
        return priceLabel;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id='" + id + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", about='" + about + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", servingRadius=" + servingRadius +
                ", imageURL='" + imageURL + '\'' +
                ", businessURL='" + businessURL + '\'' +
                ", reviewCount=" + reviewCount +
                ", instantBook=" + instantBook +
                ", phone='" + phone + '\'' +
                ", distance='" + distance + '\'' +
                ", logoURL='" + logoURL + '\'' +
                '}';
    }
}

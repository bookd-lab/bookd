package bookdlab.bookd.models;

import org.parceler.Parcel;

/**
 * Created by pranavkonduru on 11/15/16.
 */

@Parcel
public class User {

    String id;                          // Unique ID
    String username;                    // Name
    String profileImageURL;             // Profile Image

    String city;                        // Internal only; Easier for DB queries
    String address;                     // Full address of user
    Double latitude;                    // Internal only; Easier for DB queries
    Double longitude;                   // Internal only; Easier for DB queries

    String about;                       // Description about user
    String phoneNumber;                 // Phone number of user for businesses to reach out
    String memberSince;                 // Optional: AirBnb style
    Boolean isVerified;                 // Optional: AirBnb style
    String lastSeenTime;                // Optional: AirBnb style

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(String lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }
}

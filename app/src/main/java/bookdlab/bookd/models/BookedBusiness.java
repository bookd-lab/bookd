package bookdlab.bookd.models;

import org.parceler.Parcel;

/**
 * Created by pranavkonduru on 11/20/16.
 */

@Parcel
public class BookedBusiness {

    String id;
    String eventId;
    String businessId;
    String category;

    public BookedBusiness() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

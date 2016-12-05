package bookdlab.bookd.models.Favorite;

import org.parceler.Parcel;

/**
 * Created by akhmedovi on 12/4/16.
 * Copyright - 2016
 */
@Parcel
public class Favorite {
    String _id;
    String creator;
    String business;

    public Favorite() {
    }

    public Favorite(String creator, String business) {
        this.creator = creator;
        this.business = business;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }
}

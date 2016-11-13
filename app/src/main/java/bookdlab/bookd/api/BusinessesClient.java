package bookdlab.bookd.api;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.models.Business;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */
public class BusinessesClient {

    //TODO: Mock data
    public List<Business> findBusinesses(String key) {

        List<Business> businessList = new ArrayList<>();

        Business business = new Business();
        business.setName("Antoine Pâtisserie");
        business.setAddress("Downtown 185 Park Ave San Jose, CA 95113");
        business.setPhone("Phone number (408) 372-4135");
        business.setDistance("5 miles");
        business.setImageUrl("https://static1.squarespace.com/static/51ab58f4e4b0361e5f3ed294/51ab58f4e4b0361e5f3ed29a/51ab80bee4b0058e26cfcb1f/1370194240299/Benchmark_Restaurant_Dining_Room_Photographed_by_Evan_Sung.jpg");
        business.setPrice(2);
        business.setRating(2.6);

        businessList.add(business);
        businessList.add(business);
        businessList.add(business);
        businessList.add(business);
        businessList.add(business);
        businessList.add(business);
        businessList.add(business);
        businessList.add(business);

        return businessList;
    }

}

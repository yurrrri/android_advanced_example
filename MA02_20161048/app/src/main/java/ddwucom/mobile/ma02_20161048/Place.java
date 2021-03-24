package ddwucom.mobile.ma02_20161048;

import java.io.Serializable;

public class Place implements Serializable {
    private long id;
    private String category;
    private String location;
    private String name;
    private String review;
    private float rate;

    public Place(){};

    public Place(long id, String category, String location, String name, String review, float rate) {
        this.id = id;
        this.category = category;
        this.location = location;
        this.name = name;
        this.review = review;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}

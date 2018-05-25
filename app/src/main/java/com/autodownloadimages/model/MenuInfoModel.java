package com.autodownloadimages.model;

/**
 * Created by Sunil kumar yadav on 14/2/18.
 */

public class MenuInfoModel extends BaseModel {

    int id;
    String name;
    String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

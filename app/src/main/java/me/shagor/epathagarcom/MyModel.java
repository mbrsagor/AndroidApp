package me.shagor.epathagarcom;

/**
  Created by shagor on 10/17/2017.
 */

public class MyModel {

    private String postID,title,date,thumbnail;

    //Constractor
    public MyModel(String postID, String title, String date, String thumbnail) {
        this.postID = postID;
        this.title = title;
        this.date = date;
        this.thumbnail = thumbnail;
    }

    public MyModel() {
    }

    // Getter & Setter
    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
